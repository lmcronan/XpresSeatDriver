package com.leanza.xpresseat_driver;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leanza.xpresseat_driver.Data.Passenger;
import com.leanza.xpresseat_driver.Data.Van;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DriverProfileActivity extends Activity {
    final static String DB_URL = "https://xpresseat-180f4.firebaseio.com/Van/";
    final Firebase fire = new Firebase(DB_URL);

    final static String DB_URL2 = "https://xpresseat-180f4.firebaseio.com/";
    final Firebase fire2 = new Firebase(DB_URL2);

    final static String DB_URL3 = "https://xpresseat-180f4.firebaseio.com/Van/";
    final Firebase fire3 = new Firebase(DB_URL3);
    String email,van, driver, notify, email2, status, vacant, route;

    TextView textView2, vacantBtn, plateBtn, routeTv;

    String condition="true"; //condition to make notification pop up once

    Handler mHandler;
    //Runnable refresh;

    ListView passengers_lv;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_driver_profile);

        passengers_lv = (ListView) findViewById(R.id.passengersName);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        passengers_lv.setAdapter(adapter);

        Query queryRef2;
        queryRef2 = fire.orderByChild("email").equalTo(email);

        queryRef2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.clear();

                //String name = (String) dataSnapshot.child("passenger").child("0").child("name").getValue();
                //String name;
                long count = dataSnapshot.child("passenger").getChildrenCount();
                System.out.println("countz: " + count);

                for (int i = 0; i<count; i++) {
                    String iStr = Integer.toString(i) ;
                    String name = String.valueOf(dataSnapshot.child("passenger").child(iStr).child("name").getValue());
                    list.add(name);
                    adapter.notifyDataSetChanged();
                }




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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        //this.mHandler = new Handler();

        //this.mHandler.postDelayed(m_Runnable,2000);
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("userEmail");
        //email = "email here";

        //FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //DatabaseReference dbRef = firebaseDatabase.getReference();

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Query queryRef;
                                queryRef = fire.orderByChild("email").equalTo(email);

                                queryRef.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        van = (String) dataSnapshot.getValue(Van.class).getName();
                                        route = (String) dataSnapshot.getValue(Van.class).getRoute();
                                        driver = (String) dataSnapshot.getValue(Van.class).getDriver();
                                        notify = (String) dataSnapshot.getValue(Van.class).getNotify();
                                        vacant = (String) dataSnapshot.getValue(Van.class).getVacantseat();

                                        System.out.println("notify value: " + notify);
                                        System.out.println("Email value: " + email);

                                        System.out.println("driver value: " + driver);


                                        plateBtn = (TextView) findViewById(R.id.driversPlate);
                                        plateBtn.setText(van);
                                        textView2 = (TextView) findViewById(R.id.driversName);
                                        textView2.setText(driver);
                                        vacantBtn = (TextView) findViewById(R.id.driversVacant);
                                        vacantBtn.setText(vacant);
                                        routeTv = (TextView) findViewById(R.id.driversRoute);
                                        routeTv.setText(route);

                                        if(notify.equals("OFF")) {
                                            condition = "true";
                                        }

                                        if(notify.equals("ON") && condition.equals("true")) {
                                            //turn flase condtion so that prompt will not duplicate
                                            condition="false";


                                            //SHOW DIALOG BOX
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DriverProfileActivity.this);
                                            alertDialog.setMessage("User reserved a seat in your van");


                                            alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    Query query4;
                                                    query4 = fire2.child("Van").orderByChild("email").equalTo(email);

                                                    query4.addListenerForSingleValueEvent(new ValueEventListener() {

                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                                                            String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`

                                                            String path = "/" + dataSnapshot.getKey() + "/" + key + "/notify";

                                                            fire2.child(path).setValue("OFF");


                                                        }

                                                        @Override
                                                        public void onCancelled(FirebaseError databaseError) {
                                                            //Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
                                                        }
                                                    });



                                                    dialog.cancel();

                                                    Query queryRef2;
                                                    queryRef2 = fire.orderByChild("email").equalTo(email);

                                                    queryRef2.addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                            list.clear();

                                                            //String name = (String) dataSnapshot.child("passenger").child("0").child("name").getValue();
                                                            //String name;
                                                            long count = dataSnapshot.child("passenger").getChildrenCount();
                                                            System.out.println("countz: " + count);

                                                            for (int i = 0; i<count; i++) {
                                                                String iStr = Integer.toString(i) ;
                                                                String name = String.valueOf(dataSnapshot.child("passenger").child(iStr).getValue());
                                                                list.add(name);
                                                                adapter.notifyDataSetChanged();
                                                            }




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
                                                        public void onCancelled(FirebaseError firebaseError) {

                                                        }
                                                    });
                                                }
                                            });

                                            alertDialog.show();  //<-- See This!*/


                                        }
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
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });




                                //Toast.makeText(DriverProfileActivity.this,"in runnablez",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();






    }

   /* private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            Toast.makeText(DriverProfileActivity.this,"in runnable",Toast.LENGTH_SHORT).show();

            //DriverProfileActivity.this.mHandler.postDelayed(m_Runnable, 5000);
            mHandler.postDelayed(this, 2000);
        }

    };//runnable*/

    public void toggleStatus(View view) {




        Query query2;
        query2 = fire3.orderByChild("email").equalTo(email);
        System.out.println("HERE IN TOGGLE STATUS");
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`

                status = (String) dataSnapshot.getValue(Van.class).getStatus();

                Button statusBtn = (Button) findViewById(R.id.toggleStatusBtn);

                String path = "/" + dataSnapshot.getKey() + "/" + key + "/status";
                System.out.println("HERE IN NEAR IF");
                System.out.println("STATUS: " + status);
                System.out.println("QWQW: " + path);
                System.out.println("email: " + email);

                /*if(status.equals("ON")){
                    fire3.child(path).setValue("OFF");
                    statusBtn.setText("TURN OFF STATUS");
                }else if(status.equals("OFF")){
                    fire3.child(path).setValue("ON");
                    statusBtn.setText("TURN ON STATUS");
                }*/

                fire3.child(path).setValue("OFF");
                statusBtn.setText("TURN OFF STATUS");

                System.out.println("QWQW: " + path);
                System.out.println("email: " + email);




            }

            @Override
            public void onCancelled(FirebaseError databaseError) {
                //Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
            }
        });
    }

    public void logoutDriver(View view) {
        //change status from "ON" to "OFF"
        Query query;
        query = fire2.child("Van").orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`

                System.out.println("KEYYY: " + key);

                String path = "/" + dataSnapshot.getKey() + "/" + key + "/status";

                fire2.child(path).setValue("OFF");


            }

            @Override
            public void onCancelled(FirebaseError databaseError) {
                //Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
            }
        });


        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(DriverProfileActivity.this, MainActivity.class));
    }




    public void logoutDriver2(View view) {
        final Query queryToggle;
        queryToggle = fire2.child("Van").orderByChild("email").equalTo(email);

        //change status from "ON" to "OFF"
        final ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleStatusBtn);
        final TextView statusText = (TextView) findViewById(R.id.statusText);



        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    queryToggle.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`

                            String path = "/" + dataSnapshot.getKey() + "/" + key + "/status";

                            fire2.child(path).setValue("OFF");
                            toggle.setBackgroundColor(Color.parseColor("#5cb85c"));
                            toggle.setText("TURN ON STATUS");

                            statusText.setBackgroundColor(Color.parseColor("#d9534f"));
                            statusText.setText("Your van is CLOSED for reservation.");




                        }

                        @Override
                        public void onCancelled(FirebaseError databaseError) {
                            //Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
                        }
                    });
                } else {

                    // The toggle is disabled
                    queryToggle.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`

                            String path = "/" + dataSnapshot.getKey() + "/" + key + "/status";

                            fire2.child(path).setValue("ON");

                            toggle.setBackgroundColor(Color.parseColor("#d9534f"));
                            toggle.setText("TURN OFF STATUS");

                            statusText.setBackgroundColor(Color.parseColor("#5cb85c"));
                            statusText.setText("Your van is OPEN for reservation.");




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
