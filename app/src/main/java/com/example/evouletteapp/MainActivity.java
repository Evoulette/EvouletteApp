package com.example.evouletteapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.evouletteapp.objects.Event;
import com.example.evouletteapp.objects.Events;
import com.example.evouletteapp.objects.Ticket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.example.evouletteapp.EventlistHelper.objectMapper;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = "MainActivity" ;
    int participants;
    CharSequence location;
    int radius;
    int budget;
    CharSequence date;
    CharSequence time;
    static final int PICK_VALUES_REQUEST = 1;
    CharSequence standardLocation;
    double price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null){
            EditText location_editText = findViewById(R.id.searchscreenLocation_editText);
            Spinner participants_spinner = findViewById(R.id.searchscreenParticipants_spinner);
            Spinner budget_spinner =findViewById(R.id.searchscreenBudget_spinner);
            EditText date_editText = findViewById(R.id.searchscreenDate_editText);
            EditText time_editText = findViewById(R.id.searchscreenTime_editText);

            participants = savedInstanceState.getInt("participants");
            location = savedInstanceState.getCharSequence("location");
            radius = savedInstanceState.getInt("spinner");
            budget = savedInstanceState.getInt("budget");
            date = savedInstanceState.getCharSequence("date");
            time = savedInstanceState.getCharSequence("time");

            Log.i(TAG, " ba " + participants);

            participants_spinner.setSelection(participants);
            location_editText.setText(location);
            budget_spinner.setSelection(budget);
            date_editText.setText(date);
            time_editText.setText(time);

            Log.i(TAG, "onCreate - restore");



        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        standardLocation = getStandardLocation();

        Log.i(TAG, "onCreate");

    }

    @Override
    protected void onStart(){
        super.onStart();
        EditText location_editText = findViewById(R.id.searchscreenLocation_editText);
        location_editText.setText(getStandardLocation());
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void spin(View view) throws IOException {
        Spinner participants_spinner =findViewById(R.id.searchscreenParticipants_spinner);
        EditText location_editText = findViewById(R.id.searchscreenLocation_editText);
        Spinner budget_spinner = findViewById(R.id.searchscreenBudget_spinner);
        EditText date_editText = findViewById(R.id.searchscreenDate_editText);
        EditText time_editText = findViewById(R.id.searchscreenTime_editText);

        participants = participants_spinner.getSelectedItemPosition();
        location = location_editText.getText().toString();
        budget = budget_spinner.getSelectedItemPosition();
        date = date_editText.getText().toString();
        time = time_editText.getText().toString();

        price = budgettoprice(budget);
        participants++;

        Log.i(TAG, "rausgezogene daten "+location +" "+date+" "+time);

        if(location.length()==0){
            Toast.makeText(MainActivity.this,"Please define location",
                    Toast.LENGTH_SHORT).show();
        }else if(date.length()==0){
            Toast.makeText(MainActivity.this,"Please define date",
                    Toast.LENGTH_SHORT).show();

        }else if(time.length()==0){
            Toast.makeText(MainActivity.this,"Please define time",
                    Toast.LENGTH_SHORT).show();

        }else {
            ArrayList<String> al = event(location, date, time, price);

            if (al.size()==0) {
                Toast.makeText(MainActivity.this, "No Events found",
                        Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("name", al.get(0));
                intent.putExtra("category", al.get(1));
                intent.putExtra("description", al.get(2));
                intent.putExtra("date", al.get(3));
                intent.putExtra("time", al.get(4));
                intent.putExtra("id", al.get(5));
                intent.putExtra("location", al.get(6));
                intent.putExtra("participants", participants);
                startActivity(intent);
            }
        }
    }

    private double budgettoprice(int budget) {
        double price;
        if(budget==0){
            price = 0;
        }else if(budget==1){
            price=5;
        }else if(budget==2){
            price=10;
        }else if(budget==3){
            price=20;
        }else if(budget==4){
            price=25;
        }else if(budget==5){
            price=40;
        }else if(budget==6){
            price=60;
        }else if(budget==7){
            price=100;
        }else price=0;

        return price;
    }

    private ArrayList<String> event(CharSequence location, CharSequence date, CharSequence time, double budget) throws IOException {
        ArrayList<Event> ale;
        ArrayList<String>als =new ArrayList<>();
        String eventsjson = EventlistHelper.getEvents(location.toString(), date.toString(), time.toString(), budget);
        Events events = objectMapper.readValue(eventsjson, Events.class);
        ale = events.getEvents();

        Log.i(TAG, "ale bekommen");
        if(!(ale.size() ==0)){
            Log.i(TAG, "ale hat werte");
            Event ev = ale.get(new Random().nextInt(ale.size()));

            als.add(ev.getName());
            als.add(ev.getCategory());
            als.add(ev.getDescription());
            als.add(ev.getDate());
            als.add(ev.getTime());
            als.add(ev.getId());
            als.add(ev.getLocation());
        }
        return als;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wallet) {
            Intent intent = new Intent(this, TicketWalletActivity.class);
            intent.putExtra("forResult", false);
            intent.putExtra("delete", false);
            startActivity(intent);
        } else if (id == R.id.nav_loadValues) {
            Intent intent = new Intent(this, TicketWalletActivity.class);
            intent.putExtra("forResult", true);
            intent.putExtra("requestCode",PICK_VALUES_REQUEST);
            startActivityForResult(intent,PICK_VALUES_REQUEST);

        }else if (id == R.id.nav_deleteTicket){
            Intent intent = new Intent(this, TicketWalletActivity.class);
            intent.putExtra("forResult", false);
            intent.putExtra("delete", true);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");

        Spinner participants_spinner = findViewById(R.id.searchscreenParticipants_spinner);
        EditText location_editText = findViewById(R.id.searchscreenLocation_editText);
        Spinner budget_spinner = findViewById(R.id.searchscreenBudget_spinner);
        EditText date_editText = findViewById(R.id.searchscreenDate_editText);
        EditText time_editText = findViewById(R.id.searchscreenTime_editText);


        participants = participants_spinner.getSelectedItemPosition();
        location = location_editText.getText().toString();
        budget = budget_spinner.getSelectedItemPosition();
        date = date_editText.getText().toString();
        time = time_editText.getText().toString();

        Log.i(TAG, "onSaveInstanceState" + participants + " " + location + " " + radius);

        outState.putInt("participants", participants);
        outState.putCharSequence("location", location);
        outState.putInt("radius", radius);
        outState.putInt("budget", budget);
        outState.putCharSequence("date", date);
        outState.putCharSequence("time", time);

        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        Log.i(TAG, "onRestoreInstanceState");

        Spinner participants_spinner = findViewById(R.id.searchscreenParticipants_spinner);
        EditText location_editText = findViewById(R.id.searchscreenLocation_editText);
        Spinner budget_spinner =findViewById(R.id.searchscreenBudget_spinner);
        EditText date_editText = findViewById(R.id.searchscreenDate_editText);
        EditText time_editText = findViewById(R.id.searchscreenTime_editText);

        participants = savedInstanceState.getInt("participants");
        location = savedInstanceState.getCharSequence("location");
        radius = savedInstanceState.getInt("spinner");
        budget = savedInstanceState.getInt("budget");
        date = savedInstanceState.getCharSequence("date");
        time = savedInstanceState.getCharSequence("time");

        participants_spinner.setSelection(participants);
        location_editText.setText(location);
        budget_spinner.setSelection(budget);
        date_editText.setText(date);
        time_editText.setText(time);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i(TAG,"Activity Result erreicht");
        if (requestCode == PICK_VALUES_REQUEST) {
            Log.i(TAG, "Requestcode gut " + RESULT_OK);
            if (resultCode == RESULT_OK) {
                // The Intent's data Uri identifies which contact was selected.
                Activity activity = this;
                Context context = activity;
                String id;
                id = data.getStringExtra("result");
                int iddb = Integer.valueOf(id);
                DatabaseHelper mDatabasehelper;
                mDatabasehelper = new DatabaseHelper(context);
                Ticket ticket = mDatabasehelper.getTicketById(iddb);
                Log.i(TAG, ticket.getLocation());
                Log.i(TAG, ticket.getDate());
                Log.i(TAG, ticket.getTime());

                EditText location_editText = findViewById(R.id.searchscreenLocation_editText);
                EditText date_editText = findViewById(R.id.searchscreenDate_editText);
                EditText time_editText = findViewById(R.id.searchscreenTime_editText);

                location_editText.setText(ticket.getLocation());
                time_editText.setText(ticket.getTime());
                date_editText.setText(ticket.getDate());
            }
        }

    }

    @Override
    public void onClick(View v) {

    }

    private CharSequence getStandardLocation() {
        Activity activity = this;
        Context context = activity;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        standardLocation = sharedPref.getString("standardLocation_text","Frankfurt");

        Log.i(TAG, "standardloc " + standardLocation);

        return standardLocation;
    }
}
