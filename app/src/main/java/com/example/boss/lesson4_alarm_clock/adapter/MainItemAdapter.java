package com.example.boss.lesson4_alarm_clock.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.boss.lesson4_alarm_clock.Alarm;
import com.example.boss.lesson4_alarm_clock.R;
import com.example.boss.lesson4_alarm_clock.provider.Constants;
import com.example.boss.lesson4_alarm_clock.provider.DataAlarmProvider;
import com.example.boss.lesson4_alarm_clock.service.AlarmService;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by BOSS on 22.10.2016.
 */
public class MainItemAdapter extends ArrayAdapter<Alarm> {
    /**
     * This adapter help to represent data in main window when user want to set up
     * their alarms.
     */

    CopyOnWriteArrayList<Alarm> alarmsData;
    Context context;
    int resource;

    public MainItemAdapter(Context context, int resource, CopyOnWriteArrayList<Alarm> objects) {
        super(context, resource, objects);
        this.alarmsData = objects;
        this.context = context;
        this.resource = resource;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().
                    getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_main_listview, null, true);
        }

        Alarm alarm = DataAlarmProvider.getArray().get(position);

        TextView itemTimeText = (TextView) convertView.findViewById(R.id.itemTimeText);
        TextView itemDaysText = (TextView) convertView.findViewById(R.id.itemDaysText);
        SwitchCompat itemSwitch = (SwitchCompat) convertView.findViewById(R.id.itemSwitch);

        itemSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataAlarmProvider.getArray().get(position).isOn = isChecked;
                buttonView.setChecked(isChecked);
                if (!isServiceRunning(context, AlarmService.class)) {
                    context.startService(new Intent(context, AlarmService.class));
                }
                    DataAlarmProvider.saveArray(context);
            }
        });
        itemTimeText.setText(String.format("%02d:%02d", alarm.hour, alarm.minutes));
        itemSwitch.setChecked(DataAlarmProvider.getArray().get(position).isOn);
        String daysOfWeek = "";
        if (alarm.isRepeated) {
            for (int j = 0; j < Constants.DAYS; j++) {
                if (alarm.dayWeek[j]) {
                    daysOfWeek += " " + getShortDayName(j);
                }
            }
        } else {
            daysOfWeek = "Single Alarm";
        }

        itemDaysText.setText(daysOfWeek);
        return convertView;
    }

    public static String getShortDayName(int day) {
        String result = "";
        switch (day) {
            case 0:
                result = "SU ";
                break;
            case 1:
                result = "MO ";
                break;
            case 2:
                result = "TU ";
                break;
            case 3:
                result = "WE ";
                break;
            case 4:
                result = "TH ";
                break;
            case 5:
                result = "FR ";
                break;
            case 6:
                result = "SA ";
                break;
        }
        return result;
    }

    public boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}