package com.example.alialzein.myclassroom;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ALiAlzein on 3/23/2018.
 */

public class SavedSharedPreferences {
    //Save EMail of Users
    private final static String EMAIL = SavedSharedPreferences.class.getName() + ".EMAIL";
    public static void setEmail(final Context context, final String Name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(EMAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, Name);
        editor.apply();
    }
    public static String getUser (final Context context){
        SharedPreferences language = context.getSharedPreferences(EMAIL, context.MODE_PRIVATE);
        return language.getString(EMAIL, null);
    }

    //Save Password of Users
    private final static String PASSWORD = SavedSharedPreferences.class.getName() + ".PASSWORD";
    public static void setPassword(final Context context, final String Name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD, Name);
        editor.apply();
    }
    public static String getPassword (final Context context){
        SharedPreferences language = context.getSharedPreferences(PASSWORD, context.MODE_PRIVATE);
        return language.getString(PASSWORD, null);
    }

    //Check if is a student or not
    private final static String ISSTUDENT = SavedSharedPreferences.class.getName() + ".ISSTUDENT";
    public static void setIsstudent(final Context context, final String Name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ISSTUDENT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ISSTUDENT, Name);
        editor.apply();
    }
    public static String getIsstudent (final Context context){
        SharedPreferences language = context.getSharedPreferences(ISSTUDENT, context.MODE_PRIVATE);
        return language.getString(ISSTUDENT, null);
    }

}
