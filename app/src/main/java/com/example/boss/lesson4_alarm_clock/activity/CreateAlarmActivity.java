package com.example.boss.lesson4_alarm_clock.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.boss.lesson4_alarm_clock.Alarm;
import com.example.boss.lesson4_alarm_clock.R;
import com.example.boss.lesson4_alarm_clock.provider.Constants;
import com.example.boss.lesson4_alarm_clock.provider.DataAlarmProvider;
import com.example.boss.lesson4_alarm_clock.service.MyService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CreateAlarmActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    TimePicker timePicker;
    Calendar calendar;
    Button save;
    Button cancel;
    Bundle b;
    ArrayList<Button> btnsWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewsAndSet();
        DataAlarmProvider.updateArray(this);
        setButtonPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                calendar = Calendar.getInstance();
                int hoursNow = calendar.get(Calendar.HOUR_OF_DAY);
                int minutesNow = calendar.get(Calendar.MINUTE);
                int hoursSet = timePicker.getCurrentHour();
                int minutesSet = timePicker.getCurrentMinute();
                if (hoursSet > hoursNow || (hoursSet == hoursNow && minutesSet > minutesNow)) {//Set time > now time, that means no need to add 1 day to date.
                    createAlarm(hoursSet, minutesSet);
                } else {
                    calendar.add(Calendar.DATE, 1);
                    createAlarm(hoursSet, minutesSet);
                }
                Toast.makeText(CreateAlarmActivity.this, Constants.NEW_ALARM_CREATED, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btnCancel:
                DataAlarmProvider.saveArray(this);
                finish();
                break;
        }
    }

    private void setButtonPressed(){
        b = getIntent().getExtras();
        if (b != null) {
            Integer listItemPosition = (Integer) b.get(Constants.POSITION);
            if (listItemPosition != null) {
                int hour = DataAlarmProvider.getArray().get(listItemPosition).hour;
                int minute = DataAlarmProvider.getArray().get(listItemPosition).minutes;
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);
                if (DataAlarmProvider.getArray().get(listItemPosition).isRepeated) {
                    for (int i = 0; i < Constants.DAYS; i++) {
                        if (DataAlarmProvider.getArray().get(listItemPosition).dayWeek[i]) {
                            btnsWeek.get(i).setPressed(true);
                        }
                    }
                }
            }
        }
    }

    private void findViewsAndSet(){
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        calendar = Calendar.getInstance();
        if (timePicker != null) {
            timePicker.setIs24HourView(true);
        }
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        save = (Button) findViewById(R.id.btnSave);
        cancel = (Button) findViewById(R.id.btnCancel);
        Button btnMo = (Button) findViewById(R.id.btnMo);
        Button btnTu = (Button) findViewById(R.id.btnTu);
        Button btnWe = (Button) findViewById(R.id.btnWe);
        Button btnTh = (Button) findViewById(R.id.btnTh);
        Button btnFr = (Button) findViewById(R.id.btnFr);
        Button btnSa = (Button) findViewById(R.id.btnSa);
        Button btnSu = (Button) findViewById(R.id.btnSu);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        btnsWeek = new ArrayList<>();
        btnsWeek.addAll(Arrays.asList(btnSu, btnMo, btnTu, btnWe, btnTh, btnFr, btnSa));
        for (Button b : btnsWeek) {
            b.setOnTouchListener(this);
        }
    }

    private void createAlarm(int hoursSet, int minutesSet) {
        Alarm alarm = new Alarm();
        setDaysOfTheWeek(alarm);
        alarm.hour = hoursSet;
        alarm.minutes = minutesSet;
        alarm.isOn = true;
        if (!alarm.isRepeated) {
            alarm.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            alarm.month = calendar.get(Calendar.MONTH);
            alarm.year = calendar.get(Calendar.YEAR);
        }
        if (b != null) {
            Integer listItemPosition = (Integer) b.get(Constants.POSITION);
            if(listItemPosition != null) {
                DataAlarmProvider.getArray().set(listItemPosition, alarm);
                DataAlarmProvider.saveArray(this);
            }

        } else {
            DataAlarmProvider.getArray().add(alarm);
            DataAlarmProvider.saveArray(this);
        }
        if (!isServiceRunning(MyService.class)) {
            startService(new Intent(this, MyService.class));
        }
    }

    private void setDaysOfTheWeek(Alarm alarm) {
        boolean isDayWeekArrCreated = false;
        for (int i = 0; i < Constants.DAYS; i++) {
            boolean isPressed = btnsWeek.get(i).isPressed();
            if (isPressed) {
                if (!isDayWeekArrCreated) {
                    alarm.dayWeek = new boolean[Constants.DAYS];
                    isDayWeekArrCreated = true;
                }
                alarm.isRepeated = true;
                alarm.dayWeek[i] = true;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (Button b : btnsWeek) {
                if (b.getId() == v.getId()) {
                    if (b.isPressed()) {
                        b.setPressed(false);
                    } else {
                        b.setPressed(true);
                    }
                }
            }
        }
        return true;
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
