package com.example.admin.authfrag;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Admin on 2/22/2017.
 */

public class SignUpFrag extends Fragment {

    EditText etName;
    EditText etEmail;
    EditText etPassword;
    EditText etMobile;
    Button bSignUp;

    UserModel userModel;

    FirebaseAuth mAuth;

    //ImageSelection Variables

    private static final int REQUEST_SELECT_IMAGE = 0;
    private Button bSelectImage;
    private ImageView ivLogoWiseL;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        mAuth = FirebaseAuth.getInstance();

        etName = (EditText) v.findViewById(R.id.etName);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etPassword = (EditText) v.findViewById(R.id.etPassword);
        etMobile = (EditText) v.findViewById(R.id.etMobile);
        bSignUp = (Button) v.findViewById(R.id.bSignUp);


        bSelectImage = (Button) v.findViewById(R.id.bSelectImage);
        ivLogoWiseL = (ImageView) v.findViewById(R.id.ivLogoWiseL);


        bSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SelectImageActivity.class);
                startActivityForResult(i,REQUEST_SELECT_IMAGE);
            }
        });


        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });




        return v;

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_SELECT_IMAGE && resultCode == getActivity().RESULT_OK) {

            Uri imageUri = data.getData();

            if (imageUri != null){

                ivLogoWiseL.setImageURI(imageUri);
            }

        }
    }

    public void signUp() {

        String sName = etName.getText().toString();
        String sEmail = etEmail.getText().toString();
        String sPassword = etPassword.getText().toString();
        String sMobile = etMobile.getText().toString();

        userModel = new UserModel(sName, sEmail, sMobile);


        mAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Intent i = new Intent(SignUpActivity.this,MainActivity.class);
//                            startActivity(i);
//                            finish();

                            String key = task.getResult().getUser().getUid();
                            addUserDetails(key);

                        } else {
                            Toast.makeText(getActivity(),
                                    "Authentification Failed ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void addUserDetails(String key) {

        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = fbDatabase.getReference();
        databaseReference.child("users")
                .child(key)
                .setValue(userModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                           String sname = etName.getText().toString();
                            String smob = etMobile.getText().toString();

                            SPUserDetails save = new SPUserDetails(getActivity());
                            save.setKEY_USER_NAME(sname);
                            save.setKEY_USER_MOB(smob);

                            Intent i = new Intent(getActivity(), LoginPageActivity.class);
                            startActivity(i);
                            getActivity().finish();


                        } else {


                            Toast.makeText(getActivity(),
                                    "Database Failed ", Toast.LENGTH_SHORT).show();
                            mAuth.getCurrentUser().delete();
                        }

                    }
                });
    }


}
