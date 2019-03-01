package com.google.cloud.android.speech;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class NewSpeech extends AppCompatActivity {
    String SPEECH_SCRIPT_PATH;
    File[] filePathNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_speech);

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
        SPEECH_SCRIPT_PATH = getFilesDir() + File.separator + "speech-scripts";

        // Check if speech script directory exists
        File f = new File(SPEECH_SCRIPT_PATH);
        if (f.exists() && f.isDirectory()) {
            System.out.println("we already in there fam");
        }
        else { // If not, create it
            File folder = getFilesDir();
            f = new File(folder, "speech-scripts");
            f.mkdir();
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

    public void saveFile(View view) {
        /* Get speech text from editText */
        EditText editText = (EditText)findViewById(R.id.editText);
        String speechText = editText.getText().toString();

        /* Get speech name from speechText editText */
        EditText speechNameET = (EditText)findViewById(R.id.speechName);
        String speechName = speechNameET.getText().toString();

        String filePath;

        /* Write speech text to file */
        filePath = writeToFile(speechName, speechText).toString();


        // Send back to this speech's menu
         Intent intent = new Intent(this, SpeechView.class);
            intent.putExtra("filePath", filePath);
            intent.putExtra("speechName", speechName);
         startActivity(intent);
    }

    private File writeToFile(String speechName, String speechText) {
        //Create a new file in our speech scripts dir with given filename
        File file = new File(SPEECH_SCRIPT_PATH, speechName);

        //This point and below is responsible for the write operation
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists -- for now we're creating a new file
            outputStream = new FileOutputStream(file, false);

            outputStream.write(speechText.getBytes());
            outputStream.flush();
            outputStream.close();

            // Show notification on successful save
            Toast toast = Toast.makeText(getApplicationContext(),
                    "File saved!", Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    e.toString(), Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }

        return file;
    }

    private void printAllFilesInDir() {
        File dir = new File(SPEECH_SCRIPT_PATH);
        // Get all files saved to speech scripts
        File[] files = dir.listFiles();
        for (File file : files) {
            System.out.println(file);
        }
    }
}