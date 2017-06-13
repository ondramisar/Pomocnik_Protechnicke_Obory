package com.companybest.ondra.pomocnikprotechnickeobory.Screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.companybest.ondra.pomocnikprotechnickeobory.R;
import com.companybest.ondra.pomocnikprotechnickeobory.Utiliti.Content;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ContentDeatailActivity extends AppCompatActivity {


    TextView contentTextView;
    ImageView image;
    float fontsize = 13;
    Matrix matrix;

    static String content;
    private ScaleGestureDetector mScaleDetector;
    private float scale = 1f;

    private DatabaseReference mDatabase;

    final static float STEP = 200;
    float mRatio = 1.0f;
    int mBaseDist;
    float mBaseRatio;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_deatail);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();



        //mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contentTextView = (TextView) findViewById(R.id.textView);

        image = (ImageView) findViewById(R.id.imageView2);


        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        final String content = intent.getStringExtra("content");

        setTitle("Vysvětlení");

        mDatabase.child("Classes").child(title).child(content).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Content contentnew = dataSnapshot.getValue(Content.class);

//                Log.i("user", contentnew.content);
                Log.i("user", content);

                contentTextView.setText(contentnew.content);
                contentTextView.setTextSize(mRatio + 13);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Create a reference with an initial file path and name
        StorageReference pathReference = storageRef.child(title + "/" + content + "/" + content + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                if (bytes != null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    image.setImageBitmap(bmp);
                    Log.i("user", "done img");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        contentTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getPointerCount() == 2) {
                    int action = event.getAction();
                    int pureaction = action & MotionEvent.ACTION_MASK;
                    if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
                        mBaseDist = getDistance(event);
                        mBaseRatio = mRatio;
                    } else {
                        float delta = (getDistance(event) - mBaseDist) / STEP;
                        float multi = (float) Math.pow(2, delta);
                        mRatio = Math.min(1024.0f, Math.max(0.1f, mBaseRatio * multi));
                        contentTextView.setTextSize(mRatio + 13);
                    }
                }
                return true;
            }

            int getDistance(MotionEvent event) {
                int dx = (int) (event.getX(0) - event.getX(1));
                int dy = (int) (event.getY(0) - event.getY(1));
                return (int) (Math.sqrt(dx * dx + dy * dy));
            }
        });
    }
}






    /*public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            int action = event.getAction();
            int pureaction = action & MotionEvent.ACTION_MASK;
            if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
                mBaseDist = getDistance(event);
                mBaseRatio = mRatio;
            } else {
                float delta = (getDistance(event) - mBaseDist) / STEP;
                float multi = (float) Math.pow(2, delta);
                mRatio = Math.min(1024.0f, Math.max(0.1f, mBaseRatio * multi));
                contentTextView.setTextSize(mRatio + 13);
                image.setScaleX(mRatio + 12);
                image.setScaleY(mRatio + 12);
            }
        }
        return true;
    }

    int getDistance(MotionEvent event) {
        int dx = (int) (event.getX(0) - event.getX(1));
        int dy = (int) (event.getY(0) - event.getY(1));
        return (int) (Math.sqrt(dx * dx + dy * dy));
    }

    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);
        Log.i("scale", "touch");
        return true;
    }*/





