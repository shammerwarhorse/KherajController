package com.opendoors.contact;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AppService extends Service {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        editor.clear();
        editor.commit();
        super.onTaskRemoved(rootIntent);
        //here you will get call when app close.
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
