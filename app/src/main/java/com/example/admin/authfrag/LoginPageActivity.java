package com.example.admin.authfrag;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPageActivity extends AppCompatActivity {

    TextView tvDetails;
    TextView tvUsernum;
    Button bSignOut;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    String TAG  = "LoginPageActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Intent i =getIntent();



        String name = i.getStringExtra("INTENT_NAME");
        tvDetails = (TextView) findViewById(R.id.tvUserDetails);
        tvUsernum = (TextView) findViewById(R.id.tvUsernum);
        bSignOut = (Button) findViewById(R.id.bsignout);

        SPUserDetails save = new SPUserDetails(LoginPageActivity.this);
        tvDetails.setText("Hello "+ save.getKEY_USER_NAME() + " !");
        tvUsernum.setText(save.getKEY_USER_MOB());
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                    Intent i = new Intent(LoginPageActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        };

        bSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });



    }

    @Override
    public void onStart() {
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


    public void signOut(){
        mAuth.signOut();


    }
}


