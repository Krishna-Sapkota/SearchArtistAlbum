package com.music.crishna.searchartistalbum.DataModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.crishna.searchartistalbum.NetworkUtils.Utility;
import com.music.crishna.searchartistalbum.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Krishna on 11/4/17.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumContentViewHolder> {
    ArrayList<AlbumInfo> albumInfos;
    Bitmap []albumImages;
    public static int noOfImages=0;



    public AlbumAdapter(ArrayList<AlbumInfo> list){
        albumInfos=list;
        albumImages=new Bitmap[list.size()];

    }

    @Override
    public AlbumContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context c=parent.getContext();
        int layoutForAlbum=R.layout.album_card;
        LayoutInflater layoutInflater=LayoutInflater.from(c);
        boolean shouldAttachToParentImmediately = false;

        View albumView=layoutInflater.inflate(layoutForAlbum,parent,shouldAttachToParentImmediately);

        return new AlbumContentViewHolder(albumView);


    }

    @Override
    public void onBindViewHolder(AlbumContentViewHolder holder, int position) {
        String artistName=albumInfos.get(position).getArtistName();
        String albumTitle=albumInfos.get(position).getAblumTitle();
        String releaseYear=albumInfos.get(position).getReleaseYear();
        holder.artisName.setText(artistName);
        BitmapDownloadTask task2 = new BitmapDownloadTask();
        Bitmap bitmap = null;
        String url=albumInfos.get(position).getThumbnailURL();

        try {
            try {
                bitmap=task2.execute(url).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        holder.albumArt.setImageBitmap(bitmap);
        if(bitmap!=null) {
            final double viewWidthToBitmapWidthRatio = (double) holder.albumArt.getWidth() / (double) holder.albumArt.getWidth();
            holder.albumArt.getLayoutParams().height = (int) (bitmap.getHeight() * viewWidthToBitmapWidthRatio);
        }

        holder.albumTitle.setText(albumTitle);
        holder.releaseYear.setText(releaseYear);
    }

    public class AlbumContentViewHolder extends RecyclerView.ViewHolder{
        ImageView albumArt;
        TextView artisName;
        TextView albumTitle;
        TextView releaseYear;

        public AlbumContentViewHolder(View itemView) {
            super(itemView);
            albumArt=(ImageView)itemView.findViewById(R.id.album_art);
            artisName=(TextView)itemView.findViewById(R.id.artist_name);
            albumTitle=(TextView)itemView.findViewById(R.id.album_title);
            releaseYear=(TextView)itemView.findViewById(R.id.released_year);
        }
    }

    @Override
    public int getItemCount() {
        return albumInfos.size();
    }
    public  class BitmapDownloadTask extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap b = Utility.getBitmapFromURL(url);
            return b;
        }
    }


}
