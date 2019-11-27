package com.store.aurora;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ModelViewHolder> {


    Context mContest;
    List<Model> itmList;
    int postion;
    ArrayList<Integer> items;
    DatabaseHelper  itemDb;


    public ModelAdapter(Context mContest, List<Model> itmList) {
        this.mContest = mContest;
        this.itmList = itmList;
    }

    @Override
    public ModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.note_layout,parent,false);
        return  new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ModelViewHolder holder, final int position) {

        holder.deliveryNote.setText(itmList.get(position).getTitle());
        holder.detail.setText(itmList.get(position).getSub_title());
        holder.date.setText(itmList.get(position).getDev_date());

        this.postion=position;
        ListView list;


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                items = new ArrayList<Integer>();
                items.add(position);
                itemDb=new DatabaseHelper(mContest);


                Integer noteID=position;

                String noteKey=itmList.get(noteID).getDev_note_key();


                itemDb.syncDeliveryNoteItemList(noteKey);

                Toast.makeText(v.getContext(),"Item Click", Toast.LENGTH_SHORT).show();

                final ProgressDialog progressDoalog = new ProgressDialog(mContest);
                progressDoalog.setMax(100);
                progressDoalog.setMessage("Item Loading....");
                ;
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //System.out.println(itmList.get(noteID).getDev_note_key()+ "Delivery Note .....");
                        //Toast.makeText(v.getContext(),"Item Click", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(mContest, ItemListViewActivity.class);
                        // myIntent.putExtra("id",noteID);
                        mContest.startActivity(myIntent);
                        progressDoalog.dismiss();

                    }
                }, 3000);







            }
        });

    }




    @Override
    public int getItemCount() {


        return itmList.size();
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder{

        TextView deliveryNote,detail,date;
        public LinearLayout linearLayout;

        public ModelViewHolder(View itemView) {
            super(itemView);
            deliveryNote=(TextView)itemView.findViewById(R.id.deliveryNoteId);
            detail=(TextView)itemView.findViewById(R.id.item_name);
            date=(TextView)itemView.findViewById(R.id.date);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.itemSet);
        }





    }
}
