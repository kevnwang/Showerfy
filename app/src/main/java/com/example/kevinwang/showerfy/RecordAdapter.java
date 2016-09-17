package com.example.kevinwang.showerfy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by josep on 9/17/2016.
 */
public class RecordAdapter extends ArrayAdapter<IndividualRecord> {
    private ArrayList<IndividualRecord> records;
    private Context context;

    public RecordAdapter(Context c, int textViewResId, ArrayList<IndividualRecord> r){
        super(c, textViewResId, r);
        this.records = r;
        context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.record_indiv, null);
        }
        IndividualRecord rec = records.get(position);
        if (rec != null) {
            TextView date = (TextView) v.findViewById(R.id.date);
            TextView song = (TextView) v.findViewById(R.id.song);
            TextView points = (TextView) v.findViewById(R.id.points);
            TextView timeStr = (TextView) v.findViewById(R.id.timeText);
            ProgressBar duration = (ProgressBar) v.findViewById(R.id.progressBar);

            date.setText(rec.date);
            song.setText(rec.songTitle);
            points.setText(rec.points);
            timeStr.setText(rec.timeStr);
            duration.setMax(20*60*1000);
            duration.setProgress(Integer.parseInt(rec.timeNum));
        }
        return v;
    }
}
