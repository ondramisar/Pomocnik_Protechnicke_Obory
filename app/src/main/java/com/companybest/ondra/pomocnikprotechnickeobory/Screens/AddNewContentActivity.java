package com.companybest.ondra.pomocnikprotechnickeobory.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.companybest.ondra.pomocnikprotechnickeobory.R;
import com.companybest.ondra.pomocnikprotechnickeobory.Utiliti.Content;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddNewContentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button buttonSendReguest;
    EditText editText;
    EditText titleOfCategory;
    String item;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_content);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSendReguest = (Button) findViewById(R.id.buttonSendRequest);

        editText = (EditText) findViewById(R.id.editText);

        titleOfCategory = (EditText) findViewById(R.id.editText2);

        Spinner categoryChose = (Spinner) findViewById(R.id.spinner);

        categoryChose.setOnItemSelectedListener(this);

        ArrayList<String> category = new ArrayList<>();
        category.add("Mechanika");
        category.add("Sestavování a provoz strojů");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categoryChose.setAdapter(arrayAdapter);




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        item = parent.getItemAtPosition(position).toString();
        Log.i("Item", item);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void sendNewRequest(Boolean able, String content, String name){
        Content newRequest = new Content(able, content, name);

        mDatabase.child("Classes").child(item).child(String.valueOf(titleOfCategory.getText())).setValue(newRequest);

    }

    public void sendRequest(View view){

        sendNewRequest(false, String.valueOf(editText.getText()), String.valueOf(titleOfCategory.getText()));

        Log.i("content", "END");


        Intent intent = new Intent(getApplicationContext(), ClassSelect.class);
        Toast.makeText(this.getApplicationContext(), "Posláno", Toast.LENGTH_SHORT).show();

        startActivity(intent);


    }

    public void addImg(View view){


    }


}
