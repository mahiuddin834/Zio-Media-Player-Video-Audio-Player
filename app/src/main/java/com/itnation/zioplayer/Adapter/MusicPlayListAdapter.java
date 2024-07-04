package com.itnation.zioplayer.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itnation.zioplayer.Activity.AudioPlayerActivity;
import com.itnation.zioplayer.DataModel.MusicFile;
import com.itnation.zioplayer.R;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayListAdapter extends RecyclerView.Adapter<MusicPlayListAdapter.MusicViewHolder> {

    Context context;

    private List<MusicFile> musicFiles;
    private OnItemClickListener onItemClickListener;


    public MusicPlayListAdapter(Context context, List<MusicFile> musicFiles, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.musicFiles = musicFiles;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        MusicFile musicFile = musicFiles.get(position);
        holder.titleTextView.setText(musicFile.getTitle());
        holder.artistTextView.setText(musicFile.getArtistName());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView artistTextView;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
        }
    }
}
