package com.store.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    DatabaseHelper getAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Data Declare
        getAll=new DatabaseHelper(this);

        System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
        //====================================================================
        TextView DeliveryNote= (TextView)findViewById(R.id.deliveryNote);
        TextView DeliveryNoteCustomerKey= (TextView)findViewById(R.id.deliveryNoteCustomerKey);
        TextView DeliveryNoteCustomerName= (TextView)findViewById(R.id.DeliveryNoteCustomerName);
        TextView DeliveryNoteCustomerDate= (TextView)findViewById(R.id.DeliveryNoteCustomerDate);
        TextView DeliveryNoteEstimatedWeight= (TextView)findViewById(R.id.deliveryWeight);
        TextView DeliveryNoteID= (TextView)findViewById(R.id.deliveryNoteId);
        TextView DeliveryNoteKey= (TextView)findViewById(R.id.deliveryNoteKey);
        TextView DeliveryNoteNumberOrderedParts= (TextView)findViewById(R.id.DeliveryNoteNumberOrderedParts);
        TextView DeliveryNoteStoreKey= (TextView)findViewById(R.id.DeliveryNoteStoreKey);
        TextView DeliveryNoteType= (TextView)findViewById(R.id.DeliveryNoteType);
        TextView StoreCode= (TextView)findViewById(R.id.StoreCode);
        TextView StoreName= (TextView)findViewById(R.id.StoreName);
        TextView sku= (TextView)findViewById(R.id.sku);


        //====================================================================

        //====================================================================

        String[][] values= getAll.getPickItemDetails();


        DeliveryNote.setText(values[0][0]);


        //====================================================================

        //====================================================================

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            Integer j = (Integer) b.get("id");
            DeliveryNote.setText("DeliveryNote = "+values[0][j]);
            DeliveryNoteCustomerKey.setText("DeliveryNoteCustomerKey = "+values[1][j]);
            DeliveryNoteCustomerName.setText("DeliveryNoteCustomerName="+values[2][j]);
            DeliveryNoteCustomerDate.setText("DeliveryNoteCustomerDate="+values[3][j]);
            DeliveryNoteEstimatedWeight.setText("DeliveryNoteEstimatedWeight="+values[4][j]+"Kg");
            DeliveryNoteID.setText(" DeliveryNoteID="+values[0][j]);
            DeliveryNoteKey.setText(" DeliveryNoteKey="+values[6][j]);
            DeliveryNoteNumberOrderedParts.setText(" DeliveryNoteNumberOrderedParts="+values[7][j]);
            DeliveryNoteStoreKey.setText("  DeliveryNoteStoreKey="+values[8][j]);
            DeliveryNoteType.setText(" DeliveryNoteID="+values[9][j]);
            StoreCode.setText(" StoreCode="+values[10][j]);


            //========================= Get Item list

            String noteKey=values[6][j];


           // getAll.syncDeliveryNoteItemList(noteKey);
            StoreName.setText("The Part Sku="+getAll.getItemDetail()[1][0]);

        }

        //====================================================================




    }
}
