package com.store.aurora;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    // Declare Variables

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  On Create Declare Variables

        //==========================================================================================

        myDb = new DatabaseHelper(this);

        if (InternetConnection.checkConnection(this)) {
            Toast.makeText(getApplicationContext(),"Connect To aurora.systems ",Toast.LENGTH_SHORT).show();
            if(myDb.logOutCheck()){
                System.out.println("User out");


               // AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                // Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in
                // Remote MySQL DB
                // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 10 * 1000, pendingIntent);
                Toast.makeText(getApplicationContext(),"Scan to Log In",Toast.LENGTH_SHORT).show();
                OnClickScanButtonListener();


            }else{
                Toast.makeText(getApplicationContext(),"User Validated",Toast.LENGTH_SHORT).show();
                System.out.println("User in");
                Intent intent2 = new Intent(MainActivity.this,WelcomeActivity.class);
                startActivity(intent2);


            }

            // Its Available...
        } else {
            Toast.makeText(getApplicationContext(),"Can't Reach aurora.systems ",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this,NoNetworkActivity.class);
            startActivity(intent);
            finish();
        }


        //==========================================================================================




    }

    // Declare Button action
    public void OnClickScanButtonListener() {
        Button scanBtn = (Button) findViewById(R.id.btn_scan);

        scanBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }


}
