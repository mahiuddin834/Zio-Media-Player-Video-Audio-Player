package com.itnation.zioplayer.MainFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itnation.zioplayer.Activity.MusicFileActivity;
import com.itnation.zioplayer.Adapter.MusicFolderAdapter;
import com.itnation.zioplayer.Adapter.VideoFoldersAdapter;
import com.itnation.zioplayer.DataModel.MediaFiles;
import com.itnation.zioplayer.DataModel.MusicFile;
import com.itnation.zioplayer.R;

import java.util.ArrayList;


public class MusicFragment extends Fragment {

    RecyclerView musicFolderRecycler;

    ArrayList<MusicFile> musicFolderArrayList = new ArrayList<>();
    ArrayList<String> foldersArrayList = new ArrayList<>();
    MusicFolderAdapter musicFolderAdapter;
    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        musicFolderRecycler=view.findViewById(R.id.musicFolderRecycler);



        showFolders();




        return view;
    }//-------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    private void showFolders() {
        musicFolderArrayList = fetchMedia();
        musicFolderRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        musicFolderAdapter = new MusicFolderAdapter(musicFolderArrayList, foldersArrayList, getContext());
        musicFolderRecycler.setAdapter(musicFolderAdapter);
    }


    private ArrayList<MusicFile> fetchMedia() {
        ArrayList<MusicFile> musicFilesArrayList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                @SuppressLint("Range") String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                @SuppressLint("Range") String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                @SuppressLint("Range") String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));



                MusicFile musicFile = new MusicFile(id, title, displayName, size, duration, path, dateAdded, artistName);

                int index = path.lastIndexOf("/");
                String subString = path.substring(0, index);

                if (!foldersArrayList.contains(subString)) {
                    foldersArrayList.add(subString);
                }
                musicFilesArrayList.add(musicFile);
            }
            cursor.close();
        }

        return musicFilesArrayList;
    }




}