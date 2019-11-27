package com.store.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ItemNotAvilableActivity extends AppCompatActivity {

    DatabaseHelper itemdb;
    int action_key;
    int result;


    String tr_key;


    // Getter ans setter

    public String getTr_key() {
        return tr_key;
    }

    public void setTr_key(String tr_key) {
        this.tr_key = tr_key;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_not_avilable);

        // Declare Database

        itemdb=new DatabaseHelper(this);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String val = (String) b.get("tr_key");
            setTr_key(val);
            System.out.println("Tr Key ="+getTr_key());
        }

        //==========================================================================================

        Button btn_update = (Button) findViewById(R.id.btn_update_result);

        btn_update.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        int status=0;

                        RadioButton out_stk =(RadioButton)findViewById(R.id.rad_out_of_stk);
                        RadioButton rad_waiting =(RadioButton)findViewById(R.id.rad_wating);


                        if(out_stk.isChecked()){

                            Toast.makeText(getApplicationContext(),"Item Set To Out Of The Stock", Toast.LENGTH_LONG).show();
                            result=itemdb.not_avilable_Action(getTr_key(),1);
                        }

                        if(rad_waiting.isChecked()){

                            Toast.makeText(getApplicationContext(),"Item Set To Waiting", Toast.LENGTH_LONG).show();
                            result=itemdb.not_avilable_Action(getTr_key(),0);
                        }

                        if(result>0){

                            Intent intent = new Intent(ItemNotAvilableActivity.this, ItemListViewActivity.class);
                            startActivity(intent);
                        }

                    }
                }
        );

        //==========================================================================================



    }
}
