package com.example.mobile_project_g5;

import java.io.Serializable;

public class ImageClass implements Serializable {
    private int imageID;
    private String albumID;
    private String filePath;
    private String information;
    private int isFavorite;
    private String exifDatetime;
    private String activate;
    private int isSelected;
    private String deleteAt;
    private String type;

    public ImageClass(int imageID,
                      String albumID,
                      String filePath,
                      String information,
                      int isFavorite,
                      String exifDatetime,
                      String activate,
                      int isSelected,
                      String deleteAt, String type) {
        this.imageID = imageID;
        this.albumID = albumID;
        this.filePath = filePath;
        this.information = information;
        this.isFavorite = isFavorite;
        this.exifDatetime = exifDatetime;
        this.activate = activate;
        this.isSelected = isSelected;
        this.deleteAt = deleteAt;
        this.type = type;
    }

    // Các getter và setter cho các thuộc tính
    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getInformation() {
        return information;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getAlbumID() {
        return this.albumID;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getFilePath()
    {
        return this.filePath;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getExifDatetime() {
        return exifDatetime;
    }

    public void setExifDatetime(String exifDatetime) {
        this.exifDatetime = exifDatetime;
    }

    public String getActivate() {
        return activate;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(String deleteAt) {
        this.deleteAt = deleteAt;
    }
    public String getType(){
        return type;
    }

}
