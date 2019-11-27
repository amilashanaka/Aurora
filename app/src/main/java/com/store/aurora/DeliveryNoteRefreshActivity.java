package com.store.aurora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DeliveryNoteRefreshActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    TextView textView;
// =============================================================================================

    private LinearLayoutManager mLayoutManager;
    private List<Model> itemList;
    private ModelAdapter mAdapter;
    // Get Object Of database
    DatabaseHelper getMydata;
    RecyclerView myItemList;
    //==============================================================================================

    public Model getModel(int postion)
    {

        return itemList.get(postion);
    }

    //==============================================================================================

    public void itemLoad(String[][] values , RecyclerView myItemList){


        itemList =new ArrayList<>();
        mAdapter=new ModelAdapter(this,itemList);
        mLayoutManager= new LinearLayoutManager(this);
        myItemList.setAdapter(mAdapter);

        myItemList.setLayoutManager(mLayoutManager);
        for(int i=0;i<values[0].length;i++){

            itemList.add(new Model(values[0][i],values[1][i]+" Kg   " +values[2][i] +"Items", values[3][i].substring(0,10),values[4][i] ));

        }


    }


    //==============================================================================================

    public void showData(Model m){

        View v1= new View(this);
        Toast.makeText(v1.getContext(),"Assign Delivery note"+m.getTitle(),Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent(DeliveryNoteRefreshActivity.this,WorkerAsignActivity.class);
        intent2.putExtra("dev_id",m.getDev_note_key());
        startActivity(intent2);

    }

    //==============================================================================================


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(DeliveryNoteRefreshActivity.this,WelcomeActivity.class);
        startActivity(intent);
    }
//==================================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_note_refresh);

        if (InternetConnection.checkConnection(this)) {


        } else {
            Toast.makeText(getApplicationContext(),"Can't Reach aurora.systems ",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(DeliveryNoteRefreshActivity.this,NoNetworkActivity.class);
            startActivity(intent);
            finish();
        }



        //================================== Declare  ========================================

        getMydata = new DatabaseHelper(this);

        String[][] values= getMydata.getPickItemData();
        this.myItemList =(RecyclerView)findViewById(R.id.itemList);

        //==========================================================================================


        itemLoad(values,myItemList);

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

                        Intent intent = new Intent(DeliveryNoteRefreshActivity.this,WelcomeActivity.class);
                        startActivity(intent);
                        finish();




                    }
                }
        );


        //==========================================================================================

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);

        // implement setOnRefreshListener event on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMydata.logOutCheck();
                getMydata.logOutCheck();

                    if(getMydata.getNetStat()){
                        getMydata.getDeliveryNotes();
                    }else{
                        getMydata.clearDeliveryNotes();
                        Toast.makeText(getApplicationContext(), "Api Key Not Found", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DeliveryNoteRefreshActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }







                // implement Handler to wait for 3 seconds and then update UI means update value of TextView
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String[][] values= getMydata.getPickItemData();
                        RecyclerView myItemList =(RecyclerView)findViewById(R.id.itemList);
                        itemLoad(values, myItemList);

                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Data Received.....", Toast.LENGTH_LONG).show();


                    }
                }, 1000);
            }
        });
    }
}
