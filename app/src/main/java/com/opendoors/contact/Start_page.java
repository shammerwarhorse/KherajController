package com.opendoors.contact;

import androidx.activity.compose.setContent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class Start_page extends AppCompatActivity {

    RelativeLayout relativeLayout,helplayout,changepinview,Exitapp,locationview,Activatesirenbtn,Activatallsirens;
    LinearLayout linearLayout,linearLayout1;
    Button one,two,three,four,five,six,seven,eight,nine,zero,numa,numc;
    ArrayList<String> key = new ArrayList<>();
    TextView Pinview,Activatetxtclr,Locationtxt,Eventstxt,helptxt,Changetxt;
    String text = "";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Newpin = "";
    ArrayList<String> Sirenlist = new ArrayList<>();
    boolean checkpin = false;
    boolean go = true;
    CountDownTimer timer;
    int globalTime = 120;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getApplicationContext(), AppService.class);
        startService(intent);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) {
                        //handle token error
                        return;
                    }

                   String strAppToken = task.getResult();
                    System.out.println(strAppToken);
                    editor.putString("fcmtoken" ,strAppToken);
                    editor.commit();

                });
    setContent {
        StartPageScreen()
    };
        pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        Activatesirenbtn = findViewById(R.id.activatesirenbtn);
        Locationtxt = findViewById(R.id.startlocation);
        Eventstxt = findViewById(R.id.starteventtxt);
        helptxt = findViewById(R.id.starthelptxt);
        Changetxt = findViewById(R.id.startchangetxtx);
        Activatetxtclr = findViewById(R.id.activatesirentxtclr);
        if (pref.getStringSet("sirenlist", null) != null) {
            HashSet<String> set = new HashSet(Sirenlist);
            set = (HashSet<String>) pref.getStringSet("sirenlist", null);
            Sirenlist = new ArrayList(set);
            if (Sirenlist.size() > 0) {
                Activatesirenbtn.setEnabled(true);
                Activatetxtclr.setTextColor(getResources().getColor(R.color.black));
            }else {
                Activatesirenbtn.setEnabled(false);
                Activatetxtclr.setTextColor(getResources().getColor(R.color.grey));
            }
        }else {
            Activatesirenbtn.setEnabled(false);
            Activatetxtclr.setTextColor(getResources().getColor(R.color.grey));
        }

        if (pref.getString("pinchanged",null) != null) {
            checkpin = true;
            Newpin = pref.getString("pinchanged",null);
            System.out.println(Newpin);
        }
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
                    Locationtxt.setTextColor(getResources().getColor(R.color.black));
                    Eventstxt.setTextColor(getResources().getColor(R.color.black));
                    helptxt.setTextColor(getResources().getColor(R.color.black));
                    Changetxt.setTextColor(getResources().getColor(R.color.black));
                    go = true;
                    editor.putString("activatedtimes",null);
                    editor.commit();
                }else {
                    Locationtxt.setTextColor(getResources().getColor(R.color.grey));
                    Eventstxt.setTextColor(getResources().getColor(R.color.grey));
                    helptxt.setTextColor(getResources().getColor(R.color.grey));
                    Changetxt.setTextColor(getResources().getColor(R.color.grey));
                   go = false;
                    int duration = 60 * min + sec;
                    int get = 120-duration;
                    globalTime = get;
                    startTimer();
                }

                Log.i("log_tag","Hours: "+hours+", Mins: "+min+", Secs: "+sec);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Activatallsirens = findViewById(R.id.activateallsirens);
        Activatallsirens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(Start_page.this,Activate_all_sirens.class);
                startActivity(go);
            }
        });
        Activatallsirens.setVisibility(View.GONE);

        Activatesirenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(Start_page.this,Activate_Siren.class);
                startActivity(go);
            }
        });
        helplayout = findViewById(R.id.helpbtn);
        locationview = findViewById(R.id.locationview);
        locationview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (go == false) {

                }else {
                    Intent go = new Intent(Start_page.this,Add_Locations.class);
                    startActivity(go);
                }

            }
        });
        helplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (go == false) {

                }else {
                    Intent go = new Intent(Start_page.this,Help.class);
                    startActivity(go);
                }

            }
        });
        changepinview = findViewById(R.id.changepinview);
        Exitapp = findViewById(R.id.exitapp);
        Exitapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });
        changepinview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (go == false) {

                }else {
                    Intent go = new Intent(Start_page.this,Change_Pin.class);
                    startActivity(go);
                }

            }
        });
        relativeLayout = findViewById(R.id.event);
        linearLayout1 = findViewById(R.id.numbersbox);
        linearLayout = findViewById(R.id.buttonslist);
        Pinview = findViewById(R.id.pinview);
        Intent check = getIntent();
        if (check.getStringExtra("open") != null) {
            if (check.getStringExtra("open").equals("open")) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.GONE);
            }else {
                linearLayout.setVisibility(View.GONE);
            }
        }else {
            linearLayout.setVisibility(View.GONE);
        }
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        zero = findViewById(R.id.zero);
        numa = findViewById(R.id.btnA);
        numc = findViewById(R.id.btnC);
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add("0");
                text = text+"#";
                Pinview.setTextColor(Color.BLACK);
                Pinview.setText(text);
                if (key.size() == 4) {
                    PIN();
                }
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add("1");
                text = text+"#";
                Pinview.setTextColor(Color.BLACK);
                Pinview.setText(text);
                if (key.size() == 4) {
                    PIN();
                }
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add("2");
                text = text+"#";
                Pinview.setTextColor(Color.BLACK);
                Pinview.setText(text);
                if (key.size() == 4) {
                    PIN();
                }
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add("3");
                text = text+"#";
                Pinview.setTextColor(Color.BLACK);
                Pinview.setText(text);
                if (key.size() == 4) {
                    PIN();
                }
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add("4");
                text = text+"#";
                Pinview.setTextColor(Color.BLACK);
                Pinview.setText(text);
                if (key.size() == 4) {
                    PIN();
                }
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add("5");
                text = text+"#";
                Pinview.setTextColor(Color.BLACK);
                Pinview.setText(text);
                if (key.size() == 4) {
                    PIN();
                }
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add("6");
                text = text+"#";
                Pinview.setTextColor(Color.BLACK);
                Pinview.setText(text);
                if (key.size() == 4) {
                    PIN();
                }
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add("7");
                text = text+"#";
                Pinview.setTextColor(Color.BLACK);
                Pinview.setText(text);
                if (key.size() == 4) {
                    PIN();
                }
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add("8");
                text = text+"#";
                Pinview.setTextColor(Color.BLACK);
                Pinview.setText(text);
                if (key.size() == 4) {
                    PIN();
                }
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add("9");
                text = text+"#";
                Pinview.setTextColor(Color.BLACK);
                Pinview.setText(text);
                if (key.size() == 4) {
                    PIN();
                }
            }
        });
        numa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add("a");
                text = text+"#";
                Pinview.setTextColor(Color.BLACK);
                Pinview.setText(text);
                if (key.size() == 4) {
                    PIN();
                }
            }
        });
        numc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add("c");
                text = text+"#";
                Pinview.setTextColor(Color.BLACK);
                Pinview.setText(text);
                if (key.size() == 4) {
                    PIN();
                }
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (go == false) {

                }else {
                    Intent go = new Intent(Start_page.this,Event_log.class);
                    startActivity(go);
                }


            }
        });


    }

    private void startTimer() {
        timer = new CountDownTimer(globalTime * 1000,1000) {
            @Override
            public void onTick(long l) {
                     globalTime -= 1;
               }

            @Override
            public void onFinish() {
                globalTime = 120;
                Locationtxt.setTextColor(getResources().getColor(R.color.black));
                Eventstxt.setTextColor(getResources().getColor(R.color.black));
                helptxt.setTextColor(getResources().getColor(R.color.black));
                Changetxt.setTextColor(getResources().getColor(R.color.black));
                go = true;
                editor.putString("activatedtimes",null);
                editor.commit();
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    Locationtxt.setTextColor(getResources().getColor(R.color.black));
                    Eventstxt.setTextColor(getResources().getColor(R.color.black));
                    helptxt.setTextColor(getResources().getColor(R.color.black));
                    Changetxt.setTextColor(getResources().getColor(R.color.black));
                    go = true;
                    editor.putString("activatedtimes",null);
                    editor.commit();
                }else {
                    Locationtxt.setTextColor(getResources().getColor(R.color.grey));
                    Eventstxt.setTextColor(getResources().getColor(R.color.grey));
                    helptxt.setTextColor(getResources().getColor(R.color.grey));
                    Changetxt.setTextColor(getResources().getColor(R.color.grey));
                    go = false;
                    int duration = 60 * min + sec;
                    int get = 120-duration;
                    globalTime = get;
                    startTimer();
                }

                Log.i("log_tag","Hours: "+hours+", Mins: "+min+", Secs: "+sec);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (pref.getStringSet("sirenlist", null) != null) {
            HashSet<String> set = new HashSet(Sirenlist);
            set = (HashSet<String>) pref.getStringSet("sirenlist", null);
            Sirenlist = new ArrayList(set);
            if (Sirenlist.size() > 0) {
                Activatesirenbtn.setEnabled(true);
                Activatetxtclr.setTextColor(getResources().getColor(R.color.black));
            }else {
                Activatesirenbtn.setEnabled(false);
                Activatetxtclr.setTextColor(getResources().getColor(R.color.grey));
            }
        }else {
            Activatesirenbtn.setEnabled(false);
            Activatetxtclr.setTextColor(getResources().getColor(R.color.grey));
        }
    }

    public void save() {
        editor.putString("open","open");
        editor.commit();
    }

    public void PIN() {
        if (checkpin == true) {
            char[] chat = Newpin.toCharArray();
            String a = String.valueOf(chat[0]);
            String b = String.valueOf(chat[1]);
            String c = String.valueOf(chat[2]);
            String d = String.valueOf(chat[3]);
            if (key.get(0).equals(a) && key.get(1).equals(b) && key.get(2).equals(c) && key.get(3).equals(d)) {
                save();
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.GONE);
            }else {
                Pinview.setText("Invalid Pin");
                Pinview.setTextColor(Color.RED);
                text = "";
                key.clear();
            }
        }else {
            if (key.get(0).equals("1") && key.get(1).equals("2") && key.get(2).equals("3") && key.get(3).equals("4")) {
                save();
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.GONE);
            }else {
                Pinview.setText("Invalid Pin");
                Pinview.setTextColor(Color.RED);
                text = "";
                key.clear();
            }
        }

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}