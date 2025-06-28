package com.opendoors.contact;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;


public class NewEditlocationlist extends RecyclerView.Adapter<NewEditlocationlist.MyViewHolder> {
        Activity mActivity;
        ArrayList<String> Lname ;
        ArrayList<String> Lnum;
        ArrayList<String> Ltime;
        String action;
        Context context;

        public NewEditlocationlist(Context context, ArrayList<String> Lname,ArrayList<String> Lnum,ArrayList<String> Ltime,String action) {
            this.context = context;
            this.Lname = Lname;
            this.Lnum = Lnum;
            this.Ltime = Ltime;
            this.action = action;


        }

        @Override
        public NewEditlocationlist.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.editlocationview, parent, false);
            NewEditlocationlist.MyViewHolder vh = new NewEditlocationlist.MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @Override
        public void onBindViewHolder(final NewEditlocationlist.MyViewHolder holder, final int position) {

            try {
                String[] name = Lname.get(position).split("~");
                try {
                    holder.Lnum.setText(name[1].toString());
                    holder.Lname.setText(name[0].toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.Save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (action.equals("siren")) {
                        if (holder.Lname.getText().toString().length() > 1 && holder.Lnum.getText().toString().length() == 4) {
                            AlertDialog alertDialog = new AlertDialog.Builder(context)
//set icon
                                    .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                                    .setTitle("Confirm update")
//set message
                                    .setMessage("Are you sure want to update this Siren")
//set positive button
                                    .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
//set negative button
                                    .setNegativeButton("UPDATE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ((locationclick) context).UpdateLocation(holder.Lname.getText().toString(),holder.Lnum.getText().toString(),position,"update",action);

                                        }
                                    })
                                    .show();

                        }else {
                            Toast.makeText(context,"Please enter valid details!",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        if (holder.Lname.getText().toString().length() > 1 && holder.Lnum.getText().toString().length() == 10) {
                            AlertDialog alertDialog = new AlertDialog.Builder(context)
//set icon
                                    .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                                    .setTitle("Confirm update")
//set message
                                    .setMessage("Are you sure want to update this location")
//set positive button
                                    .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
//set negative button
                                    .setNegativeButton("UPDATE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ((locationclick) context).UpdateLocation(holder.Lname.getText().toString(),holder.Lnum.getText().toString(),position,"update",action);

                                        }
                                    })
                                    .show();

                        }else {
                            Toast.makeText(context,"Please enter valid details!",Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });

            holder.Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (action.equals("siren")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(context)
//set icon
                                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                                .setTitle("Confirm Delete")
//set message
                                .setMessage("Are you sure want to detele this Siren")
//set positive button
                                .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
//set negative button
                                .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ((locationclick) context).UpdateLocation(holder.Lname.getText().toString(),holder.Lnum.getText().toString(),position,"delete",action);
                                    }
                                })
                                .show();
                    }else {
                        AlertDialog alertDialog = new AlertDialog.Builder(context)
//set icon
                                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                                .setTitle("Confirm Delete")
//set message
                                .setMessage("Are you sure want to detele this location")
//set positive button
                                .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
//set negative button
                                .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ((locationclick) context).UpdateLocation(holder.Lname.getText().toString(),holder.Lnum.getText().toString(),position,"delete",action);
                                    }
                                })
                                .show();
                    }

                }
            });


        }

        @Override
        public int getItemCount() {
            return Lname.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            EditText Lname,Lnum;
            RelativeLayout Save,Delete;

            public MyViewHolder(View itemView) {
                super(itemView);
                Lname = itemView.findViewById(R.id.newlocationname);
                Lnum = itemView.findViewById(R.id.newlocationnumber);
                Save = itemView.findViewById(R.id.newsavelocation);
                Delete = itemView.findViewById(R.id.newlocationdelete);

            }
        }
    }
