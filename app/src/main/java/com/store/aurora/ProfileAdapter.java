package com.store.aurora;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {


    Context mContest;
    List<Profile> itmList;
    int postion;
    ArrayList<Integer> items;


    public ProfileAdapter(Context mContest, List<Profile> itmList) {
        this.mContest = mContest;
        this.itmList = itmList;
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.profile_layout,parent,false);
        return  new ProfileViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ProfileViewHolder holder, final int position) {

        holder.staffName.setText(itmList.get(position).getTitle());
        holder.detail.setText(itmList.get(position).getSub_title());
        holder.date.setText(itmList.get(position).getDev_date());
        this.postion=position;
        ListView list;


        holder.profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                items = new ArrayList<Integer>();
                items.add(position);

                Integer noteID=position;


                AlertDialog.Builder builder = new AlertDialog.Builder(mContest);
                builder.setMessage("Item Assign to "+itmList.get(position).getTitle());
                builder.setTitle("Worker allocation ");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {
                        items = new ArrayList<Integer>();
                        items.add(position);

                        Integer noteID=position;


                        Intent myIntent = new Intent(mContest, SetPicActivity.class);
                        myIntent.putExtra("id",noteID);
                        myIntent.putExtra("dev_id",itmList.get(position).getDev_note());
                        mContest.startActivity(myIntent);


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
                AlertDialog alertDialog = builder.create();

                // Show the Alert Dialog box
                alertDialog.show();

//                Toast.makeText(v.getContext(),"Item Assign to "+itmList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(mContest, SetPicActivity.class);
//                myIntent.putExtra("id",noteID);
//                myIntent.putExtra("dev_id",itmList.get(position).getDev_note());
//                mContest.startActivity(myIntent);




            }
        });

    }

    @Override
    public int getItemCount() {


        return itmList.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder{

        TextView staffName,detail,date;
        public LinearLayout profileLayout;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            staffName=(TextView)itemView.findViewById(R.id.staffName);
            detail=(TextView)itemView.findViewById(R.id.staffId);
            date=(TextView)itemView.findViewById(R.id.workerType);
            profileLayout=(LinearLayout)itemView.findViewById(R.id.profileList);
        }







    }
}
