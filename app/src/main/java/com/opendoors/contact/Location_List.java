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

public class Location_List extends RecyclerView.Adapter<Location_List.MyViewHolder> {
    Activity mActivity;
    ArrayList<String> Lname ;
    ArrayList<String> Lnum;
    ArrayList<String> Ltime;
    Context context;
    String check;

    public Location_List(Context context, ArrayList<String> Lname,ArrayList<String> Lnum,ArrayList<String> Ltime,String check) {
        this.context = context;
        this.Lname = Lname;
        this.Lnum = Lnum;
        this.Ltime = Ltime;
        this.check = check;


    }

    @Override
    public Location_List.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.locationlist, parent, false);
        Location_List.MyViewHolder vh = new Location_List.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(final Location_List.MyViewHolder holder, final int position) {

        holder.Rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((locationclick) context).Edit(Lname.get(position),Lnum.get(position),position);
            }
        });

        try {
            String[] name = Lname.get(position).split("~");
            holder.Lnum.setText(name[1].toString());
            holder.Lname.setText(name[0].toString());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((locationclick) context).Edit(Lname.get(position),Lnum.get(position),position);
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

        TextView Lname,Lnum;
        ImageButton Rbtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            Lname = itemView.findViewById(R.id.llname);
            Lnum = itemView.findViewById(R.id.llnum);
            Rbtn = itemView.findViewById(R.id.rbtn);
        }
    }
}