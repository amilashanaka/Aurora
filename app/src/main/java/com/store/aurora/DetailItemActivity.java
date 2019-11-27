package com.store.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailItemActivity extends AppCompatActivity {

    private  String tr_key;
    DatabaseHelper itmdb;
    ListView itemListView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        itmdb=new DatabaseHelper(this);
        itemListView= (ListView) findViewById(R.id.item_detail_list);


        //=====================================================================================
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null) {
            tr_key = (String) b.get("tr_key");



            Toast.makeText(getApplicationContext(), "Item Loading"+tr_key, Toast.LENGTH_LONG).show();

            String [] values= itmdb.getItemDetailByTrkey(tr_key);
            //String [] values2=itmdb.getItemDetailByTrkey(tr_key);

//            for(int i=0;i<15;i++){
//                values[i]= "#"+values2[i];
//            }

            if(values==null) {

               values = new String[]{"Fail", "iPhone", "WindowsMobile",
                        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                        "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                        "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                        "Android", "iPhone", "WindowsMobile"};
            }

           // Toast.makeText(getApplicationContext(), "Item Loading"+ values[0], Toast.LENGTH_LONG).show();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);
            itemListView.setAdapter(adapter);



        }

        //=========================================================================================

        Button btn_pack = (Button) findViewById(R.id.btn_go_back_item_list);

        btn_pack.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(DetailItemActivity.this, ItemListViewActivity.class);
                        startActivity(intent);
                    }
                }
        );


    }
}
