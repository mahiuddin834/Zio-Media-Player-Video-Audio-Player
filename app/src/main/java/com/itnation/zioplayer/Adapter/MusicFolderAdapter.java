package com.itnation.zioplayer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itnation.zioplayer.Activity.MusicFileActivity;
import com.itnation.zioplayer.Activity.VideoFilesActivity;
import com.itnation.zioplayer.DataModel.MediaFiles;
import com.itnation.zioplayer.DataModel.MusicFile;
import com.itnation.zioplayer.R;

import java.util.ArrayList;

public class MusicFolderAdapter extends RecyclerView.Adapter<MusicFolderAdapter.ViewHolder> {


    ArrayList<MusicFile> musicFileArrayList;
    ArrayList<String> folderPath;
    Context context;

    public MusicFolderAdapter(ArrayList<MusicFile> musicFileArrayList, ArrayList<String> folderPath, Context context) {
        this.musicFileArrayList = musicFileArrayList;
        this.folderPath = folderPath;
        this.context = context;
    }

    @NonNull
    @Override
    public MusicFolderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_folders_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MusicFolderAdapter.ViewHolder holder, int position) {

        int index_path = folderPath.get(position).lastIndexOf("/");
        String folderName = folderPath.get(position).substring(index_path + 1);

        holder.folderName.setText(folderName);
        holder.folderPath.setText(folderPath.get(position));

        // Count videos in this folder
        int musicCount = 0;
        for (MusicFile musicFile : musicFileArrayList) {
            if (musicFile.getPath().startsWith(folderPath.get(position))) {
                musicCount++;
            }
        }
        holder.totalVideos.setText(musicCount + " Musics");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MusicFileActivity.class);
            intent.putExtra("folderName", folderName);
            intent.putExtra("folderPath", folderPath.get(position));
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return folderPath.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView folderName, folderPath, totalVideos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            folderName = itemView.findViewById(R.id.folderName);
            folderPath = itemView.findViewById(R.id.folderPath);
            totalVideos = itemView.findViewById(R.id.totalVideos);
        }
    }
}
