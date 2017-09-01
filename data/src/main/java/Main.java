import com.fasterxml.jackson.databind.ObjectMapper;
import model.Album;
import model.Artist;
import org.apache.commons.lang3.StringUtils;
import parser.AlbumParser;
import parser.ArtistParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

// TODO type tags
public class Main {

    private static final String JSON_EXTENSION_NAME = ".json";
    private static final int DEFAULT_NUMBER_OF_DOCS = 10_000;

    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Required 2 arguments: DumpPath and ResultDirectory. Optional 3-d argument: DocsNum");
            return;
        }

        String dumpPath = args[0];
        String artistDirectoryPath = args[1] + File.separator + "artists";
        String albumDirectoryPath = args[1] + File.separator + "albums";

        int documentsNumber = DEFAULT_NUMBER_OF_DOCS;
        if (args.length >= 3 && StringUtils.isNumeric(args[2])) {
            int docsNumArgument = Integer.valueOf(args[2]);
            if (docsNumArgument > 0) {
                documentsNumber = docsNumArgument;
            }
        }

        ArtistParser artistParser = new ArtistParser();
        List<Artist> artists = artistParser.parseArtists(dumpPath, documentsNumber);

        AlbumParser albumParser = new AlbumParser();
        List<Album> albums = albumParser.parsAlbums(dumpPath, artists);

        createDirectoryIfNotExists(artistDirectoryPath);
        artists.forEach(artist -> writeArtistJson(artist, artistDirectoryPath));

        createDirectoryIfNotExists(albumDirectoryPath);
        albums.forEach(album -> writeAlbumJson(album, albumDirectoryPath));
    }

    private static void writeArtistJson(Artist artist, String directoryPath) {


        try {
            mapper.writeValue(new File(directoryPath + File.separator + artist.getMbid() + JSON_EXTENSION_NAME), artist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeAlbumJson(Album album, String directoryPath) {
        try {
            mapper.writeValue(new File(directoryPath + File.separator + album.getMbid() + JSON_EXTENSION_NAME), album);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDirectoryIfNotExists(String directoryPath) {

        File directory = new File(directoryPath);
        if (!directory.isDirectory() && !directory.mkdirs()) {
            throw new IllegalStateException("Unable to create output directory at '" + directoryPath + "'");
        }
    }
}
