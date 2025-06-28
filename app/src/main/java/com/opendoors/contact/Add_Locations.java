package com.opendoors.contact;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.compose.setContent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.lazy.LazyColumn;
import androidx.compose.foundation.lazy.items;
import androidx.compose.material.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.unit.dp;
import androidx.compose.ui.Alignment;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Add_Locations extends AppCompatActivity implements locationclick{

    RelativeLayout Addlocation,Savelocation,Canecllocation,Editlocation,EdiCancellocation,Savelocationedit,Cancellocationedit,Deletelocationedit,Requestlist,Adduser,Canceluser,Addsirenbtn,Savesiren,Cancelsiren,Sirensaveedit,Sirencanceledit,Sirendelete,Neweditsiren;
    LinearLayout Addview,Firstview,EditView,SirenView,Sireneditview,Newlocationeditview;
    ScrollView Editlocationview,Userview;
    ImageButton Back,Editlocationimage;
    TextView NoLocation,Heading,Editheading,Sirenedit,Requsttext,Authnew,Nosirenfound,Savepin,Savesirentxtcolor,Usertxtxcolor,RegisterClr,Neweditsirenbtn;
    EditText Locationname,Locationnumber,Locationnameedit,Locationnumedit,Slotnumbwe,Slotmobilenumber,Sirenname,Sirennumber,Sirennamedit,Sirennumberedit;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ArrayList<String> LName = new ArrayList<>();
    ArrayList<String> LNum = new ArrayList<>();
    ArrayList<String> Ltime = new ArrayList<>();
    ArrayList<String> SName = new ArrayList<>();
    RecyclerView Locationlist,Sirenlist,Newlocationlayout;
    int getpos = 0;
    String Clickname = "";
    String Clicknum = "";
    String backcheck = "";
    String clicktime = "sirenlist";
    String ts;
    String newsirename = "";
    String newsirenumber = "";
    int sirenpos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        setContent {
            AddLocationsScreen(onBack = { finish() })
        };
    }

    @Composable
    fun AddLocationsScreen(onBack: () -> Unit) {
        var locations by remember { mutableStateOf(listOf("Location 1", "Location 2")) }
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
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "Locations",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { /* TODO: Edit logic */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(locations) { location ->
                    Text(location, modifier = Modifier.padding(16.dp))
                    Divider()
                }
            }
            Button(
                onClick = { /* TODO: Add location logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Add Location")
            }
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        // ---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                switch (getResultCode()) {

                    case Activity.RESULT_OK:

                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                        Toast.makeText(getBaseContext(), "Enter valid number!",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:

                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:

                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:

                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                switch (getResultCode()) {

                    case Activity.RESULT_OK:

                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:

                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();

        String numto = phoneNumber.replaceAll(" ","");
        sms.sendTextMessage(numto, null, message, sentPI, deliveredPI);

    }

    @Override
    public void onBackPressed() {
        if (backcheck.equals("")) {
            finish();
        }else if (backcheck.equals("1")) {
            Firstview.setVisibility(View.VISIBLE);
            Sireneditview.setVisibility(View.GONE);
            Editlocationview.setVisibility(View.GONE);
            EditView.setVisibility(View.GONE);
            final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
            layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
            Location_List customAdapters2 = new Location_List(Add_Locations.this,LName,LNum,Ltime,"loc");
            Locationlist.setLayoutManager(layoutManager2);
            Locationlist.setAdapter(customAdapters2);
            Addlocation.setVisibility(View.VISIBLE);
            Editlocationimage.setVisibility(View.VISIBLE);
            backcheck = "";
        }else if (backcheck.equals("2")) {
            Editlocationview.setVisibility(View.VISIBLE);
            Sireneditview.setVisibility(View.GONE);
            Userview.setVisibility(View.GONE);
            backcheck = "1";
        }else if (backcheck.equals("3")) {
            Editlocationview.setVisibility(View.VISIBLE);
            Sireneditview.setVisibility(View.GONE);
            SirenView.setVisibility(View.GONE);
            Heading.setText("Locations");
            backcheck = "1";
        }else if (backcheck.equals("4")) {
            SName.clear();
            Sireneditview.setVisibility(View.GONE);
            Editlocationview.setVisibility(View.VISIBLE);
            backcheck = "1";
            Heading.setText("Locations");
            Editheading.setText("Locations");
            Sirenedit.setText("Sirens");
            if (pref.getStringSet(clicktime, null) != null) {
                HashSet<String> set = new HashSet(LName);
                set = (HashSet<String>) pref.getStringSet(clicktime, null);
                SName = new ArrayList(set);
                Nosirenfound.setVisibility(View.GONE);
                System.out.println(SName);
                final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
                layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
                Siren_Listview customAdapters2 = new Siren_Listview(Add_Locations.this,SName);
                Sirenlist.setLayoutManager(layoutManager2);
                Sirenlist.setAdapter(customAdapters2);
                Sirenlist.setVisibility(View.VISIBLE);
            }else {
                Nosirenfound.setVisibility(View.VISIBLE);
                Sirenlist.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void Edit(String name, String number, int position) {
        SName.clear();
        Editlocationimage.setVisibility(View.GONE);
        backcheck = "1";
        getpos = position;
        Clickname = name;
        Clicknum = number;
        String val = String.valueOf(LName.size());
        Requsttext.setText("You can request for list of users already authorized for "+val+" locations . You will receive user list as SMS Message");
        Authnew.setText("Authorize New User for "+val+" Locations");
        Firstview.setVisibility(View.GONE);
        System.out.println(name+"\n"+number+"\n"+position+"\n"+Ltime.get(position));
        clicktime = "sirenlist";
        Heading.setText("Locations");
        Editheading.setText("Locations");
        Sirenedit.setText("Sirens");
        Editlocationview.setVisibility(View.VISIBLE);
        if (pref.getStringSet(clicktime, null) != null) {

            HashSet<String> set = new HashSet(LName);
            set = (HashSet<String>) pref.getStringSet(clicktime, null);
            SName = new ArrayList(set);
            if (SName.size() > 0) {
                Nosirenfound.setVisibility(View.GONE);
                System.out.println(SName);
                final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
                layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
                Siren_Listview customAdapters2 = new Siren_Listview(Add_Locations.this,SName);
                Sirenlist.setLayoutManager(layoutManager2);
                Sirenlist.setAdapter(customAdapters2);
                Sirenlist.setVisibility(View.VISIBLE);
                Neweditsirenbtn.setTextColor(getResources().getColor(R.color.black));
                Neweditsiren.setEnabled(true);
            }else  {
                Nosirenfound.setVisibility(View.VISIBLE);
                Neweditsirenbtn.setTextColor(getResources().getColor(R.color.grey));
                Neweditsiren.setEnabled(false);
            }


        }else {
            Neweditsirenbtn.setTextColor(getResources().getColor(R.color.grey));
            Neweditsiren.setEnabled(false);
            Nosirenfound.setVisibility(View.VISIBLE);
            Sirenlist.setVisibility(View.GONE);
        }


    }

    @Override
    public void SirenEdit(String name, String number, int position) {
        Sireneditview.setVisibility(View.VISIBLE);
        backcheck = "4";
        sirenpos = position;
        newsirename = name;
        newsirenumber = number;
        Firstview.setVisibility(View.GONE);
        System.out.println(name+"\n"+number+"\n"+position);
        Heading.setText("Edit Siren");
        Editlocationview.setVisibility(View.GONE);
        Sirennamedit.setText(name);
        Sirennumberedit.setText(number);


    }

    @Override
    public void UpdateLocation(String name, String number, int position, String action,String check) {
        if (check.equals("siren")) {
            if (action.equals("update")) {
                SName.set(position,name+"~"+number);
                Toast.makeText(Add_Locations.this,"Siren Updated!",Toast.LENGTH_LONG).show();
            }else if (action.equals("delete")){

                SName.remove(position);
                Toast.makeText(Add_Locations.this,"Siren Deleted!",Toast.LENGTH_LONG).show();
            }

            HashSet<String> set = new HashSet(SName);
            editor.putStringSet(clicktime, set).apply();
            editor.commit();

            final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
            layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
            NewEditlocationlist customAdapters2 = new NewEditlocationlist(Add_Locations.this,SName,LNum,Ltime,action);
            Locationlist.setLayoutManager(layoutManager2);
            Locationlist.setAdapter(customAdapters2);
        }else {
            if (action.equals("update")) {
                LName.set(position,name+"~"+number);
                LNum.set(position,number);
                Toast.makeText(Add_Locations.this,"Location Updated!",Toast.LENGTH_LONG).show();
            }else if (action.equals("delete")){
                LName.remove(position);
                LNum.remove(position);
                Toast.makeText(Add_Locations.this,"Location Deleted!",Toast.LENGTH_LONG).show();
            }

            HashSet<String> set = new HashSet(LName);
            HashSet<String> set1 = new HashSet(LNum);
            editor.putStringSet("lname", set).apply();
            editor.putStringSet("lnum", set1).apply();
            editor.commit();

            final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            Location_List customAdapters = new Location_List(Add_Locations.this,LName,LNum,Ltime,"loc");
            Locationlist.setLayoutManager(layoutManager);
            Locationlist.setAdapter(customAdapters);

            final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
            layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
            NewEditlocationlist customAdapters2 = new NewEditlocationlist(Add_Locations.this,LName,LNum,Ltime,action);
            Newlocationlayout.setLayoutManager(layoutManager2);
            Newlocationlayout.setAdapter(customAdapters2);

            if (LName.size() < 1) {
                editor.putStringSet(clicktime, null).apply();
                editor.putString("register",null);
                editor.commit();
            }else {

            }
        }

    }
}

