package com.companybest.ondra.pomocnikprotechnickeobory.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.companybest.ondra.pomocnikprotechnickeobory.MainActivity;
import com.companybest.ondra.pomocnikprotechnickeobory.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ClassSelect extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.share_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.share){

            Intent intent = new Intent(getApplicationContext(), AddNewContentActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.logout){

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent);

        } else if (item.getItemId() == R.id.oAplikaci){

//            Intent intent = new Intent(getApplicationContext(), OAplikaciActivity.class);
  //          startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_select);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setTitle("Předměty");

        final ArrayList<String> content = new ArrayList<String>();

        final ListView userListView = (ListView) findViewById(R.id.userListView);

        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, content);
        userListView.setAdapter(arrayAdapter);




        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                intent.putExtra("title", content.get(position));

                startActivity(intent);

            }
        });





        mDatabase.child("Classes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getKey();
                content.add(value);

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
        });


    }
}
