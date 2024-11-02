package com.example.mobile_project_g5;

import java.util.List;

public class AlbumClass {
    private String albumName;
    private String albumID;
    private String information;
    private ImageClass[] images;

    public AlbumClass(String albumName, String albumID, String information, ImageClass[] images) {
        this.albumName = albumName;
        this.albumID = albumID;
        this.information = information;
        this.images = images;
    }

    // Các getter và setter cho các thuộc tính
    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumID() {
        return albumID;
    }

    public String getInformation() {
        return information;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public ImageClass[] getImages() {
        return images;
    }

    public void setImages(ImageClass[] images) {
        this.images = images;
    }
}
