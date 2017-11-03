package com.music.crishna.searchartistalbum;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.music.crishna.searchartistalbum.DataModel.AlbumInfo;
import com.music.crishna.searchartistalbum.NetworkUtils.ParseSearchResults;
import com.music.crishna.searchartistalbum.NetworkUtils.Utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {
    private SearchView searchView;
    LoadSearchResults loadAlbumDataCloud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        loadAlbumDataCloud=new LoadSearchResults();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.search_action,menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(AlbumActivity.this,"Searched",Toast.LENGTH_LONG).show();

                searchItem.collapseActionView();
                URL url=Utility.buildUrl(query);
                loadAlbumDataCloud.execute(url);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public class LoadSearchResults extends AsyncTask<URL, Void,String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl=urls[0];
            String result=null;

            try {
                result = Utility.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            ParseSearchResults parseSearchResults=new ParseSearchResults();
            parseSearchResults.setJsonData(json);
            String aid=parseSearchResults.getArtistId();
           // http://api.discogs.com/artists/3317315/releases
            Uri releasesUri= Uri.parse("https://api.discogs.com").buildUpon()
                             .appendPath("artists")
                             .appendPath(aid)
                             .appendPath("releases")
                             .build();
            URL releaseURL=null;
            try {
                releaseURL = new URL(releasesUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
           // Log.i("T","URI"+releasesUri.toString());
            LoadAlbumReleases loadAlbumReleases=new LoadAlbumReleases();
            loadAlbumReleases.execute(releaseURL);
        }
    }

    public class LoadAlbumReleases extends AsyncTask<URL,Void,String>{
        @Override
        protected String doInBackground(URL... urls) {
            URL url=urls[0];
            Log.i("http","HTTP::"+url);

            String releasesJSONText=null;
            try {
                 releasesJSONText= Utility.getResponseFromHttpUrl(url);
            } catch (IOException e) {
            }

           // Log.i("AlBUM",releasesJSONText);
            return releasesJSONText;
        }

        @Override
        protected void onPostExecute(String releasesJSONText) {
            super.onPostExecute(releasesJSONText);
           // Log.i("aasd","asdas"+releasesJSONText);
            ParseSearchResults parseSearchResults=new ParseSearchResults();
            ArrayList<AlbumInfo> albumInfos=(ArrayList)parseSearchResults.getAlbumInfo(releasesJSONText);
            Iterator<AlbumInfo> albumInfoIterator=albumInfos.iterator();
            while (albumInfoIterator.hasNext()){
                AlbumInfo a=albumInfoIterator.next();
                String year=a.getReleaseYear();
                String title=a.getAblumTitle();
                Log.i("Test","Releases Year:"+year);
                Log.i("Test","Album name Year:"+title);
            }
        }
    }
}
