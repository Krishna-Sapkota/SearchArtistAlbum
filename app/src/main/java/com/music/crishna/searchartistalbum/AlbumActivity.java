package com.music.crishna.searchartistalbum;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.music.crishna.searchartistalbum.DataModel.AlbumAdapter;
import com.music.crishna.searchartistalbum.DataModel.AlbumInfo;
import com.music.crishna.searchartistalbum.NetworkUtils.ParseSearchResults;
import com.music.crishna.searchartistalbum.NetworkUtils.Utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AlbumActivity extends AppCompatActivity {
    private SearchView searchView;
    ParseSearchResults parseSearchResults;
    LoadSearchResults loadAlbumDataCloud;
    RecyclerView recyclerView;
    TextView artistProfileTextView;
    public static TextView loadingMessage;
    public static ProgressBar progressBar;
    AlbumAdapter albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        artistProfileTextView=(TextView)findViewById(R.id.tv_artist_loading_profile);
        loadingMessage=(TextView)findViewById(R.id.tv_loading_message);
        progressBar=(ProgressBar)findViewById(R.id.progress_indicator);

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
                Toast.makeText(AlbumActivity.this,"Searching",Toast.LENGTH_LONG).show();
                loadAlbumDataCloud=new LoadSearchResults();
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
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            loadingMessage.setText("Searching for artist...");
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            parseSearchResults=new ParseSearchResults();
            parseSearchResults.setJsonData(json);
            String aid=parseSearchResults.getArtistId();
            if(aid==null||aid.equals("")){
                loadingMessage.setText("Cannot find such artist!");
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }
            loadingMessage.setText("Artist loaded!");

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
        protected void onPreExecute() {
            super.onPreExecute();
            loadingMessage.setText("Loading album releases...");

        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url=urls[0];
            Log.i("http","HTTP::"+url);

            String releasesJSONText=null;
            try {
                 releasesJSONText= Utility.getResponseFromHttpUrl(url);
            } catch (IOException e) {
            }
            publishProgress();
           // Log.i("AlBUM",releasesJSONText);
            return releasesJSONText;
        }



        @Override
        protected void onPostExecute(String releasesJSONText) {
            super.onPostExecute(releasesJSONText);
            loadingMessage.setText("Album releases loaded...");

            String artistProfile;
            LoadArtistProfileTask task=new LoadArtistProfileTask();

            String profileURL="https://api.discogs.com/artists/"+parseSearchResults.getArtistId();
            String artistProfileJSON=null;
            try {
                artistProfileJSON=task.execute(new URL(profileURL)).get();
                loadingMessage.setText("Artist profile loaded..");

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            artistProfile=parseSearchResults.getArtistProfile(artistProfileJSON);
            artistProfileTextView.setText(artistProfile);
            ArrayList<AlbumInfo> albumInfos= null;
            try {
                albumInfos = (ArrayList)parseSearchResults.getAlbumInfo(releasesJSONText,parseSearchResults.getArtistId());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }



            RecyclerView.LayoutManager layoutManager=new GridLayoutManager(AlbumActivity.this,1);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(100);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


            albumAdapter=new AlbumAdapter(albumInfos);
            recyclerView.setAdapter(albumAdapter);
            progressBar.setVisibility(View.INVISIBLE);
            loadingMessage.setVisibility(View.INVISIBLE);

          //  Iterator<AlbumInfo> albumInfoIterator=albumInfos.iterator();
           /* while (albumInfoIterator.hasNext()){
                AlbumInfo a=albumInfoIterator.next();
                String year=a.getReleaseYear();
                String title=a.getAblumTitle();
                String artist=a.getArtistName();
                Log.i("test","Artist Name:"+artist);
                Log.i("Test","Releases Year:"+year);
                Log.i("Test","Album name Year:"+title);

            }*/

        }
    }
    public class LoadArtistProfileTask extends AsyncTask<URL,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingMessage.setText("Loading artist profile");

        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url=urls[0];
            String profileJSON=null;
            try {
                profileJSON=Utility.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return profileJSON;
        }

    }
}
