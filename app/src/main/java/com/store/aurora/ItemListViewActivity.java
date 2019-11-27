package com.store.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemListViewActivity extends AppCompatActivity {



    // Decalear Variables
    //================================================================
    DatabaseHelper itemdb;
    private LinearLayoutManager item_LayoutManager;
    private List<Item> item_List;
    private ItemAdapter itemAdapter;
    private  String dev_Note_key;
    Switch toggle;
    String key="where status='0'";
    RecyclerView myItemList;
    //================================================================

    public String getDev_Note_key() {
        return dev_Note_key;
    }

    public void setDev_Note_key(String dev_Note_key) {
        this.dev_Note_key = dev_Note_key;
    }

    public Item getItem(int position)
    {

        return  item_List.get(position);
    }

    //======================================================================

    public void setPick(Item val)
    {
        String tr_key =val.getInventory_transaction_key();
        System.out.println("Ready to pick .....");

        if(itemdb.getNetStat())
        {
            itemdb.pickAction(tr_key);
            Toast.makeText(getApplicationContext(), "Item Picked", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(ItemListViewActivity.this,ItemListViewActivity.class);
            startActivity(intent);
            dashBoard();
        }
        else{

            Intent intent = new Intent(ItemListViewActivity.this,NoNetworkActivity.class);
            startActivity(intent);

        }
        String key=" where status ='0'";

        String[][] values_to_pick= itemdb.getItemDetail_remove_current(key);
        itemdb.syncDeliveryNoteItemList(getDev_Note_key());
        itemLoad(values_to_pick);
    }

//======================================================================

    public void itemNotPick(Item val)
    {
        String tr_key =val.getInventory_transaction_key();

        System.out.println("Ready to pick ........");

        String key=" where status ='0'";

        String[][] values_to_pick= itemdb.getItemDetail_remove_current(key);
        itemdb.syncDeliveryNoteItemList(getDev_Note_key());
        itemLoad(values_to_pick);

        if(itemdb.getNetStat()) {
            Toast.makeText(getApplicationContext(), "Item Not Available", Toast.LENGTH_LONG).show();
            dashBoard();
            Intent intent = new Intent(ItemListViewActivity.this, ItemNotAvilableActivity.class);
            intent.putExtra("tr_key", tr_key);
            startActivity(intent);

        }else{

            Intent intent = new Intent(ItemListViewActivity.this,NoNetworkActivity.class);
            startActivity(intent);

        }

    }

    //======================================================================

    public DatabaseHelper getItemdb() {
        return itemdb;
    }

    public void setItemdb(DatabaseHelper itemdb) {
        this.itemdb = itemdb;
    }

    //=================================================


    public void dashBoard(){

        itemdb.syncDeliveryNoteItemList(getDev_Note_key());
        int[] dash_values= this.itemdb.get_to_all_info();

        TextView status =(TextView) findViewById(R.id.item_status);
        TextView itm_collected =(TextView) findViewById(R.id.item_collected);
        TextView outstk =(TextView) findViewById(R.id.out_of_stock);
        TextView waiting =(TextView) findViewById(R.id.lbl_wating);
        TextView note_key =(TextView) findViewById(R.id.not_key);
        TextView note_weight =(TextView) findViewById(R.id.note_weight);
        TextView note_store =(TextView) findViewById(R.id.note_store);
        TextView note_order_state =(TextView) findViewById(R.id.note_order_state);
        TextView note_order_date =(TextView) findViewById(R.id.note_order_date);
        TextView note_parts =(TextView) findViewById(R.id.note_item_count);
        TextView cu_name =(TextView) findViewById(R.id.note_cu_name);

        final ProgressBar bar =(ProgressBar) findViewById(R.id.all_process);

        bar.setMax(dash_values[1]);
        bar.setProgress(dash_values[0]);

        if( bar.getProgress()==bar.getMax()){

            status.setText("Action : completed");

        }

        itm_collected.setText(dash_values[0]+"/"+dash_values[1]);
        outstk.setText(""+dash_values[3]);
        waiting.setText(""+dash_values[2]);

        String dev_note_name=itemdb.getDeliliveryNoteDataBykey(getDev_Note_key())[0];
        note_key.setText(dev_note_name);
        note_weight.setText(itemdb.getDeliliveryNoteDataBykey(getDev_Note_key())[1]+" Kg");
        note_store.setText(itemdb.getDeliliveryNoteDataBykey(getDev_Note_key())[2]);
        note_order_state.setText(itemdb.getDeliliveryNoteDataBykey(getDev_Note_key())[3]);
        note_order_date.setText(itemdb.getDeliliveryNoteDataBykey(getDev_Note_key())[4]);
        note_parts.setText(itemdb.getDeliliveryNoteDataBykey(getDev_Note_key())[5]);
        cu_name.setText(itemdb.getDeliliveryNoteDataBykey(getDev_Note_key())[6]);
    }



    //loading Items to recycle


    public void itemLoad( String[][] values){


        // Declare

        myItemList =(RecyclerView)findViewById(R.id.item_list_view_recycle);

        item_List=new ArrayList<>();
        itemAdapter=new ItemAdapter(this,item_List);
        item_LayoutManager= new LinearLayoutManager(this);
        myItemList.setAdapter(itemAdapter);

        myItemList.setLayoutManager(item_LayoutManager);

        //=========================================================================================


        System.out.println(" Item Loading..for ="+ values[0].length);

        Boolean stat= false;
        if(itemdb.getNetStat()){


        for(int i=0;i<values[0].length;i++){

            String toPick=values[6][i]+"/"+values[5][i];

            if(values[12][i].contains("1")){

                stat=true;

            }

            item_List.add(new Item(values[3][i],values[4][i],values[5][i],values[6][i],values[7][i],values[8][i],values[9][i],values[10][i],toPick,stat,values[1][i],values[2][i],values[11][i]));


            stat= false;
        }
        }

    }


    //=====================================================================================

    //==============================================================================================


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(ItemListViewActivity.this,DeliveryNoteRefreshActivity.class);
        startActivity(intent);
    }
    //==================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list_view);

        if (InternetConnection.checkConnection(this)) {
            Toast.makeText(getApplicationContext(),"Connect To aurora.systems ",Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(getApplicationContext(),"Can't Reach aurora.systems ",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ItemListViewActivity.this,NoNetworkActivity.class);
            startActivity(intent);
            finish();
        }

        // Item Load ==============================================================================


        toggle = (Switch) findViewById(R.id.showall);
        itemdb=new DatabaseHelper(this);

        String[][] values= itemdb.getItemDetail();
        if(values!=null){
            setDev_Note_key(values[0][0]);
            System.out.println("Set Delivery Note Key to ="+this.getDev_Note_key());
            if(itemdb.getNetStat()){
                itemdb.syncDeliveryNoteItemList(getDev_Note_key());
            }
            else{

                finish();
            }


        }

        String[][] values_to_pick= itemdb.getItemDetail_remove_current(key);

        if( values_to_pick!=null){

            toggle.setText("Pending");
            itemLoad(values_to_pick);
            dashBoard();
        }


        Button barcode = (Button) findViewById(R.id.btn_item_scn_barcode);


        barcode.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {


                        Toast.makeText(getApplicationContext(), "Scan Barcode ....", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ItemListViewActivity.this,BarCodeReadActivity.class);
                        startActivity(intent);

                    }
                }
        );


        //===========================================================================================


        Button home = (Button) findViewById(R.id.btn_home);

        home.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        Toast.makeText(getApplicationContext(), "Sync.....", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(ItemListViewActivity.this,WelcomeActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
        );



        //===========================================================================================

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                System.out.println("Right Action ");

                Toast.makeText(getApplicationContext(), " Item Not Available"+getItem(viewHolder.getAdapterPosition()).getInventory_transaction_key(), Toast.LENGTH_LONG).show();
                itemNotPick(getItem(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(myItemList);


        //==========================================================================================


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                System.out.println("Start user list");

                Toast.makeText(getApplicationContext(), "Item Picked"+getItem(viewHolder.getAdapterPosition()).getInventory_transaction_key(), Toast.LENGTH_LONG).show();
                setPick(getItem(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(myItemList);


        //==========================================================================================

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                dashBoard();

                if(toggle.isChecked()){
                    Toast.makeText(getApplicationContext(), "Show Completed", Toast.LENGTH_LONG).show();
                    toggle.setText("Completed");
                    String key=" where status ='1'";

                    String[][] values_to_pick= itemdb.getItemDetail_remove_current(key);
                    itemLoad(values_to_pick);
                }
                else {

                    Toast.makeText(getApplicationContext(), "Show Pending", Toast.LENGTH_LONG).show();

                    String key=" where status ='0'";
                    toggle.setText("Pending");
                    String[][] values_to_pick= itemdb.getItemDetail_remove_current(key);
                    itemLoad(values_to_pick);

                }

            }



        });

//==========================================================================================


    }


}
