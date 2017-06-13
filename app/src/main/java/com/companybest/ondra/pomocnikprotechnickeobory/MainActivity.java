package com.companybest.ondra.pomocnikprotechnickeobory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.companybest.ondra.pomocnikprotechnickeobory.Screens.ClassSelect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean signUpMOdeActive = true;
    TextView changeSignUpTextView;

    EditText passwordEditText;

    //variables for firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public void showUserList() {
        Intent intet = new Intent(getApplicationContext(), ClassSelect.class);
        startActivity(intet);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            signUp(v);
            //when enter key press sign up
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        // change from sign up and log in and other way
        if (v.getId() == R.id.changeSignUpTextView) {

            Button signUpButton = (Button) findViewById(R.id.signUpButton);

            if (signUpMOdeActive) {

                signUpMOdeActive = false;
                signUpButton.setText("Login");
                changeSignUpTextView.setText("Or, Signup");

            } else {

                signUpMOdeActive = true;
                signUpButton.setText("Signup");
                changeSignUpTextView.setText("Or, Login");

            }

        } else if (v.getId() == R.id.backgroundRelativeLayaut) {
            // when touch on screen and keyboard close keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }

    }

    public void signUp(View view) {

        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);

        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {

            // user didnt email and password
            Toast.makeText(this, "A email and password are required", Toast.LENGTH_SHORT).show();

        } else {
            String email = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (signUpMOdeActive) {

                // sign user to database
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("user", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "fail",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            } else {

                // sign in when is mode to log in
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("user", "signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w("user", "signInWithEmail:failed", task.getException());
                                    Toast.makeText(MainActivity.this, "fail",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // when activity creted

        mAuth = FirebaseAuth.getInstance();
        // when is user sign
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("user", "onAuthStateChanged:signed_in:" + user.getUid());

                    showUserList();
                } else {
                    // User is signed out
                    Log.d("user", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        setTitle("Pro učení");


        // seting up layout
        changeSignUpTextView = (TextView) findViewById(R.id.changeSignUpTextView);
        changeSignUpTextView.setOnClickListener(this);

        RelativeLayout background = (RelativeLayout) findViewById(R.id.backgroundRelativeLayaut);
        background.setOnClickListener(this);


        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setOnKeyListener(this);

    }


    // needed for firebase auth
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

