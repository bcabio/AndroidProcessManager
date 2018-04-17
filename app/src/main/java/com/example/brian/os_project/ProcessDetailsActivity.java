package com.example.brian.os_project;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Process;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.ActivityManager.RunningAppProcessInfo;
import com.example.brian.os_project.ProcessList;
import android.app.ActivityManager;
import android.os.Debug.MemoryInfo;

import org.w3c.dom.Text;

public class ProcessDetailsActivity extends AppCompatActivity {

    ActivityManager activityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        final RunningAppProcessInfo runningAppProcessInfo = intent.getParcelableExtra(ProcessList.PROCESS_DETAILS_INTENT_MESSAGE);
        TextView processDetailsName = findViewById(R.id.process_details_name);
        TextView processMemoryUsage = findViewById(R.id.process_memory_usage);
        TextView processPackageName = findViewById(R.id.process_package_name);

        String processName = intent.getStringExtra("PROCESS_NAME");

        activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);

        MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
        int totalPrivateCleanMemory = memoryInfos[0].getTotalPrivateDirty();

        String packagePrefix = intent.getStringExtra("PACKAGE_PREFIX");

        processPackageName.setText(packagePrefix);
        processDetailsName.setText(processName);
        processMemoryUsage.setText("Total Memory Usage: " + String.valueOf(totalPrivateCleanMemory) + "Kb");

        Button killProcessButton = findViewById(R.id.kill_process_button);
        killProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupKillPopup(runningAppProcessInfo);
            }
        });
    }

    protected void setupKillPopup(final RunningAppProcessInfo runningAppProcessInfo) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ProcessDetailsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(ProcessDetailsActivity.this);
        }

        builder.setTitle("ATTENTION")
                .setMessage("Are you sure you would like to kill process " + runningAppProcessInfo.processName
                            + " PID: " + runningAppProcessInfo.pid + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Process.killProcess(runningAppProcessInfo.pid);
                        activityManager.killBackgroundProcesses(runningAppProcessInfo.processName);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
