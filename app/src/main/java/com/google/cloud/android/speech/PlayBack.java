package com.google.cloud.android.speech;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

public class PlayBack extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_back);
        VideoView videoView =(VideoView)findViewById(R.id.vdVw);
        //Set MediaController  to enable play, pause, forward, etc options.
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        //Location of Media File
        Uri videoUri = Uri.parse(getVideoFilePath(getApplicationContext()));
        //Starting VideView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.start();
    }
    private String getVideoFilePath(Context context) {
        final File dir = context.getExternalFilesDir(null);
        Intent intent = getIntent();
        String speechName = intent.getStringExtra("speechName");
        return (dir == null ? "" : (dir.getAbsolutePath() + "/"))
                +  speechName + ".mp4";
    }
}