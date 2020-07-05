package com.example.studentsapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class savedSharedPreferences
{
    static final String PREF_DATA="data";


    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String data) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_DATA, data);
        editor.apply();
    }



    public static String getUserName(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_DATA, "");
    }

    /*public static void clear(Context r){
        File dir = new File(r.getFilesDir().getParent() + "/shared_prefs/"); // files directory is a sibling of the shared_prefs directory
        String[] children = dir.list();
        for (String aChildren : children) {
            r.getSharedPreferences(aChildren.replace(".xml", ""), Context.MODE_PRIVATE).edit().clear().commit();
        }
        try {
            Thread.sleep(800);
        }
        catch (InterruptedException e) { }
        for (String aChildren : children) {
            new File(dir, aChildren).delete();
        }
    }

     */

}
