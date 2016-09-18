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

import java.text.DateFormat;
import java.util.*;

public class PenaltyActivity extends Activity {

    private Button returnButton;
    TextView song, overtime, message;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
        if (data == null) {
            return;
        }
        String[] info = data.getStringArray("info");
        int screenChoice = 0;
        boolean passTest = false;
        setContentView(R.layout.activity_penalty);
        while (!passTest) {

            screenChoice = (int) (Math.random() * 3) + 1;
            if (screenChoice == 1) {
                passTest = true;
            } else if (screenChoice == 2) {
                passTest = true;
            } else {
                if (Double.valueOf(info[1]) >= 180000.0) {
                    passTest = true;
                }
            }
        }


        song = (TextView) findViewById(R.id.textView2);
        overtime = (TextView) findViewById(R.id.textView);
        message = (TextView) findViewById(R.id.textView3);

        song.setText(info[0]);

        int seconds = 0;
        int minutes = 0;
        double overtimee = Double.valueOf(info[1]);
        overtimee /= 1000.0;
        if (overtimee >= 60) {
            minutes = (int) (overtimee / 60.0);
        }
        seconds = (int) (overtimee % 60.0);

        overtime.setText("OVERTIME: " + String.valueOf(minutes) + " mins, " + String.valueOf(seconds) + " secs");
        double num = 0;
        String messageStr = "";

        if (screenChoice == 1) {
            num = overtimee / 60.0 * 2.1 / 2.1 * 13;
            messageStr = "The amount of extra water used is the same as X water bottles!";
        } else if (screenChoice == 2) {
            num = overtimee / 60.0 * 2.1 / 1.125;
            messageStr = "You could have cooked X pots of mom's spaghetti with that water :(";
        } else {
            num = overtimee / 60.0 * 2.1 / 7;
            messageStr = "You essentially dehydrated X gorillas to death...";
        }
        if (num % 1 >= 4) {
            num = (int) (num + .5);
        }

        for (int i = 0; i < messageStr.length(); i++) {
            if (messageStr.charAt(i) == 'X') {
                String newMessage = messageStr.substring(0, i - 1) + " " + (int) num + " " + messageStr.substring(i + 2, messageStr.length());
                message.setText(newMessage);
                break;
            }
        }
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
        Intent returnIntent = new Intent(this, MainActivity.class);
        startActivity(returnIntent);
        finish();
    }

}
