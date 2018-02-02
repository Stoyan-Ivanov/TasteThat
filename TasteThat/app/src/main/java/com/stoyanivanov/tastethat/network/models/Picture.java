package com.stoyanivanov.tastethat.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by stoyan-ivanov on 14.11.17.
 */

public class Picture {
    @SerializedName("title")
    private String title;

    @SerializedName("type")
    private String type;

    @SerializedName("media")
    private String imgUrl;

    @SerializedName("desc")
    private String description;

    @SerializedName("thumbnail")
    private String thumbnailUrl;

    @SerializedName("thumb_width")
    private int thumbnailWidth;

    @SerializedName("thumb_height")
    private int thumbnailHeight;

    @SerializedName("width")
    private String imageWidth;

    @SerializedName("height")
    private String imageHeight;

    @SerializedName("size")
    private String imageSize;

    @SerializedName("url")
    private String imageSource;

    @SerializedName("_id")
    private String id;

    @SerializedName("b_id")
    private String bId;

    @SerializedName("media_fullsize")
    private String mediaFullsize;

    @SerializedName("thumb_type")
    private String thumbnailType;

    @SerializedName("count")
    private int count;


    public Picture(String title, String type, String imgUrl, String description,
                   String thumbnailUrl, int thumbnailWidth, int thumbnailHeight,
                   String imageWidth, String imageHeight, String imageSize,
                   String imageSource, String id, String bId, String mediaFullsize,
                   String thumbnailType, int count) {

        this.title = title;
        this.type = type;
        this.imgUrl = imgUrl;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailWidth = thumbnailWidth;
        this.thumbnailHeight = thumbnailHeight;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imageSize = imageSize;
        this.imageSource = imageSource;
        this.id = id;
        this.bId = bId;
        this.mediaFullsize = mediaFullsize;
        this.thumbnailType = thumbnailType;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(int thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getMediaFullsize() {
        return mediaFullsize;
    }

    public void setMediaFullsize(String mediaFullsize) {
        this.mediaFullsize = mediaFullsize;
    }

    public String getThumbnailType() {
        return thumbnailType;
    }

    public void setThumbnailType(String thumbnailType) {
        this.thumbnailType = thumbnailType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}