package com.example.kevinwang.showerfy;

/**
 * Created by albert on 9/17/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;

public class PenaltyActivity extends Activity{
    private Random rand;
    private Button returnButton;
    TextView song, overtime, message;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rand = new Random();
        int screenChoice = rand.nextInt()*2+1;
        if(screenChoice==1){
            setContentView(R.layout.activity_penalty);
        }
        else if(screenChoice==2){
            setContentView(R.layout.activity_penalty2);
        }
        else{
            setContentView(R.layout.activity_penalty3);
        }


        song = (TextView) findViewById(R.id.textView2);
        overtime = (TextView) findViewById(R.id.textView);
        message = (TextView) findViewById(R.id.textView3);

        song.setText("");




        addButtonListener();

    }



    private void addButtonListener() {
        returnButton = (Button) findViewById(R.id.button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                handleClick();
            }
        });
    }

    private void handleClick() {
        Intent returnIntent = new Intent(this,MainActivity.class);
    }

}
