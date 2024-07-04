package com.itnation.zioplayer.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itnation.zioplayer.Adapter.MusicFilesAdapter;
import com.itnation.zioplayer.DataModel.MusicFile;
import com.itnation.zioplayer.R;

import java.util.ArrayList;

public class MusicFileActivity extends AppCompatActivity {

    RecyclerView musicFilesRecyclerView;
    ArrayList<MusicFile> musicFilesArrayList = new ArrayList<>();
    MusicFilesAdapter musicFilesAdapter;
    String folder_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_file);

        // Request permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            // Initialize components
            initializeComponents();
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            // Initialize components
            initializeComponents();
        } else {
            // Permission denied, handle as appropriate
            Log.e("MusicFileActivity", "Permission denied to read external storage");
        }
    });

    private void initializeComponents() {
        musicFilesRecyclerView = findViewById(R.id.musicFileRV);

        folder_name = getIntent().getStringExtra("folderName");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(folder_name);
        }

        showMusicFiles();
    }

    private void showMusicFiles() {
        musicFilesArrayList = fetchMusic(folder_name);
        musicFilesAdapter = new MusicFilesAdapter(musicFilesArrayList, this, 0);
        musicFilesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        musicFilesRecyclerView.setAdapter(musicFilesAdapter);
        musicFilesAdapter.notifyDataSetChanged();
    }

    private ArrayList<MusicFile> fetchMusic(String folderName) {
        ArrayList<MusicFile> musicFiles = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.DATA + " like?";
        String[] selectionArg = new String[]{"%" + folderName + "%"};
        Cursor cursor = getContentResolver().query(uri, null, selection, selectionArg, null);

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
                musicFiles.add(musicFile);
            }
            cursor.close();
        }

        return musicFiles;
    }



}
