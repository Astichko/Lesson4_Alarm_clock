package com.example.boss.lesson4_alarm_clock.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.boss.lesson4_alarm_clock.Alarm;
import com.example.boss.lesson4_alarm_clock.R;
import com.example.boss.lesson4_alarm_clock.provider.Constants;
import com.example.boss.lesson4_alarm_clock.provider.DataAlarmProvider;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by BOSS on 22.10.2016.
 */
public class DelItemAdapter extends ArrayAdapter<Alarm> {
    /**
     * This adapter help to represent data in main window when user want to delete
     * their alarms.
     */

    CopyOnWriteArrayList<Alarm> alarmsData;
    Context context;
    int resource;

    public DelItemAdapter(Context context, int resource, CopyOnWriteArrayList<Alarm> objects) {
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
            convertView = layoutInflater.inflate(R.layout.item_del_listview, null, true);
        }

        Alarm alarm = DataAlarmProvider.getArray().get(position);

        TextView itemTimeText = (TextView) convertView.findViewById(R.id.itemTimeText);
        TextView itemDaysText = (TextView) convertView.findViewById(R.id.itemDaysText);

        itemTimeText.setText(String.format("%02d:%02d", alarm.hour, alarm.minutes));
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
}