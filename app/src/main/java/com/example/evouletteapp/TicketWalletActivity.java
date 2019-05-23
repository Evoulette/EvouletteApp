package com.example.evouletteapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.evouletteapp.objects.TicketContent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class TicketWalletActivity extends AppCompatActivity {

    private static final String TAG = "TicketWalletActivity";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final Logger log = LoggerFactory.getLogger(TicketWalletActivity.class);
    String requestCode;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_wallet);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabaseHelper = new DatabaseHelper(this);

        recyclerView = (RecyclerView) findViewById(R.id.ticket_walletRecyclerView_RecyclerView);

        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        ItemFragment.OnListFragmentInteractionListener listener = new ItemFragment.OnListFragmentInteractionListener(){

            @Override
            public void onListFragmentInteraction(TicketContent.TicketItem item) {
               if(!getIntent().getBooleanExtra("forResult",true)){
                  if(!getIntent().getBooleanExtra("delete", true)) showTicket(item.id);
                  else deleteTicket(item.id);
               }
               else{
                   requestCode = getIntent().getStringExtra("requestCode");
                   returnId(item.id, requestCode);
               }
            }
        };

        populateRecyclerView(listener);

        Log.i(TAG, "onCreate");
    }

    private void deleteTicket(String id) {
        boolean deleted = mDatabaseHelper.deleteData(Integer.valueOf(id));
        if(deleted) Log.i(TAG, "geloescht");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private void populateRecyclerView(ItemFragment.OnListFragmentInteractionListener listener) {
        Cursor data = mDatabaseHelper.getData();
        ArrayList<TicketContent.TicketItem> listData = new ArrayList<>();
        while(data.moveToNext()){
            String id = data.getString(0);
            String content = data.getString(1);
            String date = data.getString(3);
            listData.add(new TicketContent.TicketItem(id, content, date));
        }
        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(listData, listener));

    }

    private void showTicket(String id) {
        Intent intent = new Intent(this, TicketActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");

        super.onDestroy();
    }



    public void returnId(String result, String requestCode){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",result);
        returnIntent.putExtra("requestCode", requestCode);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
