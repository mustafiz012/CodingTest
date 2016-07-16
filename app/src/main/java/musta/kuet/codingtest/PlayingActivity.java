package musta.kuet.codingtest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class PlayingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = null;
    String[] songItems;
    String testItems;
    ListView songList;
    File internal, external, externalStorageRoot;
    File[] externalFiles;
    int counter =0;
    Button playingSong;
    EditText search;
    ArrayAdapter<String> adapter;
    NowPlaying status = new NowPlaying();
    LinearLayout layoutProgressBar;
    Random randomPosition = new Random();
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ArrayList<File> root = new ArrayList<File>();
        playingSong = (Button) findViewById(R.id.playingSong);
        //final TextView songsSize = (TextView) findViewById(R.id.songsSize);
        search = (EditText) findViewById(R.id.search_bar);
        layoutProgressBar = (LinearLayout) findViewById(R.id.progressbar_view);
        //new Task().execute();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                PlayingActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        playingSong.setOnClickListener(this);
        songList = (ListView) findViewById(R.id.lvSongList);

        final String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) ) {  // we can read the External Storage...
            //Retrieve the primary External Storage:
            final File primaryExternalStorage = Environment.getExternalStorageDirectory();

            //Retrieve the External Storages root directory:
            final String externalStorageRootDir;
            if ( (externalStorageRootDir = primaryExternalStorage.getParent()) == null ) {  // no parent...
                Log.d(TAG, "External Storage: " + primaryExternalStorage + "\n");
            }
            else {
                externalStorageRoot = new File( externalStorageRootDir );
                externalFiles = externalStorageRoot.listFiles();
                for ( final File file : externalFiles ) {
                    if ( file.isDirectory() && file.canRead() && (file.listFiles().length > 0) ) {  // it is a real directory (not a USB drive)...
                        Log.d(TAG, "External Storage: " + file.getAbsolutePath() + "\n");
                        //customToast("" + file.getAbsolutePath());
                        counter++;
                        if (counter == 1){
                            testItems = file.getAbsolutePath();
                            //customToast(""+testItems);
                        }
                    }
                }
            }
        }

        //select music root storage (if extSdCard present, two roots will be returned)
        for (int j=1; j <= 2; j++){
            //internal storage directory
            internal = Environment.getExternalStorageDirectory();
            //external storage directory
            external = new File(testItems);
            //collecting audio from internal storage
            final ArrayList<File> songs = updateSongList(internal);

            if (testItems != null){
                Log.i("Bluestacks ","Working");
                //collecting audio songs from external storage
                final ArrayList<File> song2 = updateSongList(external);
                //all songs getting together
                songs.addAll(song2);
            }

            //songsSize.setText(songs.size()+" songs");
            songItems = new String[songs.size()];
            for (int i = 0; i < songs.size(); i++){
                //getting together all songs into a String array
                songItems[i] = songs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
            }
            //listing out the songList in Playing activity
            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.songListText, songItems);
            songList.setAdapter(adapter);

            //shuffling playlist from list directly
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    try {
                        position = randomPosition.nextInt((songs.size() - 0) + 0);
                        startActivity(new Intent(getApplicationContext(), NowPlaying.class).putExtra("pos", position).putExtra("songs", songs));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            new Task().execute();
            //playing specific song by clicking on the the song item
            songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(getApplicationContext(), NowPlaying.class).putExtra("pos", position).putExtra("songs", songs));
                }
            });
            //trying to check out the option of the song item by pressing and holding on each
            songList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    customToast("something need to add");

                    return false;
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("playing", "onResumed");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("playing", "onRestart");
    }

    public void customToast(String text){
        Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
    }
    //reading all music files from sdCard using ArrayList<>
    public ArrayList<File> updateSongList(File root){
        ArrayList<File> arrayList = new ArrayList<File>();
        File[] files = root.listFiles();  //all files from root directory //file array
        for (File singleFile : files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(updateSongList(singleFile));
            }else{
                //picking up only .mp3 and .wav format files
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Playing ", "stop called");
    }

    @Override
    public void onClick(View v) {
        if (v == playingSong){
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(PlayingActivity.this, NowPlaying.class);
            //checking if the NowPlaying activity is active or not
            if (status.active){
                Log.i("NowPlaying ", "active is true");
                startActivity(intent);
            }else {
                try {
                    Toast.makeText(PlayingActivity.this, "Nothing is playing....", Toast.LENGTH_LONG).show();

                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
        }
    }

    class Task extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Thread.sleep(2000);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            layoutProgressBar.setVisibility(View.VISIBLE);
            songList.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            layoutProgressBar.setVisibility(View.GONE);
            songList.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            super.onPostExecute(aBoolean);
        }

    }

}
