package com.leanza.xpresseat_driver;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        mAuth = FirebaseAuth.getInstance();

        //GET FIREBASE INSTANCE
        final String DB_URL = "https://xpresseat-180f4.firebaseio.com/Route";
        final Firebase fire = new Firebase(DB_URL);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) { //user is logged in
                    String userEmail = mAuth.getCurrentUser().getEmail();

                    Toast.makeText(MainActivity.this, userEmail, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, DriverProfileActivity.class);
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

    /*public void logInPopUp(View view) {
        //SHOW DIALOG BOX
        final CharSequence[] choice = {"Log In as Commuter", "Log In as Driver"};

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setItems(choice, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    System.out.println("chosen is commuter");
                    Intent intent = new Intent(MainActivity.this, LoginUserActivity.class);
                    intent.putExtra("role", "Commuter");
                    startActivity(intent);
                } else if ( which == 1) {
                    System.out.println("chosen is driver");
                    Intent intent = new Intent(MainActivity.this, LoginUserActivity.class);
                    intent.putExtra("role", "Driver");
                    startActivity(intent);
                }
            }
        });

        alertDialog.show();  //<-- See This!
    }

    /*public void goToLoginUserActivity(View view) {
        Intent intent = new Intent(MainActivity.this, LoginUserActivity.class);
        startActivity(intent);
    }*/

    public void goToLoginDriverActivity(View view) {
        Intent intent = new Intent(MainActivity.this, LoginDriverActivity.class);
        startActivity(intent);
    }

    /*public void goToSignUpActivity(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }*/



    public void test() {

    }

}
