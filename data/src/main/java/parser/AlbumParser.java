package parser;

import client.CoverArtArchiveClient;
import model.Album;
import model.Artist;
import model.Track;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlbumParser {

    private List<Artist> artists;
    private String albumFilePath;
    private String albumStatusFilePath;
    private String albumPackagingFilePath;
    private String languageFilePath;
    private String mediumFilePath;
    private String trackFilePath;

    public List<Album> parsAlbums(String dumpPath, List<Artist> artists) {
        this.artists = artists;

        albumFilePath = dumpPath + File.separator + "release";
        albumStatusFilePath = dumpPath + File.separator + "release_status";
        albumPackagingFilePath = dumpPath + File.separator + "release_packaging";
        languageFilePath = dumpPath + File.separator + "language";
        mediumFilePath = dumpPath + File.separator + "medium";
        trackFilePath = dumpPath + File.separator + "track";

        List<Album> albums = parseAlbumFile();
        parseReleaseStatusFile(albums);
        parseReleasePackagingFile(albums);
        parseReleaseLanguageFile(albums);

        parseMediumFile(albums);
        parseTrackFile(albums);

        return albums;
    }

    private List<Album> parseAlbumFile() {
        Map<String, List<Artist>> artistMap = artists.stream()
                .filter(artist -> !StringUtils.isEmpty(artist.getArtistCreditId()))
                .collect(Collectors.groupingBy(Artist::getArtistCreditId));

        List<Album> albums = null;
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(albumFilePath))) {
            Stream<String[]> rows = stream.map(strRow ->
                    Arrays.stream(strRow.split("\t"))
                            .map(val -> val.equals("\\N") ? "" : val)
                            .toArray(String[]::new)

            );
            albums = rows.flatMap(row -> {
                List<Album> albumList = new LinkedList<>();
                List<Artist> artistList = artistMap.get(row[3]);
                if (artistList != null) {
                    artistList.forEach(artist -> {
                        Album album = parseAlbumRow(row, artist);
                        album.getArtistList().add(artist);
                        albumList.add(album);
                    });
                }

                return albumList.stream();
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return albums;
    }

    private List<Album> parseReleaseStatusFile(List<Album> albums) {
        Map<String, List<Album>> albumMap = albums.stream()
                .filter(album -> !StringUtils.isEmpty(album.getStatus()))
                .collect(Collectors.groupingBy(Album::getStatus));

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(albumStatusFilePath))) {
            Stream<String[]> rows = stream.map(strRow -> strRow.split("\t"));
            rows.forEach(row -> {
                List<Album> curAlbums = albumMap.get(row[0]);
                if (curAlbums != null) {
                    curAlbums.forEach(album -> album.setStatus(row[1]));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return albums;
    }

    private List<Album> parseReleasePackagingFile(List<Album> albums) {
        Map<String, List<Album>> albumMap = albums.stream()
                .filter(album -> !StringUtils.isEmpty(album.getPackaging()))
                .collect(Collectors.groupingBy(Album::getPackaging));

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(albumPackagingFilePath))) {
            Stream<String[]> rows = stream.map(strRow -> strRow.split("\t"));
            rows.forEach(row -> {
                List<Album> curAlbums = albumMap.get(row[0]);
                if (curAlbums != null) {
                    albums.forEach(album -> album.setPackaging(row[1]));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return albums;
    }

    private List<Album> parseReleaseLanguageFile(List<Album> albums) {
        Map<String, List<Album>> albumMap = albums.stream()
                .filter(album -> !StringUtils.isEmpty(album.getLanguage()))
                .collect(Collectors.groupingBy(Album::getLanguage));

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(languageFilePath))) {
            Stream<String[]> rows = stream.map(strRow -> strRow.split("\t"));
            rows.forEach(row -> {
                List<Album> curAlbums = albumMap.get(row[0]);
                if (curAlbums != null) {
                    albums.forEach(album -> album.setLanguage(row[1]));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return albums;
    }

    private List<Album> parseMediumFile(List<Album> albums) {

        Map<String, Album> albumIdAlbumMap = albums.stream()
                .collect(Collectors.toMap(Album::getPk, Function.identity()));

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(mediumFilePath))) {
            Stream<String[]> rows = stream.map(strRow -> strRow.split("\t"));
            rows.forEach(row -> {
                Album album = albumIdAlbumMap.get(row[1]);
                if (album != null) {
                    album.setMediumId(row[0]);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return albums;
    }

    private List<Album> parseTrackFile(List<Album> albums) {

        Map<String, Album> mediumIdAlbumMap = albums.stream()
                .collect(Collectors.toMap(Album::getMediumId, Function.identity()));

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(trackFilePath))) {
            Stream<String[]> rows = stream.map(strRow -> strRow.split("\t"));
            rows.forEach(row -> {
                Album album = mediumIdAlbumMap.get(row[3]);
                if (album != null) {
                    parseTrack(row, album);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return albums;
    }

    private Album parseAlbumRow(String[] values, Artist artist) {

        Album album = new Album();
        album.setId(values[1]);
        album.setPk(values[0]);
        album.setName(values[2]);
        album.setStatus(values[5]); //Status ID
        album.setPackaging(values[6]); //Packaging ID
        album.setLanguage(values[7]); //Language ID
        album.setScript(values[8]);
        album.setBarcode(values[9]);
        album.setMbid(values[1]);

        artist.getAlbumsIds().add(album.getId());

        CoverArtArchiveClient artArchiveClient = new CoverArtArchiveClient(album.getMbid());
        album.setCoverImageUrl(artArchiveClient.getCoverImage());
        album.setImagesUrls(artArchiveClient.getImages());

        return album;
    }

    public static Album parseTrack(String[] values, Album album) {
        Track track = new Track();
        track.setLength(Integer.parseInt(values[8]));
        track.setMbid(values[1]);
        track.setName(values[6]);
        track.setPosition(Integer.parseInt(values[4]));

        // TODO ARTIST CREDIT?
        album.addTrack(track);
        return album;
    }
}
