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

import java.util.ArrayList;

public class Select_Siren_List extends RecyclerView.Adapter<Select_Siren_List.MyViewHolder> {
    Activity mActivity;
    ArrayList<String> Lname ;
    ArrayList<String> Getlist = new ArrayList<>();
    int check = 8;
    Context context;

    public Select_Siren_List(Context context, ArrayList<String> Lname) {
        this.context = context;
        this.Lname = Lname;




    }

    @Override
    public Select_Siren_List.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sirenlistr, parent, false);
        Select_Siren_List.MyViewHolder vh = new Select_Siren_List.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(final Select_Siren_List.MyViewHolder holder, final int position) {

        if (check == position) {
            holder.Lname.setTextColor(context.getColor(R.color.black));
            holder.Lnum.setTextColor(context.getColor(R.color.black));
            holder.radioButton.setChecked(true);
        }else {
            holder.Lname.setTextColor(context.getColor(R.color.grey));
            holder.Lnum.setTextColor(context.getColor(R.color.grey));
            holder.radioButton.setChecked(false);
        }

        String[] name = Lname.get(position).split("~");

        holder.Lname.setText(name[0]);
        holder.Lnum.setText(name[1]);

        holder.Rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (Getlist.contains(name[1])) {
                   holder.radioButton.setChecked(false);
                   Getlist.remove(name[1]);
               }else {
                   Getlist.add(name[1]);
                   holder.radioButton.setChecked(true);
               }
                ((Siren_selector) context).selected(name[1],Lname.get(position));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = position;
                notifyDataSetChanged();
                if (Getlist.contains(name[1])) {
                    holder.radioButton.setChecked(false);
                    Getlist.remove(name[1]);
                }else {
                    Getlist.add(name[1]);
                    holder.radioButton.setChecked(true);
                }
                ((Siren_selector) context).selected(name[1],Lname.get(position));
            }
        });

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = position;
                notifyDataSetChanged();
                if (Getlist.contains(name[1])) {
                    holder.radioButton.setChecked(false);
                    Getlist.remove(name[1]);
                }else {
                    Getlist.add(name[1]);
                    holder.radioButton.setChecked(true);
                }
                ((Siren_selector) context).selected(name[1],Lname.get(position));
            }
        });


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
        RadioButton radioButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.bgcolor);
            Lname = itemView.findViewById(R.id.llname);
            heading = itemView.findViewById(R.id.locationheadingshow);
            Lnum = itemView.findViewById(R.id.llnum);
            lineview = itemView.findViewById(R.id.lineview);
            Rbtn = itemView.findViewById(R.id.rbtn);
            radioButton = itemView.findViewById(R.id.radio);

        }
    }
}
