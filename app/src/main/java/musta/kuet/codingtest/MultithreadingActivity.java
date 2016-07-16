package musta.kuet.codingtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MultithreadingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText urlField;
    Button downloadImage, goToAbout;
    ProgressBar downloadProgress;
    ListView images;
    LinearLayout loadingSection = null;
    String[] listOfImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multithreading);
        initializer();
    }


    public void initializer(){
        urlField = (EditText) findViewById(R.id.downloadUrl);
        downloadImage = (Button) findViewById(R.id.downloadImage);
        downloadProgress = (ProgressBar) findViewById(R.id.downloadProgress);
        images = (ListView) findViewById(R.id.urlList);
        images.setOnItemClickListener(this);
        listOfImages = getResources().getStringArray(R.array.imageUrls);
        goToAbout = (Button) findViewById(R.id.goToAbout);
        goToAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                intent.putExtra(AboutActivity.REQUESTED_PAGE_KEY, AboutActivity.ABOUT);
                startActivity(intent);
            }
        });
    }

    public void downloadImage(View view){
        downloadingImagesUsingThreads(listOfImages[0]);
    }

    public boolean downloadingImagesUsingThreads(String url){
        boolean successful = false;
        URL downloadUrl = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            downloadUrl = new URL(url);
            connection = ((HttpURLConnection) downloadUrl.openConnection());
            inputStream = connection.getInputStream();
            int read = -1;
            while ((read = inputStream.read()) != -1){
                Log.i("Musta", ""+read);
            }
        } catch (MalformedURLException e) { //this'll handle the colon or semi-colon after http:/;

        }catch (IOException e){

        }finally {
            if (connection != null)
                connection.disconnect();
            if (inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            }
        }
        return successful;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        urlField.setText(listOfImages[position]);
    }
}
