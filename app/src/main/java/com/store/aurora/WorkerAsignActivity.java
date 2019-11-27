package com.store.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WorkerAsignActivity extends AppCompatActivity {

    // Decalear Variables
    //================================================================
    DatabaseHelper staffList;
    private LinearLayoutManager mLayoutManager;
    private List<Profile> profileList;
    private ProfileAdapter profileAdapter;
    private  String dev_Note_key;

    //================================================================

    public Profile getProfile(int postion)
    {

        return  profileList.get(postion);
    }

    //================================================================



    //================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_asign);

        //=====================================================================================
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null) {
            dev_Note_key = (String) b.get("dev_id");
        }

        //=========================================================================================

        View v1= new View(this);
        Toast.makeText(v1.getContext(),"Loading Picker List....",Toast.LENGTH_SHORT).show();
        //=========================================================================================

        staffList=new DatabaseHelper(this);
        // setup tables

        String url=staffList.getUserData()[2];

        System.out.println(url);

        staffList.getWorkerList(url);


        // Defined Array values to show in ListView
        String[][] values= staffList.getStaffDetails();


        //Declare Recycle view ==============================================================
        RecyclerView myItemList =(RecyclerView)findViewById(R.id.workerListRecycle);
        profileList =new ArrayList<>();
        profileAdapter=new ProfileAdapter(this,profileList);
        mLayoutManager= new LinearLayoutManager(this);
        myItemList.setAdapter(profileAdapter);

        myItemList.setLayoutManager(mLayoutManager);

        //===================================================================================
        // profileList.add(new Profile("W Code","W name", "w Staff Id","devNote"));
        // Load profiles
        for(int i=0;i<values[0].length;i++){

            profileList.add(new Profile(values[2][i],values[3][i], values[4][i],dev_Note_key));

        }

        //===================================================================================

        //==========================================================================================


        Button back = (Button) findViewById(R.id.btn_back_to_note_list);

        back.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {


                        Intent intent = new Intent(WorkerAsignActivity.this,DeliveryNoteRefreshActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        //==========================================================================================
    }
}
