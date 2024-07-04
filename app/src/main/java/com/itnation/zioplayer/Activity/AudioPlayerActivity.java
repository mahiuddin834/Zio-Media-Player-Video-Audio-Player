package com.itnation.zioplayer.Activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.itnation.zioplayer.Adapter.MusicPlayListAdapter;
import com.itnation.zioplayer.DataModel.MusicFile;
import com.itnation.zioplayer.R;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class AudioPlayerActivity extends AppCompatActivity {

    private ExoPlayer exoPlayer;
    private ArrayList<MusicFile> musicList;
    private int currentPosition;
    private boolean isRepeating = false;

    ImageView backBtn, repeatBtn, previewsBtn, playPauseBtn, nextBtn, playListBtn;
    TextView songName, artistName, currentTime, durationTime;
    CircleImageView albumArt;
    SeekBar seekBar;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;
    RotateAnimation rotateAnimation;

    private ImageView noti_play_pause, nextButton, prevButton;
    private boolean isBound = false;
    private String songTitle = "Your Song Title";
    private String songArtist = "Your Artist Name";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        // Initialize UI elements
        backBtn = findViewById(R.id.backBtn);
        repeatBtn = findViewById(R.id.repeatBtn);
        previewsBtn = findViewById(R.id.previewsBtn);
        playPauseBtn = findViewById(R.id.playPauseBtn);
        nextBtn = findViewById(R.id.nextBtn);
        playListBtn = findViewById(R.id.playListBtn);
        songName = findViewById(R.id.songName);
        artistName = findViewById(R.id.artistName);
        currentTime = findViewById(R.id.currentTime);
        durationTime = findViewById(R.id.durationTime);
        seekBar = findViewById(R.id.seekBar);
        albumArt = findViewById(R.id.albumArt);

        // Initialize notification controls
        noti_play_pause = findViewById(R.id.noti_play_pause);
        nextButton = findViewById(R.id.noti_next);
        prevButton = findViewById(R.id.noti_prev);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        // Get the music list and position from the intent
        musicList = getIntent().getParcelableArrayListExtra("musicList");
        currentPosition = getIntent().getIntExtra("position", 0);

        if (musicList == null || musicList.isEmpty()) {
            Toast.makeText(this, "No audio files available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializePlayer();
        playAudio(currentPosition);

        playPauseBtn.setImageResource(R.drawable.ic_pause_circle_outline_24);
        playPauseBtn.setOnClickListener(v -> {
            if (exoPlayer.isPlaying()) {
                exoPlayer.pause();
                albumArt.clearAnimation();  // Stop rotation animation
                playPauseBtn.setImageResource(R.drawable.ic_play_circle_outline_24);
            } else {
                exoPlayer.play();
                albumArt.startAnimation(loadRotation());  // Start rotation animation
                playPauseBtn.setImageResource(R.drawable.ic_pause_circle_outline_24);
            }
        });

        nextBtn.setOnClickListener(v -> playNextAudio());
        previewsBtn.setOnClickListener(v -> playPreviousAudio());

        repeatBtn.setOnClickListener(v -> {
            isRepeating = !isRepeating;
            exoPlayer.setRepeatMode(isRepeating ? ExoPlayer.REPEAT_MODE_ONE : ExoPlayer.REPEAT_MODE_OFF);
            repeatBtn.setColorFilter(isRepeating ? getResources().getColor(R.color.my_light_primary) : getResources().getColor(android.R.color.white));
        });

        playListBtn.setOnClickListener(v -> openBottomSheet());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    exoPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null && exoPlayer.isPlaying()) {
                    seekBar.setProgress((int) exoPlayer.getCurrentPosition());
                    currentTime.setText(formatTime(exoPlayer.getCurrentPosition()));
                    durationTime.setText(formatTime(exoPlayer.getDuration()));
                    handler.postDelayed(this, 1000);
                }
            }
        };

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == ExoPlayer.STATE_READY) {
                    seekBar.setMax((int) exoPlayer.getDuration());
                    updateSeekBar.run();
                }
            }
        });



    }

    private void initializePlayer() {
        exoPlayer = new ExoPlayer.Builder(this).build();
    }

    private void playAudio(int position) {
        MusicFile musicFile = musicList.get(position);
        Uri uri = Uri.parse(musicFile.getPath());

        String path = musicFile.getPath();

        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(uri)
                .setMimeType("audio/*")
                .build();


        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
        albumArt.startAnimation(loadRotation());  // Start rotation animation

        updateUI(musicFile);
    }

    private Animation loadRotation() {
        rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        return rotateAnimation;
    }

    private void updateUI(MusicFile musicFile) {
        songName.setText(musicFile.getTitle());
        artistName.setText(musicFile.getArtistName());
    }

    private void playNextAudio() {
        if (currentPosition < musicList.size() - 1) {
            currentPosition++;
        } else {
            currentPosition = 0;
        }
        playAudio(currentPosition);
    }

    private void playPreviousAudio() {
        if (currentPosition > 0) {
            currentPosition--;
        } else {
            currentPosition = musicList.size() - 1;
        }
        playAudio(currentPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateSeekBar);
        exoPlayer.release();
        exoPlayer = null;


    }

    private String formatTime(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1));
    }

    private void openBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.playlist_layout_bottom_sheet);

        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.recyclerView);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            MusicPlayListAdapter adapter = new MusicPlayListAdapter(this, musicList, this::playAudio);
            recyclerView.setAdapter(adapter);
        }

        bottomSheetDialog.show();
    }

}
