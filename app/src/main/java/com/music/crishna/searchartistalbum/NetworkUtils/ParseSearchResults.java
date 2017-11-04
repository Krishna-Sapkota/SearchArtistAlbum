package com.music.crishna.searchartistalbum.NetworkUtils;

import android.util.Log;

import com.music.crishna.searchartistalbum.DataModel.AlbumInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Krishna on 11/3/17.
 */

public class ParseSearchResults {
    String jsonData;
    String artistId;

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getArtistId() {

        JSONObject searchResults=null;

        try {
            searchResults = new JSONObject(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert searchResults != null;
        JSONObject id= null;
        try {
            id = searchResults.getJSONArray("results").getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String artistId= null;
        try {
            artistId = id.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("ID","ID:::"+artistId);
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }


    public ArrayList<AlbumInfo> getAlbumInfo(String releasesJSONText) {
        JSONObject releases=null;
        JSONArray albumsArray=null;
        ArrayList<AlbumInfo> albums=new ArrayList<AlbumInfo>(100);
        int current=0;
        try {
             releases=new JSONObject(releasesJSONText);
             albumsArray=releases.getJSONArray("releases");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        while (current<albumsArray.length()){
            try {

                JSONObject object=albumsArray.getJSONObject(current);
                AlbumInfo albumInfo=new AlbumInfo();
                albumInfo.setAblumTitle(object.getString("title"));
                albumInfo.setArtistName(object.getString("artist"));
                String abc=object.getString("thumb");
                Log.i("test:",abc);
              //  albumInfo.setThumnail(Integer.parseInt(object.getString("thumb")));
                albumInfo.setReleaseYear(object.optString("year"));



                albums.add(albumInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            current++;
        }
        return albums;
    }
}
