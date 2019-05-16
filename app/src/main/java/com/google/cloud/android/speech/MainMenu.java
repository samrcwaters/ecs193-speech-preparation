package com.google.cloud.android.speech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;

public class MainMenu extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    String[] fileNames;
    private Toolbar mTopToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        getIntent();
        this.setTitle("Speeches");

        // Instantiate toolbar
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);

        File dir = new File(getFilesDir() + File.separator + "speechFiles");

        //get file names
        fileNames = dir.list();

        if (fileNames != null && fileNames.length != 0) {
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);

            // Connect this adapter to a listview to be populated
            listView = (ListView) findViewById(R.id.speechNames);
            listView.setAdapter(itemsAdapter);

            listView.setOnItemClickListener(this);
        }
//        Uncomment below for startup redirection
//        else
//        {
//            Intent intent = new Intent(MainMenu.this, NewSpeech.class);
//            startActivity(intent);
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            View view = findViewById(R.id.action_add);
            goToNewSpeech(view);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToNewSpeech(View view) {
        Intent intent = new Intent(this, NewSpeech.class);
        startActivity(intent);
    }

    public void goToSpeechMenu(View view, String speechName) {
        Intent intent = new Intent(this, SpeechView.class);
        intent.putExtra("speechName", speechName);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String speechName = fileNames[position];
        goToSpeechMenu(view, speechName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
