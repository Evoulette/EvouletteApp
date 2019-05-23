package com.example.evouletteapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.evouletteapp.objects.Ticket;

public class TicketActivity extends AppCompatActivity {

    private static final String TAG = "TicketActivity";
    String id;
    int iddb;
    String name;
    String location;
    String date;
    String time;
    String description;
    DatabaseHelper mDatabasehelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tktnameTextView = findViewById(R.id.eventpageHeadline_textView);
        TextView tktdateTextView = findViewById(R.id.eventpageDate_textView);
        TextView tktlocationTextView = findViewById(R.id.eventpageLocation_textView);
        TextView tkttimeTextView = findViewById(R.id.eventpageTime_textView);
        TextView tktdescriptionTextView = findViewById(R.id.eventpageDescription_TextView);

        Activity activity = this;
        Context context = activity;

        id = getIntent().getStringExtra("id");
        iddb = Integer.valueOf(id);

        mDatabasehelper = new DatabaseHelper(context);
        Ticket ticket = mDatabasehelper.getTicketById(iddb);

        tktnameTextView.setText(ticket.getName());
        tktdateTextView.setText(ticket.getDate());
        tktlocationTextView.setText(ticket.getLocation());
        tkttimeTextView.setText(ticket.getTime());
        tktdescriptionTextView.setText(ticket.getDescription());

        Log.i(TAG, "onCreate");
    }

    @Override
    protected void onStart(){
        super.onStart();

        Log.i(TAG, "onStart");

    }

    @Override
    protected void onResume(){
        super.onResume();

        Log.i(TAG, "onResume");

    }

    @Override
    protected void onStop(){
        Log.i(TAG, "onStop");

        super.onStop();
    }

    @Override
    protected void onPause(){
        Log.i(TAG, "onPause");

        super.onPause();

    }

    @Override
    protected void onDestroy(){
        Log.i(TAG, "onDestroy");

        super.onDestroy();
    }

}
