package com.example.boss.lesson4_alarm_clock.service;

import android.app.IntentService;
import android.content.Intent;

import com.example.boss.lesson4_alarm_clock.Alarm;
import com.example.boss.lesson4_alarm_clock.activity.DisplayAlarmActivity;
import com.example.boss.lesson4_alarm_clock.provider.Constants;
import com.example.boss.lesson4_alarm_clock.provider.DataAlarmProvider;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MyService extends IntentService {
    Calendar calendar;
    public boolean isFirstTimeAlignment = true;

    public MyService() {
        super("MyService");
    }

    public void onCreate() {
        super.onCreate();
        calendar = Calendar.getInstance();
        DataAlarmProvider.updateArray(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        turnAlarm();
    }

    public void turnAlarm() {
        calendar = Calendar.getInstance();
        while (!DataAlarmProvider.isEmpty()) {
            try {
                timeAlignmentWait();
                checkAlarmsAndRing();
                TimeUnit.SECONDS.sleep(Constants.ONE_MINUTE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void displayAlarm() {
        Intent displayAlarm = new Intent(getBaseContext(), DisplayAlarmActivity.class);
        displayAlarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(displayAlarm);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void timeAlignmentWait() {
        if (isFirstTimeAlignment) {
            calendar = Calendar.getInstance();
            try {
                TimeUnit.SECONDS.sleep(Constants.ONE_MINUTE - calendar.get(Calendar.SECOND) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isFirstTimeAlignment = false;
        }
    }

    public void checkAlarmsAndRing() {
        DataAlarmProvider.updateArray(this);
        calendar = Calendar.getInstance();
        int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
        int minutesNow = calendar.get(Calendar.MINUTE);
        int dayOfWeekNow = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
        int monthNow = calendar.get(Calendar.MONTH);
        int yearNow = calendar.get(Calendar.YEAR);
        for (Alarm a : DataAlarmProvider.getArray()) {
            if (a.isRepeated && a.dayWeek[dayOfWeekNow] && hourNow == a.hour && minutesNow == a.minutes) {
                displayAlarm();
            } else {
                if (hourNow == a.hour && minutesNow == a.minutes
                        && dayOfMonthNow == a.dayOfMonth && monthNow == a.month && yearNow == a.year) {
                    displayAlarm();
                    DataAlarmProvider.getArray().remove(a);
                }
            }
        }
        DataAlarmProvider.saveArray(this);
    }
}
