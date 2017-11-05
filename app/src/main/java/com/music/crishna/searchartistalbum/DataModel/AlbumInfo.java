package com.music.crishna.searchartistalbum.DataModel;

import android.graphics.Bitmap;

/**
 * Created by Krishna on 11/3/17.
 */

public class AlbumInfo {


    String artistID;
    String artistName;
    String releaseYear;
    String ablumTitle;
    String albumThumpURL;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getAblumTitle() {
        return ablumTitle;
    }

    public void setAblumTitle(String ablumTitle) {
        this.ablumTitle = ablumTitle;
    }

    public String getThumbnailURL() {
        return albumThumpURL;
    }

    public void setThumnailURL(String thumbnail) {
        albumThumpURL = thumbnail;
    }
    public String getArtistID() {
        return artistID;
    }

    public void setArtistID(String artistID) {
        this.artistID = artistID;
    }
}
