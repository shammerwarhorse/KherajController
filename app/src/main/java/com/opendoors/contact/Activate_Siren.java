package com.opendoors.contact;

import androidx.activity.compose.setContent;
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
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Activate_Siren : AppCompatActivity implements Siren_helper,Siren_selector {

    ImageButton Back;
    RecyclerView Activatelist,ListSiren;
    ArrayList<String> LName = new ArrayList<>();
    ArrayList<String> LNum = new ArrayList<>();
    ArrayList<String> Ltime = new ArrayList<>();
    ArrayList<String> SName = new ArrayList<>();
    ArrayList<String> Allsirens = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView Heading,Actlocationnmae,Actlocationnumber,Lastactivated,ProceedText,Selectedsirenname,Selectedsirencode,Sirenactivatedtext,Timertime,Lasttext,aborttext;
    String check = "1";
    LinearLayout linearLayout;
    RelativeLayout Actsiren,Abortsiren,Proceed,Activatelastsiren;
    String Alertnumber = "";
    String Alertname = "";
    String Sirencode = "";
    ArrayList<String> Getnumbers = new ArrayList<>();
    String smsnumber = "";
    ArrayList<String> Sirentodeactivate = new ArrayList<>();
    ArrayList<String> Sirentime = new ArrayList<>();
    ArrayList<String> Sirentimedate = new ArrayList<>();
    ArrayList<String> Eventlog = new ArrayList<String>();
    ArrayList<String> Sirenlist = new ArrayList<>();
    ArrayList<String> Selectedsiren = new ArrayList<>();
    ArrayList<String> selectedlocation = new ArrayList<>();
    ArrayList<String> selectedlocationlatest = new ArrayList<>();
    ArrayList<String> loc1 = new ArrayList<>();
    ArrayList<String> loc2 = new ArrayList<>();
    WebView webView;
    ArrayList<Person> list = new ArrayList<>();
    RadioButton Selectalllocationbtn;
    JSONArray jsonArray = new JSONArray();
    boolean on = false;
    CountDownTimer timer;
    int globalTime = 120;
    String FCMTOKEN = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent {
        ActivateSirenScreen(onBack = { finish() })
    };
}

@Composable
fun ActivateSirenScreen(onBack: () -> Unit) {
    var sirens by remember { mutableStateOf(listOf("Siren 1", "Siren 2")) }
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
                    text = "Select Siren",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(sirens) { siren ->
                Text(siren, modifier = Modifier.padding(16.dp))
                Divider()
            }
        }
        Button(
            onClick = { /* TODO: Activate selected siren logic */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Activate Siren")
        }
    }
}
    private void Createactivate() {
        AlertDialog alertDialog = new AlertDialog.Builder(Activate_Siren.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm Activate")
                .setMessage("Do you really want to activate siren?")
                .setPositiveButton("DON'T ACTIVATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("ACTIVATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Permissions.check(Activate_Siren.this, Manifest.permission.SEND_SMS, null, new PermissionHandler() {
                            @Override
                            public void onGranted() {
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                                String formattedDate = df.format(c.getTime());
                                Lastactivated.setText(formattedDate);
                                editor.putString("activatedtimes",formattedDate);
                                editor.commit();
                                for (int i = 0; i < selectedlocation.size(); i++) {
                                    String[] name = selectedlocation.get(i).split("~");
                                    sendSMS(name[1],Selectedsirencode.getText().toString());
                                }
                                HashSet<String> set5 = new HashSet(selectedlocation);
                                editor.putStringSet("abortsirenlist", set5).apply();
                                linearLayout.setVisibility(View.VISIBLE);
                                ListSiren.setVisibility(View.GONE);
                                Actsiren.setVisibility(View.GONE);
                                Createtimetext();
                                startTimer();
                                Savelog("activated");

                            }
                        });

                    }
                })
                .show();
    }

    private void Savelog(String name) {
        String s = pref.getString("nameofloc","");
        String s1 = pref.getString("numberofloc","");
        String s2 = pref.getString("nameofsiren","");
        String s3 = pref.getString("numberofsiren","");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dfs = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
        String formattedDates = dfs.format(c.getTime());
        String Data = "Siren ~ "+name+"\nAt Location ~ "+s+"\nLocation Number ~ "+s1+"\nSiren Name ~ "+s2+"\n Siren Code ~ "+s3+"\nTime ~"+formattedDates;

        Eventlog.add(Data);

        // Write a message to the database
        String ts = String.valueOf(System.currentTimeMillis());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FCMTOKEN).child(ts);

        myRef.setValue(Data);



        //
    }

    @Override
    protected void onDestroy() {
        HashSet<String> set = new HashSet(Eventlog);
        pref.edit().putStringSet("neweventrtry", set).apply();
        super.onDestroy();
    }



    private void startTimer() {
        aborttext.setTextColor(getResources().getColor(R.color.black));
        Abortsiren.setEnabled(true);
        Activatelastsiren.setEnabled(false);
        Lasttext.setTextColor(getResources().getColor(R.color.grey));
        Timertime.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(globalTime * 1000,1000) {
            @Override
            public void onTick(long l) {

               long hours = globalTime / 3600;
               long minutes = (globalTime % 3600) / 60;
               long seconds = globalTime % 60;

               String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                globalTime -= 1;
                Timertime.setText(timeString);
                System.out.println(String.valueOf(globalTime));
            }

            @Override
            public void onFinish() {
                HashSet<String> set = new HashSet(Eventlog);
                pref.edit().putStringSet("neweventrtry", set).apply();
                aborttext.setTextColor(getResources().getColor(R.color.grey));
                Abortsiren.setEnabled(false);
                globalTime = 120;
                Activatelastsiren.setEnabled(true);
                Lasttext.setTextColor(getResources().getColor(R.color.black));
                Sirenactivatedtext.setVisibility(View.GONE);
                Timertime.setVisibility(View.GONE);
                editor.putString("activatedtimes",null);
                editor.commit();
                finish();
            }
        }.start();
    }


    private void Createtimetext() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());
        String myTime = formattedDate;
        int minsToAdd = 2;
        Date date = new Date();
        date.setTime((((Integer.parseInt(myTime.split(":")[0]))*60 + (Integer.parseInt(myTime.split(":")[1])))+ date.getTimezoneOffset())*60000);
        System.out.println(date.getHours() + ":"+date.getMinutes());
        date.setTime(date.getTime()+ minsToAdd *60000);
        System.out.println(date.getHours() + ":"+date.getMinutes());
        String tt = date.getHours() + ":"+date.getMinutes();
        String text = "Siren activated at "+myTime.toString()+", Activation disabled until "+tt.toString();
        System.out.println(text);
        Sirenactivatedtext.setVisibility(View.VISIBLE);
        Sirenactivatedtext.setText(text);
        editor.putString("sirenredtext",text);
        editor.commit();
    }

    private void GetList() {
        for (int i = 0; i < LNum.size(); i++) {
            SName.clear();
            Allsirens.add(LName.get(i)+"~head~"+LNum.get(i));
        }
        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        Activate_Siren_List customAdapters2 = new Activate_Siren_List(Activate_Siren.this,Allsirens,selectedlocation);
        Activatelist.setLayoutManager(layoutManager2);
        Activatelist.setAdapter(customAdapters2);
        Activatelist.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        HashSet<String> set = new HashSet(Eventlog);
        pref.edit().putStringSet("neweventrtry", set).apply();
        if (pref.getString("activatedtimes",null) != null) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            System.out.println(pref.getString("activatedtimes",null));
            Date startDate = null;
            try {
                startDate = simpleDateFormat.parse(pref.getString("activatedtimes",null));
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
                    if (check.equals("1")) {
                        finish();
                    }else {
                        ListSiren.setVisibility(View.GONE);
                        Lastactivated.setText("");
                        check = "1";
                        Activatelist.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                        Heading.setText("Select Location");
                        Proceed.setVisibility(View.VISIBLE);
                        Selectalllocationbtn.setVisibility(View.VISIBLE);
                    }
                }else {
                    finish();
                }

                Log.i("log_tag","Hours: "+hours+", Mins: "+min+", Secs: "+sec);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            if (check.equals("1")) {
                finish();
            }else {
                ListSiren.setVisibility(View.GONE);
                Lastactivated.setText("");
                check = "1";
                Activatelist.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                Heading.setText("Select Location");
                Proceed.setVisibility(View.VISIBLE);
                Selectalllocationbtn.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void Pass(String name, String number,String method) {

            if (selectedlocation.contains(name)) {
                selectedlocation.remove(name);
            }else {
                selectedlocation.add(name);
            }
            System.out.println(selectedlocation);
            if (selectedlocation.size() == LName.size()) {
                Selectalllocationbtn.setTextColor(getResources().getColor(R.color.black));

                Selectalllocationbtn.setChecked(true);
            }else {
                Selectalllocationbtn.setTextColor(getResources().getColor(R.color.grey));

                Selectalllocationbtn.setChecked(false);
            }


       if (selectedlocation.size() > 0) {
            ProceedText.setTextColor(getResources().getColor(R.color.black));
           Proceed.setEnabled(true);
        }else {
            ProceedText.setTextColor(getResources().getColor(R.color.grey));
           Proceed.setEnabled(false);
        }

     check = "2";
     Heading.setText("Activate Siren");
     linearLayout.setVisibility(View.GONE);
     Sirencode = "All Sirens";
     Getnumbers.add(number);
     smsnumber = number;

        if (Sirentodeactivate != null) {
            if (Sirentodeactivate.contains(number)) {
                int a = Sirentodeactivate.indexOf(number);
                Lastactivated.setText(Sirentimedate.get(a));
            }else {
                Lastactivated.setText("");
            }
        }else {
            Lastactivated.setText("");
        }

        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        Activate_Siren_List customAdapters2 = new Activate_Siren_List(Activate_Siren.this,Allsirens,selectedlocation);
        Activatelist.setLayoutManager(layoutManager2);
        Activatelist.setAdapter(customAdapters2);
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

            String numto = smsnumber.replaceAll(" ","");
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);




    }

    private void Savetime() {
        SName.clear();
        if (pref.getStringSet("sirenlist", null) != null) {
            HashSet<String> setd = new HashSet(LName);
            setd = (HashSet<String>) pref.getStringSet("sirenlist", null);
            SName = new ArrayList(setd);
            System.out.println(SName);
            for (int i = 0; i < SName.size(); i++) {
                String[] value = SName.get(i).split("~");
                HashSet<String> set = new HashSet(Sirentodeactivate);
                HashSet<String> set1 = new HashSet(Sirentime);
                HashSet<String> set2 = new HashSet(Sirentimedate);
                editor.putStringSet("Sirentodeactivate", set).apply();
                editor.putStringSet("Sirentime", set1).apply();
                editor.putStringSet("Sirentimedate", set2).apply();


            }
        }

    }

    @Override
    public void selected(String values,String v) {
        String[] name = v.split("~");
        Selectedsirenname.setText(name[0]);
        Selectedsirencode.setText(name[1]);
        editor.putString("nameofsiren",name[0]);
        editor.putString("numberofsiren",name[1]);
        editor.commit();
        if (Selectedsiren.contains(values)) {
            Selectedsiren.remove(values);
        }else {
            Selectedsiren.add(values);
        }
        if (Selectedsiren.size() > 0) {
            Actsiren.setVisibility(View.VISIBLE);
        }else {
            Actsiren.setVisibility(View.GONE);
        }
    System.out.println(Selectedsiren);
    }
}