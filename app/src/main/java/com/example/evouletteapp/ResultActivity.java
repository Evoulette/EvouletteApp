package com.example.evouletteapp;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class ResultActivity extends AppCompatActivity {


    private static final String TAG = "ResultActivity";
    DatabaseHelper mDatabasehelper;
    String name;
    String category;
    String description;
    String date;
    String time;
    int participants;
    String location;
    NotificationCompat.Builder notification;
    int uniqueId =2315;
    String CHANNEL_ID = "Channel1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createNotificationChannel();
        notification = new NotificationCompat.Builder(this, CHANNEL_ID);
        notification.setAutoCancel(true);

        mDatabasehelper = new DatabaseHelper(this);

        name = getIntent().getStringExtra("name");
        category = getIntent().getStringExtra("category");
        description =getIntent().getStringExtra("description");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        location = getIntent().getStringExtra("location");
        participants = getIntent().getIntExtra("participants", 0);

        TextView resname = findViewById(R.id.resultEvent_textView);
        TextView caname = findViewById(R.id.resultCategory_textView);
        TextView detext=  findViewById(R.id.resultDescription_textView);

        resname.setText(name);
        caname.setText(category);
        detext.setText(description);

        Log.i(TAG, "onCreate" );
    }

    @Override
    protected void onDestroy(){

        Log.i(TAG, "onDestroy" );

        super.onDestroy();


    }

    public void buttonAgainClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void buttonAcceptClicked(View view) {
        while(participants>0){
            addData(name, location, date, time, description);
            participants--;
        }
        Log.i(TAG, "addDATA erreicht");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addData(String name,String location, String date, String time, String description){
        boolean insertData = mDatabasehelper.addData(name, location, date, time, description);


        if(insertData){
            Log.i(TAG, "inserted " + name);
            notification();
        }else {
            Log.i(TAG, "insert failed " + name);
        }

    }

    public void notification(){
        String notifitext =getString( R.string.notification_content);
        notification.setSmallIcon(R.drawable.ic_info_black_24dp);
        notification.setTicker("This is a ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Evoulette");
        notification.setContentText(notifitext);

        Intent intent = new Intent(this, TicketWalletActivity.class);
        intent.putExtra("forResult", false);
        intent.putExtra("delete", false);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.setContentIntent(pendingIntent);
        nm.notify(uniqueId, notification.build());
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
