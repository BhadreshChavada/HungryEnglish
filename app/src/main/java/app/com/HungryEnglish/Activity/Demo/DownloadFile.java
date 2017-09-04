package app.com.HungryEnglish.Activity.Demo;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.provider.DocumentFile;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import app.com.HungryEnglish.R;

/**
 * Created by Bhadresh Chavada on 02-08-2017.
 */

public class DownloadFile extends Activity {

    private EditText audioText;
    private String downloadAudioPath;
    private String urlDownloadLink = "";

    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dummy);




        downloadAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"Hungry";
        File audioVoice = new File("file:///storage/emulated/0/hungry/AUD-20170713-WA0003.mp4");


        if(!audioVoice.exists()){
            audioVoice.mkdir();
        }

        progressbar = (ProgressBar)findViewById(R.id.progress_view);
        audioText = (EditText)findViewById(R.id.audio_link);

        audioText.setText("http://smartsquad.16mb.com/HungryEnglish/api/Audio/audio_5970fe2e11acc.mp3");
        Button selectMatch = (Button)findViewById(R.id.select_match);
//        selectMatch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent selectIntent = new Intent(DownloadFile.this, SelectAudioActivity.class);
//                startActivity(selectIntent);
//            }
//        });

        Button audioLinkButton = (Button)findViewById(R.id.download_audio);

        audioLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlDownloadLink = audioText.getText().toString();
                if(urlDownloadLink.equals("")){
                    Toast.makeText(DownloadFile.this, "Please add audio download link", Toast.LENGTH_LONG).show();
                    return;
                }
                String filename = extractFilename();
                downloadAudioPath = downloadAudioPath + File.separator + "voices" + File.separator + filename;
                DownloadFile1 downloadAudioFile = new DownloadFile1();
                downloadAudioFile.execute(urlDownloadLink, downloadAudioPath);
                audioText.setText("");
            }
        });
    }


    private class DownloadFile1 extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... url) {
            int count;
            try {
                URL urls = new URL(url[0]);
                URLConnection connection = urls.openConnection();
                connection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(urls.openStream());
                OutputStream output = new FileOutputStream(url[1]);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar.setVisibility(ProgressBar.GONE);
        }
    }

    private String extractFilename(){
        if(urlDownloadLink.equals("")){
            return "";
        }
        String newFilename = "";
        if(urlDownloadLink.contains("/")){
            int dotPosition = urlDownloadLink.lastIndexOf("/");
            newFilename = urlDownloadLink.substring(dotPosition + 1, urlDownloadLink.length());
        }
        else{
            newFilename = urlDownloadLink;
        }
        return newFilename;
    }
}