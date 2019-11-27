package com.store.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetPicActivity extends AppCompatActivity {

    DatabaseHelper getStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pic);

        //=================================================================

        getStaff=new DatabaseHelper(this);

        //======================================================================

        String[][] values= getStaff.getStaffDetails();



        //====================================================================

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            Integer j = (Integer) b.get("id");
            j=j-1;

            String staff_id=values[1][j];

            String note=(String) b.get("dev_id");

            String url= getStaff.getUserData()[2];
            System.out.println("staff Id="+staff_id+"     Delevery Note Id="+note);
            System.out.println(url);

            if(getStaff.setPicker(url,note,staff_id)>0){
                Toast.makeText(getApplicationContext(), "Parameter Set In Server", Toast.LENGTH_LONG).show();
                System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGG");
            }

            getStaff.getDeliveryNotes();


            Intent intent2 = new Intent(SetPicActivity.this,DeliveryNoteRefreshActivity.class);

            startActivity(intent2);



        }




        //====================================================================
    }
}
