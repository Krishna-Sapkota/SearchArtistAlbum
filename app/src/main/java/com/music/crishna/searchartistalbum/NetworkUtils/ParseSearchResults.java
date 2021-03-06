package com.music.crishna.searchartistalbum.NetworkUtils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.music.crishna.searchartistalbum.AlbumActivity;
import com.music.crishna.searchartistalbum.DataModel.AlbumInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Krishna on 11/3/17.
 */

public class ParseSearchResults {
    String jsonData;
    String artistId;
    Bitmap bitmap;

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
            if(id==null||id.equals("")) {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String artistId= null;
            if(id!=null)
            artistId = id.optString("id");
            if(artistId==null||artistId.equals("")) {
                return null;
            }

        Log.i("ID","ID:::"+artistId);
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public void setBitmap(Bitmap b){
        bitmap=b;

    }
    public Bitmap getBitmap(){
        return bitmap;
    }

    public String getArtistProfile(String artistProfileJSON) {
        JSONObject profileJSON=null;
        try {
             profileJSON=new JSONObject(artistProfileJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String artistProfile=null;
        try {
             artistProfile=profileJSON.getString("profile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return artistProfile;
    }

    public class ImageLinkDownloadTask extends AsyncTask<URL,Void,String[]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AlbumActivity.loadingMessage.setText("Searching for albums images....");

        }

        @Override
        protected String[] doInBackground(URL... urls) {
            String[] imgUrl=new String[5];
            URL url=urls[0];
            String src=null;
            try {
                String htmlText=Utility.getResponseFromHttpUrl(url);
                org.jsoup.nodes.Document doc= Jsoup.parse(htmlText);
                Elements image=doc.select("img");
                int j=0;
                for(Element i:image) {
                    imgUrl[j] = i.attr("src");
                    j++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return imgUrl;
        }

    }
   /*


    }*/

    public ArrayList<AlbumInfo> getAlbumInfo(String releasesJSONText, String artistId) throws MalformedURLException {
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
                albumInfo.setArtistID(artistId);
                albumInfo.setAblumTitle(object.getString("title"));
                albumInfo.setArtistName(object.getString("artist"));

                String abc=object.getString("thumb");
                final String[] imgUrl=new String[5];
                Log.i("test:","asdASD"+artistId);
              //albumInfo.setThumnail(Integer.parseInt(object.getString("thumb")));
                ImageLinkDownloadTask task1=new ImageLinkDownloadTask();
                String relaseID=object.getString("id");
                if(object.getString("id")!=null&&object.getString("id")!="") {
                    String[] url = new String[5];
                    try {
                        url = task1.execute(new URL("https://www.discogs.com/release/" + relaseID)).get();
                        AlbumActivity.loadingMessage.setText("Loaded "+current+" album images...");

                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                   /* */
                    if(url!=null) {
                        albumInfo.setThumnailURL(url[1]);
                    }
                    else{
                        albumInfo.setThumnailURL(null);
                    }


                }
                albumInfo.setReleaseYear(object.optString("year"));



                albums.add(albumInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            current++;
        }
        AlbumActivity.loadingMessage.setText("Image load completed..");

        return albums;
    }
}
