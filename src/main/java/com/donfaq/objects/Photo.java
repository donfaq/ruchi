package com.donfaq.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo {
    public Photo() {
    }

    /**
     * Access key for the photo
     */
    @JsonProperty("access_key")
    private String accessKey;

    /**
     * Album ID
     */
    @JsonProperty("album_id")
    private Integer albumId;

    /**
     * Date when uploaded
     */
    @JsonProperty("date")
    private Integer date;

    /**
     * Original photo height
     */
    @JsonProperty("height")
    private Integer height;

    /**
     * Photo ID
     */
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("images")
    private List<Image> images;

    /**
     * Latitude
     */
    @JsonProperty("lat")
    private Float lat;

    /**
     * Longitude
     */
    @JsonProperty("long")
    private Float lng;

    /**
     * Photo owner's ID
     */
    @JsonProperty("owner_id")
    private Integer ownerId;

    /**
     * Post ID
     */
    @JsonProperty("post_id")
    private Integer postId;

    @JsonProperty("sizes")
    private List<Photo> sizes;

    /**
     * Photo caption
     */
    @JsonProperty("text")
    private String text;

    /**
     * ID of the user who have uploaded the photo
     */
    @JsonProperty("user_id")
    private Integer userId;

    /**
     * Original photo width
     */
    @JsonProperty("width")
    private Integer width;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public List<Photo> getSizes() {
        return sizes;
    }

    public void setSizes(List<Photo> sizes) {
        this.sizes = sizes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "accessKey='" + accessKey + '\'' +
                ", albumId=" + albumId +
                ", date=" + date +
                ", height=" + height +
                ", id=" + id +
                ", images=" + images +
                ", lat=" + lat +
                ", lng=" + lng +
                ", ownerId=" + ownerId +
                ", postId=" + postId +
                ", sizes=" + sizes +
                ", text='" + text + '\'' +
                ", userId=" + userId +
                ", width=" + width +
                '}';
    }
}
