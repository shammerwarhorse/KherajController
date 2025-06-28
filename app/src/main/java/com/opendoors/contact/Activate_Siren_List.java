package com.opendoors.contact;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Activate_Siren_List extends RecyclerView.Adapter<Activate_Siren_List.MyViewHolder> {
        Activity mActivity;
        ArrayList<String> Lname ;
        ArrayList<String> selected;
        Context context;
        boolean check = false;

        public Activate_Siren_List(Context context, ArrayList<String> Lname,ArrayList<String> selected) {
            this.context = context;
            this.Lname = Lname;
            this.selected = selected;
        }
       @Override
        public Activate_Siren_List.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.showallsirens, parent, false);
            Activate_Siren_List.MyViewHolder vh = new Activate_Siren_List.MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @Override
        public void onBindViewHolder(final Activate_Siren_List.MyViewHolder holder, final int position) {

            if (selected.contains(Lname.get(position))) {
                holder.heading.setChecked(true);
            }else {
                holder.heading.setChecked(false);
            }

            String[] name = Lname.get(position).split("~");
            if (name[1].equals("head")) {
                holder.Lname.setVisibility(View.GONE);
                holder.Lnum.setVisibility(View.GONE);
                holder.lineview.setVisibility(View.GONE);
                holder.heading.setVisibility(View.VISIBLE);
                holder.heading.setText(name[0]);
              }else {
                holder.Lname.setVisibility(View.GONE);
                holder.Lnum.setVisibility(View.GONE);
                holder.lineview.setVisibility(View.GONE);
                holder.heading.setVisibility(View.VISIBLE);
                holder.heading.setText(name[0]);

            }

            holder.Rbtn.setVisibility(View.GONE);

            holder.heading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String locaname = name[0];
                    String locnumber = name[1];
                    ((Siren_helper) context).Pass(Lname.get(position),locnumber,"single");
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String locaname = name[0];
                    String locnumber = name[1];
                    ((Siren_helper) context).Pass(Lname.get(position),locnumber,"single");


                }
            });


        }

        @Override
        public int getItemCount() {
            return Lname.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView Lname,Lnum;
            RadioButton heading;
            View lineview;
            RelativeLayout relativeLayout;
            ImageButton Rbtn;

            public MyViewHolder(View itemView) {
                super(itemView);
                relativeLayout = itemView.findViewById(R.id.bgcolor);
                Lname = itemView.findViewById(R.id.llname);
                heading = itemView.findViewById(R.id.locationheadingshow);
                Lnum = itemView.findViewById(R.id.llnum);
                lineview = itemView.findViewById(R.id.lineview);
                Rbtn = itemView.findViewById(R.id.rbtn);

            }
        }
    }
