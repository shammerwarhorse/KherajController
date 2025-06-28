package com.opendoors.contact;

import androidx.activity.compose.setContent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.lazy.LazyColumn;
import androidx.compose.foundation.lazy.items;
import androidx.compose.material.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.unit.dp;
import androidx.compose.ui.Alignment;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Event_log extends AppCompatActivity {
    private RecyclerViewAdapter adapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private Button selectButton;
    RecyclerView recyclerView;
    ImageButton Back;
    ArrayList<String> deletearraylist = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    RelativeLayout relativeLayout;
    LinearLayout Editview;
    ImageButton Editbtn;
    boolean check = false;
    String FCMTOKEN = "";
    JSONArray jsonArray = new JSONArray();
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent {
            EventLogScreen()
        };
    }
    @Composable
    fun EventLogScreen() {
        var eventList by remember { mutableStateOf(arrayListOf("Event 1", "Event 2", "Event 3")) }
        Column(modifier = Modifier.fillMaxSize()) {
            Card(
                elevation = 10.dp,
                backgroundColor = MaterialTheme.colors.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    IconButton(onClick = { finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "Event Log",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { /* TODO: Edit logic */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(eventList) { event ->
                    Text(
                        text = event,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    Divider()
                }
            }
            Button(
                onClick = { /* TODO: Select logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Select")
            }
        }
    }

    public void getlog() {
        if (pref.getString("fcmtoken",null) != null) {
            System.out.println("Fcm token not found" + pref.getString("fcmtoken",null));
            FCMTOKEN = pref.getString("fcmtoken",null);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FCMTOKEN);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        System.out.println(ds);
                        arrayList.add(ds.getKey().toString()+"%"+ds.getValue().toString());
                    }
                    populateRecyclerView();
                    onClickEvent();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else  {
            System.out.println("Fcm token not found");
        }
    }

    private void populateRecyclerView() {
        if (arrayList.size() > 0) {

        }else  {
            Toast.makeText(Event_log.this,"Log is Empty!",Toast.LENGTH_LONG).show();
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.eventlog);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
        dialog.dismiss();
    }
    private void onClickEvent() {
        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(Event_log.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm Delete")
                        .setMessage("Do you really want to Delete log?")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int ii) {
                                SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
                                System.out.println("selectedRows"+selectedRows);
                                dialog.show();
                                if (selectedRows.size() > 0) {
                                    //Loop to all the selected rows array
                                    for (int i = (selectedRows.size() - 1); i >= 0; i--) {

                                        //Check if selected rows have value i.e. checked item
                                        if (selectedRows.valueAt(i)) {

                                            //remove the checked item
                                            System.out.println("selectedRows"+selectedRows.keyAt(i));
                                            String[] value = arrayList.get(selectedRows.keyAt(i)).split("%");
                                            System.out.println("Key = "+value[0]);
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference myRef = database.getReference(FCMTOKEN);
                                            myRef.child(value[0]).removeValue();
                                            arrayList.remove(selectedRows.keyAt(i));
                                        }
                                    }
                                    dialog.dismiss();
                                    Intent push = new Intent(Event_log.this,Event_log.class);
                                    startActivity(push);
                                }
                            }
                        })
                        .show();

            }
        });
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check the current text of Select Button
                if (selectButton.getText().toString().equals(getResources().getString(R.string.select_all))) {

                    //If Text is Select All then loop to all array List items and check all of them
                    for (int i = 0; i < arrayList.size(); i++)
                        adapter.checkCheckBox(i, true);

                    //After checking all items change button text
                    selectButton.setText(getResources().getString(R.string.deselect_all));
                } else {
                    //If button text is Deselect All remove check from all items
                    adapter.removeSelection();

                    //After checking all items change button text
                    selectButton.setText(getResources().getString(R.string.select_all));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent push = new Intent(Event_log.this,Start_page.class);
        startActivity(push);
    }

    public static class InternetConnection {

        /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet


                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true;
                }
            }
            return false;
        }
    }
}
