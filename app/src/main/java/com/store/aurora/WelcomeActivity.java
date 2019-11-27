package com.store.aurora;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {
    DatabaseHelper getMydata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getMydata = new DatabaseHelper(this);

        String workerName=getMydata.getUserData()[0];
        String workerType=getMydata.getUserData()[1];
        String url=getMydata.getUserData()[2];

        //getMydata.getDeliveryNotes();


        TextView userName = (TextView) findViewById(R.id.userName);
        userName.setText(workerName);

        TextView userType = (TextView) findViewById(R.id.userType);
        userType.setText(workerType);
        Button scanBtn = (Button) findViewById(R.id.btn_deliveryNote);

        scanBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(WelcomeActivity.this,DeliveryNoteRefreshActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        //==========================================================================================

        Button btn_pack = (Button) findViewById(R.id.btn_pack);

        btn_pack.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(WelcomeActivity.this,packActivity.class);
                        startActivity(intent);
                    }
                }
        );

        //==========================================================================================

        //==========================================================================================


        Button logOut = (Button) findViewById(R.id.logOut);

        logOut.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        Toast.makeText(getApplicationContext(), "Login Out...", Toast.LENGTH_LONG).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                        builder.setMessage("Do you want to exit ?");
                        builder.setTitle("Aurora Picking App");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                if(getMydata.clearAccount()){
                                    System.out.println("Account deleted");

                                    getMydata.close();

                                    Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }

                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });

                        // Create the Alert dialog
                        AlertDialog alertDialog = builder.create();

                        // Show the Alert Dialog box
                        alertDialog.show();




                    }
                }
        );

        //==========================================================================================
        //==========================================================================================


        Button exit = (Button) findViewById(R.id.exit);

        exit.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        Toast.makeText(getApplicationContext(), "Shutting Down...", Toast.LENGTH_LONG).show();


                        finishAffinity();
                        finish();
                        ActivityCompat.finishAffinity(WelcomeActivity.this);
                        System.exit(0);
                    }
                }
        );

        //==========================================================================================



    }
}
