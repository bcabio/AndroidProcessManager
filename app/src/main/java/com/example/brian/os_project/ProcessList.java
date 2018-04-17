package com.example.brian.os_project;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Activity;
import android.content.Context;

import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public class ProcessList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView listview = (ListView) findViewById(R.id.processList);

        ArrayList<RunningAppProcessInfo> processes = new ArrayList<>();

        StableArrayAdapter adapter = new StableArrayAdapter(this, processes);
        listview.setAdapter(adapter);

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> n = activityManager.getRunningAppProcesses();

        Collections.sort(n, new SortByPID());

        for(RunningAppProcessInfo x: n) {
            adapter.add(x);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_process_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class StableArrayAdapter extends ArrayAdapter<RunningAppProcessInfo> {


        public StableArrayAdapter(Context context,
                                 ArrayList<RunningAppProcessInfo> objects) {
            super(context, 0);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RunningAppProcessInfo s = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.process_layout, parent, false);
            }

            TextView processName = convertView.findViewById(R.id.process_name);
//            processName.setMinHeight(200);
            processName.setText(s.processName);

            TextView processImportance = convertView.findViewById(R.id.process_importance);
//            processImportance.setMinHeight(200);
            processImportance.setText("Importance: " + Integer.toString(s.importance));

            TextView processId = convertView.findViewById(R.id.process_id);
            processId.setText("PID: " + Integer.toString(s.pid));

            return convertView;
        }

    }

    class SortByPID implements Comparator<RunningAppProcessInfo>
    {
        // Used for sorting in descending order of importance
        public int compare(RunningAppProcessInfo a, RunningAppProcessInfo b)
        {
            return b.importance - a.importance;
        }
    }
}
