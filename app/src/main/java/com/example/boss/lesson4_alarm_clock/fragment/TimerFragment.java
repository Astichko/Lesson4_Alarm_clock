package com.example.boss.lesson4_alarm_clock.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boss.lesson4_alarm_clock.CircularSeekBar;
import com.example.boss.lesson4_alarm_clock.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment {

    protected View mView;
    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        mView = view;
        CircularSeekBar circularSeekBar = (CircularSeekBar) view.findViewById(R.id.circularSeekBar);
        circularSeekBar.setBackgroundColor(getResources().getColor(R.color.colorDark));//outside ring
        circularSeekBar.setRingBackgroundColor(getResources().getColor(R.color.colorDarkBeige));
        circularSeekBar.setProgressColor(getResources().getColor(R.color.colorBeige));
        circularSeekBar.setBarWidth(10);
        circularSeekBar.setPadding(20,20,20,20);
        circularSeekBar.setBackGroundColor(getResources().getColor(R.color.colorDark));
        return view;
    }


}
