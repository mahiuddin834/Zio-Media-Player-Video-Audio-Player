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

import com.itnation.zioplayer.Adapter.VideoFoldersAdapter;
import com.itnation.zioplayer.DataModel.MediaFiles;
import com.itnation.zioplayer.R;

import java.util.ArrayList;

public class VideoFragment extends Fragment {

    RecyclerView videoFolderRecycler;
    ArrayList<MediaFiles> mediaFilesArrayList = new ArrayList<>();
    ArrayList<String> foldersArrayList = new ArrayList<>();
    VideoFoldersAdapter videoFoldersAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        videoFolderRecycler = view.findViewById(R.id.videoFolderRecycler);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        showFolders();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showFolders();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void showFolders() {
        mediaFilesArrayList = fetchMedia();
        videoFolderRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        videoFoldersAdapter = new VideoFoldersAdapter(mediaFilesArrayList, foldersArrayList, getContext());
        videoFolderRecycler.setAdapter(videoFoldersAdapter);
    }

    private ArrayList<MediaFiles> fetchMedia() {
        ArrayList<MediaFiles> mediaFilesArrayList = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                @SuppressLint("Range") String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));

                MediaFiles mediaFiles = new MediaFiles(id, title, displayName, size, duration, path, dateAdded);

                int index = path.lastIndexOf("/");
                String subString = path.substring(0, index);

                if (!foldersArrayList.contains(subString)) {
                    foldersArrayList.add(subString);
                }
                mediaFilesArrayList.add(mediaFiles);
            }
            cursor.close();
        }

        return mediaFilesArrayList;
    }
}
