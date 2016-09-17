package com.example.kevinwang.showerfy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kevinwang on 9/17/16.
 */
public class SongSelectActivity extends Activity {
    private ListView songSelectList;
    private ArrayAdapter<String> adapter;
    private final String[] songTitles = new String[]{
            "Rick Roll",
            "No Way No",
            "Sandstorm",
            "My Gospel",
            "Closer"
    };
    private final String[] songUris = new String[]{
            "spotify:track:7GhIk7Il098yCjg4BQjzvb",
            "spotify:track:5xHooj89TQoQHj3dALvSh1",
            "spotify:track:3XWZ7PNB3ei50bTPzHhqA6",
            "spotify:track:1RdykyqZss4snJH9e58CQJ",
            "spotify:track:7BKLCZ1jbUBVqRi2FVlTVw"
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_songselect);

        songSelectList = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songTitles);

        songSelectList.setAdapter(adapter);

        songSelectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String itemValue = (String) songSelectList.getItemAtPosition(position);
                Intent returnIntent = new Intent(SongSelectActivity.this,MainActivity.class);
                returnIntent.putExtra("song", songUris[itemPosition]);
                setResult(Activity.RESULT_OK, returnIntent);
                startActivity(returnIntent);
                finish();
            }
        });

    }
}
