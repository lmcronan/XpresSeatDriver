package com.leanza.xpresseat_driver;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.leanza.xpresseat_driver.Data.Van;

public class LoginDriverActivity extends Activity {

    private EditText emailField;
    private EditText pwordField;
    private Button loginBtn;

    String email;
    String pword;

    final static String DB_URL = "https://xpresseat-180f4.firebaseio.com/";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Firebase ref = new Firebase("https://xpresseat-180f4.firebaseio.com/Driver");
    //String email = ref.getAuth().password.email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_driver);

        mAuth = FirebaseAuth.getInstance();

        emailField = (EditText) findViewById(R.id.emailLoginDriver);
        pwordField = (EditText) findViewById(R.id.pwordLoginDriver);
        loginBtn = (Button) findViewById(R.id.loginDriverBtn);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) { //user is logged in
                    String userEmail = mAuth.getCurrentUser().getEmail();

                    Toast.makeText(LoginDriverActivity.this, userEmail, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginDriverActivity.this, DriverProfileActivity.class);
                    intent.putExtra("userEmail", userEmail);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    protected void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    public void logInDriver(View view){
        email = emailField.getText().toString().trim();
        pword = pwordField.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pword)){
            Toast.makeText(LoginDriverActivity.this, "Fields are empty.", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginDriverActivity.this, "Log in problem", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginDriverActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();

                        //ONCE THE DRIVER LOGS IN, CHANGE ITS RESERVATION STATUS TO "ON"
                        final Firebase fire = new Firebase(DB_URL);
                        Query queryRef;
                        queryRef = fire.child("Van").orderByChild("email").equalTo(email);

                        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                                String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`

                                System.out.print("KEY: " + key);
                                String path = "/" + dataSnapshot.getKey() + "/" + key;

                                System.out.println("PATH: " + path);

                                fire.child(path + "/status").setValue("ON");
                            }

                            @Override
                            public void onCancelled(FirebaseError databaseError) {
                                //Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
                            }
                        });
                    }
                }
            });
        }
    }
}
