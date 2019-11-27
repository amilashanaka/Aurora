package com.store.aurora;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    Context mContest;
    List<Item> itmList;
    int postion;
    ArrayList<Integer> items_array;
    DatabaseHelper itemdb;


    public ItemAdapter(Context mContest, List<Item> itmList) {
        this.mContest = mContest;
        this.itmList = itmList;
    }



    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.item_layout,parent,false);
        return  new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {


        itemdb=new DatabaseHelper(mContest);

        holder.part_ref.setText(itmList.get(position).getPart_reference());
        holder.itm_loc.setText(itmList.get(position).getLocation_key());
        holder.itm_des.setText(itmList.get(position).getPkg_des());
        holder.itm_note.setText(itmList.get(position).getItem_note());
        // Set The Item Pick
        holder.itm_check.setChecked(itmList.get(position).getItem_pick());
        holder.itm_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!holder.itm_check.isChecked()){




                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContest);
                    builder.setMessage("Do you want Cancel Operation ?");
                    builder.setTitle("Aurora Picking App");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which)
                        {
                            String [] values=itemdb.getItemDetailByTrkey(itmList.get(position).getInventory_transaction_key());
                            Toast.makeText(mContest.getApplicationContext(), "Cancel pick action"+values[12], Toast.LENGTH_LONG).show();
                            itemdb.update_cancel_picked_item(values[12],itmList.get(position).getInventory_transaction_key());

                        }

                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.cancel();
                        }
                    });

                    // Create the Alert dialog
                    androidx.appcompat.app.AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();

                }
                else{

                    Toast.makeText(mContest.getApplicationContext(), "bulk pick action"+itmList.get(position).getPart_reference(), Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContest);
                    builder.setTitle("Input No of Items");

                    // Set up the input
                    final EditText input = new EditText(mContest);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );
                    builder.setView(input);

// Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String m_Text = input.getText().toString();
                            int qty=itemdb.validatePickingItems(itmList.get(position).getInventory_transaction_key(),m_Text);

                              if(qty>=0) {

                                  itemdb.bulckPickAction(itmList.get(position).getInventory_transaction_key(), m_Text);
                                  Toast.makeText(mContest.getApplicationContext(), "Item "+m_Text +"is Picked", Toast.LENGTH_LONG).show();
                              }
                              else{

                                  Toast.makeText(mContest.getApplicationContext(), m_Text +"is Invalid quantity", Toast.LENGTH_LONG).show();
                              }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }




            }
        });

        this.postion=position;
        ListView list;

        holder.item_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                items_array = new ArrayList<Integer>();
                items_array.add(position);

                Integer noteID=position;


                Toast.makeText(v.getContext(),"Item Click", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(mContest, DetailItemActivity.class);
              myIntent.putExtra("tr_key",itmList.get(noteID).getInventory_transaction_key());
              mContest.startActivity(myIntent);

            }
        });

    }






    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public Toast part_loc;
        TextView part_ref,itm_des,itm_note,itm_loc;
        CheckBox itm_check;
        public LinearLayout item_Layout;
        public ItemViewHolder( View itemView) {
            super(itemView);
            part_ref=(TextView)itemView.findViewById(R.id.part_ref);
            itm_des=(TextView)itemView.findViewById(R.id.itm_des);
            itm_note=(TextView)itemView.findViewById(R.id.part_note);
            itm_loc=(TextView)itemView.findViewById(R.id.part_loc);

            itm_check=(CheckBox)itemView.findViewById(R.id.itm_check);

            item_Layout=(LinearLayout)itemView.findViewById(R.id.item_Card);



        }
    }

    @Override
    public int getItemCount() {


        return itmList.size();
    }


}
