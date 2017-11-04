package com.music.crishna.searchartistalbum.DataModel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.crishna.searchartistalbum.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krishna on 11/4/17.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumContentViewHolder> {
    ArrayList<AlbumInfo> albumInfos;


    public AlbumAdapter(ArrayList<AlbumInfo> list){
        albumInfos=list;

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
}
