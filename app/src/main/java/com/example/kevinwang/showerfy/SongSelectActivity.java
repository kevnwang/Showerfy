package com.example.kevinwang.showerfy;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kevinwang on 9/17/16.
 */
public class SongSelectActivity extends Activity {
    private GridView SongGridView;
    private GridViewAdapter gridAdapter;
    private final String[] songTitles = new String[]{
            "Rick Roll",
            "No Way No",
            "Sandstorm",
            "My Gospel",
            "Closer",
            "Shots-Broiler Remix",
            "Don't Let Me Down"

    };
    private final String[] songUris = new String[]{
            "spotify:track:7GhIk7Il098yCjg4BQjzvb",
            "spotify:track:5xHooj89TQoQHj3dALvSh1",
            "spotify:track:3XWZ7PNB3ei50bTPzHhqA6",
            "spotify:track:1RdykyqZss4snJH9e58CQJ",
            "spotify:track:7BKLCZ1jbUBVqRi2FVlTVw",
            "spotify:track:3iQHc5Mk6yN1ntBjJYD89H",
            "spotify:track:1i1fxkWeaMmKEB4T7zqbzK"
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songselect);

        SongGridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        SongGridView.setAdapter(gridAdapter);

        SongGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int itemPosition = position;

                Intent returnIntent = new Intent();
                String[] song = {songUris[itemPosition], songTitles[itemPosition]};
                returnIntent.putExtra("song", song);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, songTitles[i]));
        }
        return imageItems;
    }




    }




