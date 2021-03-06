package com.mapr.music.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object for {@link com.mapr.music.model.Album} model class.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumDto {

    @JsonProperty("_id")
    private String id;
    private String name;
    private String style;
    private String barcode;
    private String status;
    private String packaging;
    private String language;
    private String script;
    private String MBID;
    private String format;
    private String country;
    private List reviews;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("artists")
    private List<ArtistDto> artistList;

    @JsonProperty("catalog_numbers")
    private List catalogNumbers;

    @JsonProperty("tracks")
    private List<TrackDto> trackList;

    @JsonProperty("cover_image_url")
    private String coverImageUrl;

    @JsonProperty("images_urls")
    private List<String> imagesUrls;

    @JsonProperty("released_date")
    private Date releasedDateDay;

    @JsonProperty("rating")
    private Double rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<ArtistDto> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<ArtistDto> artistList) {
        this.artistList = artistList;
    }

    public List getCatalogNumbers() {
        return catalogNumbers;
    }

    public void setCatalogNumbers(List catalogNumbers) {
        this.catalogNumbers = catalogNumbers;
    }

    public List<TrackDto> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<TrackDto> trackList) {
        this.trackList = trackList;
    }

    public List getReviews() {
        return reviews;
    }

    public void setReviews(List reviews) {
        this.reviews = reviews;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getReleasedDateDay() {
        return releasedDateDay;
    }

    public void setReleasedDateDay(Date releasedDateDay) {
        this.releasedDateDay = releasedDateDay;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getMBID() {
        return MBID;
    }

    public void setMBID(String MBID) {
        this.MBID = MBID;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
