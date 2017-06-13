package com.companybest.ondra.pomocnikprotechnickeobory.Screens;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.companybest.ondra.pomocnikprotechnickeobory.Utiliti.Content;
import com.companybest.ondra.pomocnikprotechnickeobory.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContentActivity extends AppCompatActivity {


    static ArrayList<String> content;
    static ArrayAdapter arrayAdapter;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setTitle("Témata");

        content = new ArrayList<String>();

        final ListView userListView = (ListView) findViewById(R.id.contentListView);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, content);

        userListView.setAdapter(arrayAdapter);





        Intent intent = getIntent();

        final String titleString = intent.getStringExtra("title");



        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ContentDeatailActivity.class);
                intent.putExtra("content", content.get(position));
                Log.i("user", content.get(position));
                intent.putExtra("title", titleString);

                startActivity(intent);

            }
        });

        mDatabase.child("Classes").child(titleString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()){

                    Content contentNew = data.getValue(Content.class);

                    if (contentNew.able){
                        content.add(contentNew.name);
                        Log.i("user", contentNew.name);

                    }

                    arrayAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*mDatabase.child("Classes").child(titleString).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                String value = dataSnapshot.getKey();
                //String contentnew = dataSnapshot.getValue(String.class);

                content.add(value);

                Log.i("user", value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }

}
