package com.example.kevinwang.showerfy;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        histoEntries.add(new IndividualRecord("500000000", "My Gospel","100000", "20"));
        histoEntries.add(new IndividualRecord("1010000000", "Cheap Thrills","200000", "10"));
        histoEntries.add(new IndividualRecord("2000100000", "Take Me to Church","60000", "0"));

        RecordAdapter listadapter = new RecordAdapter(this, R.layout.record_indiv, histoEntries);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(listadapter);
    }
}
