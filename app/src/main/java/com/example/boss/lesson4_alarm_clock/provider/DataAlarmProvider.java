package com.example.boss.lesson4_alarm_clock.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.boss.lesson4_alarm_clock.Alarm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by BOSS on 19.10.2016.
 */
public class DataAlarmProvider {

    public static CopyOnWriteArrayList<Alarm> alarmsData;

    public static CopyOnWriteArrayList<Alarm> getArray(){
        if(alarmsData == null){
            alarmsData = new CopyOnWriteArrayList<>();
        }
        return alarmsData;
    }

    public static void saveArray(Context context){
        SharedPreferences sharedPrefs = context.getSharedPreferences(Constants.PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(DataAlarmProvider.alarmsData);
        editor.putString(Constants.PREFERENCES_TAG, json);
        editor.apply();
    }
    public static void updateArray(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(Constants.PREFERENCES_NAME, 0);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(Constants.PREFERENCES_TAG, null);
        Type type = new TypeToken<CopyOnWriteArrayList<Alarm>>() {
        }.getType();
        DataAlarmProvider.alarmsData = gson.fromJson(json, type);
        if (DataAlarmProvider.alarmsData == null) {
            DataAlarmProvider.alarmsData = new CopyOnWriteArrayList<>();
        }
    }

    public static boolean isEmpty(){
        return getArray().isEmpty();

    }
}
