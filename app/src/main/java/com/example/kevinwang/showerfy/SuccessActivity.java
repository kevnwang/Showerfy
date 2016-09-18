package com.example.kevinwang.showerfy;

/**
 * Created by kevinwang on 9/17/16.
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

public class SuccessActivity extends Activity{

    private Button share;
    TextView infor;
    String totalScore;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Bundle data = getIntent().getExtras();

        if(data==null){return;}
        String[] info = data.getStringArray("info");



        infor = (TextView) findViewById(R.id.infor);
        infor.setText(info[0]+"\n"+"Points Earned: "+info[1]);

        totalScore=info[2];

        addButtonListener();


    }

    private void addButtonListener() {
        share = (Button) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                handleClick();
            }
        });
    }

    private void handleClick() {

        Intent share = new Intent(this, ShareActivity.class);
        share.putExtra("stats", totalScore);
        startActivity(share);


    }


}
