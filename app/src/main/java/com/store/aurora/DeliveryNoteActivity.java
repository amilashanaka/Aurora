package com.store.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DeliveryNoteActivity extends AppCompatActivity {

    // =============================================================================================

    private LinearLayoutManager mLayoutManager;
    private List<Model> itemList;
    private ModelAdapter mAdapter;
    // Get Object Of database
    DatabaseHelper getMydata;

    //==============================================================================================

    public Model getModel(int postion)
    {

        return itemList.get(postion);
    }



    //==============================================================================================

    public void showData(Model m){

        View v1= new View(this);
        Toast.makeText(v1.getContext(),"Assign Delivery note"+m.getTitle(),Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent(DeliveryNoteActivity.this, WorkerAsignActivity.class);
        intent2.putExtra("dev_id",m.getDev_note_key());
        startActivity(intent2);

    }

    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_note);

        Toast.makeText(getApplicationContext(), "Data Received.....", Toast.LENGTH_LONG).show();

        //================================== Declare  ========================================

        getMydata = new DatabaseHelper(this);

        getMydata.logOutCheck();

        if(!getMydata.getNetStat()){

            Toast.makeText(getApplicationContext(), "Wrong API", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(DeliveryNoteActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }


        RecyclerView myItemList =(RecyclerView)findViewById(R.id.itemList);
        String[][] values= getMydata.getPickItemData();
        //==========================================================================================

        itemList =new ArrayList<>();
        mAdapter=new ModelAdapter(this,itemList);
        mLayoutManager= new LinearLayoutManager(this);
        myItemList.setAdapter(mAdapter);

        myItemList.setLayoutManager(mLayoutManager);
        //==========================================================================================
       if(getMydata.getNetStat()){

           for(int i=0;i<values[0].length;i++){

               itemList.add(new Model(values[0][i],values[1][i]+" Kg   " +values[2][i] +"Items", values[3][i].substring(0,10),values[4][i] ));

           }

       }

       //===========================================================================================

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                System.out.println("Start user list");

                showData(getModel(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(myItemList);





        //==========================================================================================

        Button logOut = (Button) findViewById(R.id.btn_back_Button);

        logOut.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        Toast.makeText(getApplicationContext(), "Back to Main Menu...", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(DeliveryNoteActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                            finish();




                    }
                }
        );


        //==========================================================================================




    }
}
