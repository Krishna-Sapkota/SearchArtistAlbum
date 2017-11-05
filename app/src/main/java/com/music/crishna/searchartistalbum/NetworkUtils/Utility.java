package com.music.crishna.searchartistalbum.NetworkUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Krishna on 11/3/17.
 */

public class Utility {
    static final String DICSOGS_BASE_URL="https://api.discogs.com/database/search";

    final static String PARAM_TYPE="type";
    final static String PARAM_QUERY="q";

    final static String type="artist";
    final static String token="OsBGZdPTjChFxaNwmbSctPEaqTZlStqvAKeeJJNl";


    public static URL buildUrl(String artistName) {
        Uri builtUri = Uri.parse(DICSOGS_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_TYPE, type)
                .appendQueryParameter(PARAM_QUERY, artistName)
                .appendQueryParameter("token",token)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    public static Bitmap getBitmapFromURL(String src) {
        HttpURLConnection connection=null;
        try {
            URL url = new URL(src);
             connection= (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
        finally {
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
       // urlConnection.setRequestMethod("GET");
        //urlConnection.setRequestProperty("Authorization","token"+token);
        InputStream in=null;
        try {
             in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();

            } else {
                return null;
            }

        }

        finally {
            urlConnection.disconnect();
        }
    }


}
