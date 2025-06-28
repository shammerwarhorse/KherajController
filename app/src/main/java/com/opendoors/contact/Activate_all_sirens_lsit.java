package com.opendoors.contact;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Activate_all_sirens_lsit extends RecyclerView.Adapter<Activate_all_sirens_lsit.MyViewHolder> {
    Activity mActivity;
    ArrayList<String> Lname ;

    Context context;

    public Activate_all_sirens_lsit(Context context, ArrayList<String> Lname) {
        this.context = context;
        this.Lname = Lname;



    }

    @Override
    public Activate_all_sirens_lsit.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.showallsirens, parent, false);
        Activate_all_sirens_lsit.MyViewHolder vh = new Activate_all_sirens_lsit.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(final Activate_all_sirens_lsit.MyViewHolder holder, final int position) {

        String[] name = Lname.get(position).split("~");
        if (name[1].equals("head")) {
            holder.Lname.setVisibility(View.GONE);
            holder.Lnum.setVisibility(View.GONE);
            holder.lineview.setVisibility(View.GONE);
            holder.heading.setVisibility(View.VISIBLE);
            holder.heading.setText(name[0]);
            holder.Rbtn.setVisibility(View.GONE);
            holder.relativeLayout.setBackgroundColor(Color.LTGRAY);
        }else {
            holder.Rbtn.setVisibility(View.VISIBLE);
            holder.relativeLayout.setBackgroundColor(Color.TRANSPARENT);
            holder.heading.setVisibility(View.GONE);
            holder.Lname.setVisibility(View.VISIBLE);
            holder.Lnum.setVisibility(View.VISIBLE);
            holder.lineview.setVisibility(View.VISIBLE);
            try {
                holder.Lnum.setText(name[1]);
                holder.Lname.setText(name[0]);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       holder.Rbtn.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return Lname.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Lname,Lnum,heading;
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
