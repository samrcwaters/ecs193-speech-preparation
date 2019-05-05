package com.google.cloud.android.speech;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import android.content.SharedPreferences;


public class NewSpeech extends AppCompatActivity {
    String SPEECH_SCRIPT_PATH;
    File[] filePathNames;
    private Toolbar mTopToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_speech);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setTitle("New speech");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24px);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get extras for editing a script (if they exist)
        Intent intent = getIntent();
        String scriptName = intent.getStringExtra("filename");
        String scriptText = intent.getStringExtra("scriptText");
        if (scriptName != null) {
            this.setTitle("Edit: " + scriptName);
            // Set the text in our speech name edit text to be our speechName
            EditText speechName = (EditText)findViewById(R.id.speechName);
            speechName.setText(scriptName);
        }
        else {
            this.setTitle("Enter a Speech");
        }
        if (scriptText != null) {
            EditText speechText = (EditText)findViewById(R.id.editText);
            speechText.setText(scriptText);
        }

    }


    public void goToMainMenu(View view){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void goToSpeechView(View view){
        Intent intent = new Intent(this, SpeechView.class);
        startActivity(intent);
    }

    public void saveFile(View view) throws Exception {
        /* Get speech text from editText */
        EditText editText = (EditText)findViewById(R.id.editText);
        String speechText = editText.getText().toString();

        /* Get speech name from speechText editText */
        EditText speechNameET = (EditText)findViewById(R.id.speechName);
        String speechName = speechNameET.getText().toString();

        String filePath;

        SPEECH_SCRIPT_PATH = getFilesDir() + File.separator + speechName;

        // Check if speech script directory exists
        File f = new File(SPEECH_SCRIPT_PATH);
        if (f.exists()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "A speech with this name already exists.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
//        else if (!f.exists() || !f.isDirectory()) {
        else if (!f.exists()) {
            f = new File(SPEECH_SCRIPT_PATH, "speech-script");
            f.mkdirs();

            try {
                /* Write speech text to file */
                filePath = FileService.writeToFile(speechName, speechText,
                        SPEECH_SCRIPT_PATH + File.separator + "speech-script");
                Log.d("NEWSPEECH", filePath);
                // Show notification on successful save
                Toast toast = Toast.makeText(getApplicationContext(),
                        "File saved!", Toast.LENGTH_SHORT);
                toast.show();

                // Send back to this speech's menu
                Intent intent = new Intent(this, SpeechView.class);
                intent.putExtra("speechName", speechName);

                //CREATE the shared preference file and add necessary values
                SharedPreferences sharedPref = getSharedPreferences(speechName, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("filepath", filePath);
                editor.putInt("currRun", 0);
                editor.commit();

                startActivity(intent);
            } catch (Exception e) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        e.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void printAllFilesInDir() {
        File dir = new File(SPEECH_SCRIPT_PATH);
        // Get all files saved to speech scripts
        File[] files = dir.listFiles();
        for (File file : files) {
            System.out.println(file);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base_menu, menu);
        return true;
    }
}