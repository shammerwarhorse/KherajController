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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.compose.setContent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.unit.dp;
import androidx.compose.ui.Alignment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class Activate_all_sirens extends AppCompatActivity implements Siren_helper {

    ImageButton Back;
    RecyclerView Activatelist;
    ArrayList<String> LName = new ArrayList<>();
    ArrayList<String> LNum = new ArrayList<>();
    ArrayList<String> Ltime = new ArrayList<>();
    ArrayList<String> SName = new ArrayList<>();
    ArrayList<String> Allsirens = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView Heading,Actlocationnmae,Actlocationnumber,ActSirenname,ActSirencode;
    String check = "1";
    LinearLayout linearLayout;
    RelativeLayout Actsiren,Abortsiren;
    String Alertnumber = "";
    String Alertname = "";
    String Sirencode = "";
    ArrayList<String> Getnumbers = new ArrayList<>();
    Button Activateallsireninone,Abortall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent {
            ActivateAllSirensScreen(onBack = { finish() })
        };
        pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        linearLayout = findViewById(R.id.sirendetails);
        linearLayout.setVisibility(View.GONE);
        Activateallsireninone = findViewById(R.id.activatellinone);
        Actlocationnmae = findViewById(R.id.actlocationname);
        Actlocationnumber = findViewById(R.id.actlocationnumber);
        ActSirenname = findViewById(R.id.actsirenname);
        ActSirencode = findViewById(R.id.actsirencode);
        Abortall = findViewById(R.id.abortallinone);
        Abortsiren = findViewById(R.id.abortsiren);

        Abortsiren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Back = findViewById(R.id.pinactivate);
        Heading = findViewById(R.id.sirenhead);
        Activatelist = findViewById(R.id.activatesirenlist);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.equals("1")) {
                    finish();
                }else {
                    check = "1";
                    Heading.setText("Select Siren");
                    Activatelist.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }

            }
        });
        if (pref.getStringSet("lname", null) != null) {
            HashSet<String> set = new HashSet(LName);
            set = (HashSet<String>) pref.getStringSet("lname", null);
            LName = new ArrayList(set);
            HashSet<String> set1 = new HashSet(LNum);
            set1 = (HashSet<String>) pref.getStringSet("lnum", null);
            LNum = new ArrayList(set1);
            HashSet<String> set2 = new HashSet(LNum);
            set2 = (HashSet<String>) pref.getStringSet("ltime", null);
            Ltime = new ArrayList(set2);
            System.out.println(LName);
            System.out.println(LNum);
            System.out.println(Ltime);
            if (LName.size() > 0) {
                GetList();
            }else {
                Activatelist.setVisibility(View.GONE);
            }

            }
        else {
            Activatelist.setVisibility(View.GONE);
        }
        Activateallsireninone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getString("allsirentime",null) != null) {
                    String timetocheck = pref.getString("allsirentime",null);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    System.out.println("Format dateTime => " + timetocheck);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date startDate = null;
                    try {
                        startDate = simpleDateFormat.parse(timetocheck);
                        Date endDate = simpleDateFormat.parse(formattedDate);

                        long difference = endDate.getTime() - startDate.getTime();
                        if(difference<0)
                        {
                            Date dateMax = simpleDateFormat.parse("24:00:00");
                            Date dateMin = simpleDateFormat.parse("00:00:00");
                            difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
                        }
                        int days = (int) (difference / (1000*60*60*24));
                        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                        int sec = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours) - (1000*60*min)) / (1000);
                        if (min >= 2) {

                            Createactivate();
                        }else {
                            Toast.makeText(Activate_all_sirens.this,"Please try after 2 Minutes!",Toast.LENGTH_LONG).show();

                        }
                        Log.i("log_tag","Hours: "+hours+", Mins: "+min+", Secs: "+sec);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else {
                    Createactivate();
                }

            }
        });
        Abortall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getString("allsirentime",null) != null) {
                    String timetocheck = pref.getString("allsirentime",null);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    System.out.println("Format dateTime => " + timetocheck);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date startDate = null;
                    try {
                        startDate = simpleDateFormat.parse(timetocheck);
                        Date endDate = simpleDateFormat.parse(formattedDate);

                        long difference = endDate.getTime() - startDate.getTime();
                        if(difference<0)
                        {
                            Date dateMax = simpleDateFormat.parse("24:00:00");
                            Date dateMin = simpleDateFormat.parse("00:00:00");
                            difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
                        }
                        int days = (int) (difference / (1000*60*60*24));
                        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                        int sec = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours) - (1000*60*min)) / (1000);
                        if (min > 2) {
                            Toast.makeText(Activate_all_sirens.this,"First activate siren to deactivate",Toast.LENGTH_LONG).show();
                        }else {
                            if (Getnumbers.size() > 0) {
                                for (int ii = 0; ii < Getnumbers.size(); ii++) {
                                    try {
                                        String[] name = Getnumbers.get(ii).split("~");
                                        System.out.println(name[0]+"\n"+name[2]);
                                        sendSMS(name[0],"SALR");
                                        Toast.makeText(Activate_all_sirens.this,"All sirens are aborted!",Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(Activate_all_sirens.this,"First activate siren to deactivate",Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }

                                }
                            }else {
                                Toast.makeText(Activate_all_sirens.this,"First activate siren to deactivate",Toast.LENGTH_LONG).show();
                            }
                          }
                        Log.i("log_tag","Hours: "+hours+", Mins: "+min+", Secs: "+sec);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else {
                    Createactivate();
                }
            }
        });
    }

    private void Createactivate() {
        AlertDialog alertDialog = new AlertDialog.Builder(Activate_all_sirens.this)
//set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle("Confirm Activate")
//set message
                .setMessage("Do you really want to activate all the sirens?")
//set positive button
                .setPositiveButton("DON'T ACTIVATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
//set negative button
                .setNegativeButton("ACTIVATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Permissions.check(Activate_all_sirens.this, Manifest.permission.SEND_SMS, null, new PermissionHandler() {
                            @Override
                            public void onGranted() {
                                if (Getnumbers.size() > 0) {
                                    for (int ii = 0; ii < Getnumbers.size(); ii++) {
                                        String[] name = Getnumbers.get(ii).split("~");
                                        System.out.println(name[0]+"\n"+name[2]);
                                        sendSMS(name[0],"AALR");
                                    }
                                }else {
                                    Toast.makeText(Activate_all_sirens.this,"Contact Empty!",Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                    }
                })
                .show();
    }

    private void GetList() {
        for (int i = 0; i < LNum.size(); i++) {
            SName.clear();
            Allsirens.add(LName.get(i)+"~head");
            if (pref.getStringSet(Ltime.get(i), null) != null) {
                HashSet<String> set = new HashSet(LName);
                set = (HashSet<String>) pref.getStringSet(Ltime.get(i), null);
                SName = new ArrayList(set);
                for (int ii = 0; ii < SName.size(); ii++) {
                    Allsirens.add(SName.get(ii)+"~"+LNum.get(i)+"~"+LName.get(i));
                    Getnumbers.add(LNum.get(i)+"~"+SName.get(ii));
                }

                System.out.println(Allsirens);
            }else {

            }
        }
        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        Activate_all_sirens_lsit customAdapters2 = new Activate_all_sirens_lsit(Activate_all_sirens.this,Allsirens);
        Activatelist.setLayoutManager(layoutManager2);
        Activatelist.setAdapter(customAdapters2);
        Activatelist.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {
        if (check.equals("1")) {
            finish();
        }else {
            check = "1";
            Activatelist.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            Heading.setText("Select Siren");
        }
    }

    @Override
    public void Pass(String name, String number,String method) {
        check = "2";
        Activatelist.setVisibility(View.GONE);
        Heading.setText("Activate Siren");
        linearLayout.setVisibility(View.VISIBLE);
        Actlocationnmae.setText(name);
        Actlocationnumber.setText(number);
       // ActSirenname.setText(sirenname);
       // ActSirencode.setText(sirencode);
        Alertname = name;
        Alertnumber = number;
        //Sirencode = sirencode;
        Getnumbers.add(number);
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

                        Toast.makeText(getBaseContext(), "Generic failure",
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
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        System.out.println("Format dateTime => " + formattedDate);
        editor.putString("allsirentime",formattedDate);
        editor.apply();

    }

    @Composable
    fun ActivateAllSirensScreen(onBack: () -> Unit) {
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
                        text = "Activate all sirens",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: Activate all sirens logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Activate All In One")
            }
        }
    }
}