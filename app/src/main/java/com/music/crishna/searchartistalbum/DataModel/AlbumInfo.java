package com.music.crishna.searchartistalbum.DataModel;

import android.graphics.Bitmap;

/**
 * Created by Krishna on 11/3/17.
 */

public class AlbumInfo {
    String artistName;
    String releaseYear;
    String ablumTitle;
    Bitmap albumThump;

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

    public Bitmap getThumbnail() {
        return albumThump;
    }

    public void setThumnail(Bitmap thumbnail) {
        this.albumThump = thumbnail;
    }
}
