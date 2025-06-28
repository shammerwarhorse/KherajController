package com.opendoors.contact;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


    public class Siren_Listview extends RecyclerView.Adapter<Siren_Listview.MyViewHolder> {
        Activity mActivity;
        ArrayList<String> Lname ;
        int check = 8;
        Context context;

        public Siren_Listview(Context context, ArrayList<String> Lname) {
            this.context = context;
            this.Lname = Lname;



        }

        @Override
        public Siren_Listview.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.locationlist, parent, false);
            Siren_Listview.MyViewHolder vh = new Siren_Listview.MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @Override
        public void onBindViewHolder(final Siren_Listview.MyViewHolder holder, final int position) {

        holder.Rbtn.setVisibility(View.GONE);

        String[] name = Lname.get(position).split("~");
            try {
                holder.Lnum.setText(name[1].toString());
                holder.Lname.setText(name[0].toString());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      check = position;
                      notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            return Lname.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageButton Rbtn;
            TextView Lname,Lnum;

            public MyViewHolder(View itemView) {
                super(itemView);
                Lname = itemView.findViewById(R.id.llname);
                Lnum = itemView.findViewById(R.id.llnum);
                Rbtn = itemView.findViewById(R.id.rbtn);
            }
        }
    }
