package com.example.kevinwang.showerfy;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by josep on 9/17/2016.
 */
public class RecordsActivity extends ListActivity {

    private ArrayList<IndividualRecord> histoEntries = new ArrayList<IndividualRecord>(0);
    public static String recordString = "9/12,,My Gospel,,100000,,20;;9/13,,Cheap Thrills,,200000,,10;;9/14,,Take Me To Church,,60000,,0";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        String[] buffer = recordString.split(";;");
        for (int i = 0; i < buffer.length; i++) {

            String[] buffy = buffer[i].split(",,");
            Log.d("buffy", buffy + "");
            histoEntries.add(new IndividualRecord(buffy[0], buffy[1], buffy[2], buffy[3]));
        }

        RecordAdapter listadapter = new RecordAdapter(this, R.layout.record_indiv, histoEntries);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(listadapter);
    }
}
