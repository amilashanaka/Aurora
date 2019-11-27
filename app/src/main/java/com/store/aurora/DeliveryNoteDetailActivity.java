package com.store.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeliveryNoteDetailActivity extends AppCompatActivity {
    ListView listView ;
    DatabaseHelper getAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_note_detail);


        // Data Declare
        getAll=new DatabaseHelper(this);
        listView = (ListView) findViewById(R.id.list);


       String[][] fields= getAll.getPickItemDetails();


        //====================================================================

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
              Integer j = (Integer) b.get("id");

            String[] values = new String[] { "DeliveryNote = "+fields[0][j],
                    "DeliveryNoteCustomerKey = "+fields[1][j],
                    "DeliveryNoteCustomerName="+fields[2][j],
                    "DeliveryNoteCustomerDate="+fields[3][j],
                    "DeliveryNoteEstimatedWeight="+fields[4][j]+"Kg",
                    " DeliveryNoteID="+fields[0][j],
                    " DeliveryNoteKey="+fields[6][j],
                    " DeliveryNoteNumberOrderedParts="+fields[7][j],
                    "  DeliveryNoteStoreKey="+fields[8][j],
                    " DeliveryNoteID="+fields[9][j]

            };

            //            StoreCode.setText(" StoreCode="+fields[10][j]);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);


            listView.setAdapter(adapter);

            //========================= Get Item list

          String noteKey=fields[6][j];

          // Load Item List to the Table
               if(getAll.getNetStat()){

                   getAll.syncDeliveryNoteItemList(noteKey);
               }
               else{
                   getAll.clearItems();
                   getAll.clearDeliveryNotes();
                   Intent intent = new Intent(DeliveryNoteDetailActivity.this, MainActivity.class);
                   startActivity(intent);
                   finish();


               }


        }


        //==========================================================================================



        //implement Handler to wait for 3 seconds and then update UI means update value of TextView


        //==========================================================================================

        Button itemload = (Button) findViewById(R.id.btn_load_items);

        itemload.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        Toast.makeText(getApplicationContext(), "Item Loading", Toast.LENGTH_LONG).show();



                            Intent intent = new Intent(DeliveryNoteDetailActivity.this, ItemListViewActivity.class);
                            startActivity(intent);
                            finish();


                    }
                }
        );

        //==========================================================================================




    }
}
