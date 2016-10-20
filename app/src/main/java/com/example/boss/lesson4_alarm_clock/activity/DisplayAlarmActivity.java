package com.example.boss.lesson4_alarm_clock.activity;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boss.lesson4_alarm_clock.R;
import com.example.boss.lesson4_alarm_clock.provider.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DisplayAlarmActivity extends AppCompatActivity implements View.OnClickListener {
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_alarm);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TextView time = (TextView) findViewById(R.id.timeNow);
        TextView date  = (TextView) findViewById(R.id.date);
        ImageView  imageAlarmAnimation = (ImageView) findViewById(R.id.imageAlarmAnimation);

        Calendar calendar = Calendar.getInstance();
        int minutes;
        String minuteToSet = ((minutes = calendar.get(Calendar.MINUTE)) < Constants.TIME_DISPLAY) ? "0"
                + String.valueOf(minutes) : String.valueOf(minutes);
        if (time != null) {
            time.setText(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + " : "
                    + minuteToSet);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd", Locale.UK);
        if (date != null) {
            date.setText(sdf.format(Calendar.getInstance().getTime()));
        }

        Button btnStopAlarm = (Button) findViewById(R.id.btnStopAlarm);
        if (btnStopAlarm != null) {
            btnStopAlarm.setOnClickListener(this);
        }
        Animation alarmAnimation = AnimationUtils.loadAnimation(this,R.anim.image_alarm_anim);
        if (imageAlarmAnimation != null) {
            imageAlarmAnimation.startAnimation(alarmAnimation);
        }
        mp = MediaPlayer.create(this, R.raw.bon_jovi_its_my_life);
        mp.setLooping(true);
        mp.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStopAlarm:
                mp.stop();
                finish();
                break;
        }
    }
}
