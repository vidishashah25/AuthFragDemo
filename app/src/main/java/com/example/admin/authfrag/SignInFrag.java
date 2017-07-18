package com.example.admin.authfrag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 2/22/2017.
 */

public class SignInFrag extends Fragment {


    EditText etEmail;
    EditText etPassword;
    Button bSignIn;
    Button bSignUp;
    TextView tvValid;
    boolean validDetails=false;



    FirebaseAuth mAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_signin, container, false);

        mAuth = FirebaseAuth.getInstance();
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etPassword = (EditText) v.findViewById(R.id.etPassword);
        bSignIn = (Button) v.findViewById(R.id.bSignIn);
        bSignUp = (Button) v.findViewById(R.id.bSignUp);
        tvValid = (TextView)v.findViewById(R.id.tvValid);



        final String email = etEmail.getText().toString().trim();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                if(email.matches(emailPattern) && s.length()>0){
//                    tvValid.setText("Valid Email");
//                }
//                else {
//                    tvValid.setText("Invalid Email");
//                }

                if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
                    etEmail.setError("Invalid Email");
                }
                else
                {
                    validDetails = true;
                }

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String pass = etPassword.getText().toString();
                if (!isValidPassword(pass)) {
                    etPassword.setError("Invalid Password");
                }
                else {
                    validDetails = true;
                }


            }
        });



        


        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                // getChildFragmentManager().beginTransaction().add(new SignUpFrag(), null).addToBackStack(null).commit();
                MainActivity.mViewPager.setCurrentItem(1);



//            TODO (1,FragtoActivity):    Intent i = new Intent(getActivity(), LoginPageActivity.class);
//                                        startActivity(i);
//                                        getActivity().finish();
            }
        });




        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        return v;

    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }


    public void signIn() {

        String sEmail = etEmail.getText().toString();
        String sPassword = etPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            getUserDetails();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Authentification Failed ", Toast.LENGTH_SHORT).show();
                        }
                    }

                });


    }

    private void getUserDetails() {

        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = fbDatabase
                .getReference()
                .child("users")
                .child(mAuth.getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);

                SPUserDetails save = new SPUserDetails(getActivity());
                save.setKEY_USER_NAME(user.getName());
                save.setKEY_USER_MOB(user.getMobilenum());
                Intent i = new Intent(getActivity(), LoginPageActivity.class);
                i.putExtra("INTENT_NAME",user.getName());
                startActivity(i);
                getActivity().finish();

                Log.d("Sign In", "onDataChange:" + user.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
