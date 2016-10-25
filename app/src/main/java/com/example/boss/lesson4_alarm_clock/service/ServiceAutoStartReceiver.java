package com.example.boss.lesson4_alarm_clock.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by BOSS on 18.10.2016.
 */
public class ServiceAutoStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, AlarmService.class);
        context.startService(startServiceIntent);
    }
}
