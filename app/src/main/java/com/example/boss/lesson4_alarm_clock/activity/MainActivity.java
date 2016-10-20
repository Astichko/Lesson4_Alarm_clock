package com.example.boss.lesson4_alarm_clock.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boss.lesson4_alarm_clock.Alarm;
import com.example.boss.lesson4_alarm_clock.R;
import com.example.boss.lesson4_alarm_clock.provider.Constants;
import com.example.boss.lesson4_alarm_clock.provider.DataAlarmProvider;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    Button btnNewAlarm;
    Button btnDeleteAlarm;
    Button btnCancelMain;
    Button btnDeleteChecked;
    ListView listView;
    ArrayList<Integer> selectedItems;
    ImageView alarmImage;
    TextView noAlarmText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        DataAlarmProvider.updateArray(this);
        findViewsAndSet();
        showNoAlarmBlock();
        updateListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showNoAlarmBlock();
        updateListView();
    }

    @Override
    protected void onResume() {
        DataAlarmProvider.updateArray(this);
        updateListView();
        showNoAlarmBlock();
        super.onResume();
    }

    public void findViewsAndSet(){
        btnNewAlarm = (Button) findViewById(R.id.btnNewAlarm);
        btnDeleteAlarm = (Button) findViewById(R.id.btnDeleteAlarm);
        btnCancelMain = (Button) findViewById(R.id.btnCancelMain);
        btnDeleteChecked = (Button) findViewById(R.id.btnDeleteChecked);
        listView = (ListView) findViewById(R.id.listView);
        alarmImage = (ImageView) findViewById(R.id.imageAlarmAnimation);
        noAlarmText = (TextView) findViewById(R.id.noAlarmText);

        btnNewAlarm.setOnClickListener(this);
        btnDeleteAlarm.setOnClickListener(this);
        btnDeleteChecked.setOnClickListener(this);
        btnCancelMain.setOnClickListener(this);
        selectedItems = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (view.getId()) {
                    case R.id.chTextView:
                        saveCheckedItems(position);
                        break;
                    case R.id.itemText:
                        editItem(position);
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNewAlarm:
                Intent intentCreateAlarm = new Intent(this, CreateAlarmActivity.class);
                startActivity(intentCreateAlarm);
                break;
            case R.id.btnDeleteAlarm:
                if(!DataAlarmProvider.isEmpty()){
                    createCheckListView();
                    showAdditionalButtons();
                }else{
                    Toast.makeText(MainActivity.this, "No Alarms", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnDeleteChecked:
                DataAlarmProvider.updateArray(this);
                deleteCheckedItems();
                showMainButtons();
                showNoAlarmBlock();
                updateListView();
                Toast.makeText(this, Constants.DELETED, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnCancelMain:
                showMainButtons();
                updateListView();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void saveCheckedItems(int position) {
        if (selectedItems.contains((Integer) position)) {
            selectedItems.remove((Integer) position);
        } else {
            selectedItems.add((Integer) position);
        }
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

    private void updateListView() {
        if (DataAlarmProvider.getArray() != null) {
            String[] itemTextDisplay = getItemsTextToDisplay();
            ListAdapter la = new ArrayAdapter<>(this, R.layout.item_list_view, itemTextDisplay);
            listView = (ListView) findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(la);
            }
        }
    }

    public void editItem(int position) {
        Intent intentCreateAlarm = new Intent(MainActivity.this, CreateAlarmActivity.class);
        intentCreateAlarm.putExtra(Constants.POSITION, position);
        startActivity(intentCreateAlarm);
    }

    public String[] getItemsTextToDisplay() {
        int size = DataAlarmProvider.getArray().size();
        String[] itemTextDisplay = new String[size];
        int timeAlarm;
        for (int i = 0; i < size; i++) {
            itemTextDisplay[i] = String.valueOf(DataAlarmProvider.getArray().get(i).hour)
                    + " : " +
                    (((timeAlarm = DataAlarmProvider.getArray().get(i).minutes) < Constants.TIME_DISPLAY) ? "0"
                            + String.valueOf(timeAlarm) : String.valueOf(timeAlarm)) + " ";
            if (DataAlarmProvider.getArray().get(i).isRepeated) {
                for (int j = 0; j < Constants.DAYS; j++) {
                    if (DataAlarmProvider.getArray().get(i).dayWeek[j]) {
                        itemTextDisplay[i] += " " + getShortDayName(j);
                    }
                }
            } else {
                itemTextDisplay[i] += Constants.SINGLE_ALARM;
            }
        }
        return itemTextDisplay;
    }

    public void showMainButtons() {
        btnCancelMain.setVisibility(View.GONE);
        btnDeleteChecked.setVisibility(View.GONE);
        btnNewAlarm.setVisibility(View.VISIBLE);
        btnDeleteAlarm.setVisibility(View.VISIBLE);
    }

    public void showAdditionalButtons() {
        btnNewAlarm.setVisibility(View.GONE);
        btnDeleteAlarm.setVisibility(View.GONE);
        btnCancelMain.setVisibility(View.VISIBLE);
        btnDeleteChecked.setVisibility(View.VISIBLE);
    }

    public void deleteCheckedItems() {
        for (Integer i : selectedItems) {
            DataAlarmProvider.getArray().set(i,null);
        }
        for(Alarm a: DataAlarmProvider.getArray()){
            if (a == null){
                DataAlarmProvider.getArray().remove(a);
            }
        }
        DataAlarmProvider.saveArray(this);
        selectedItems.clear();
    }

    public void createCheckListView() {
        String[] itemTextDisplay = getItemsTextToDisplay();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_check_delete, R.id.chTextView, itemTextDisplay);
        listView = (ListView) findViewById(R.id.listView);
        if (listView != null) {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        }
        if (listView != null) {
            listView.setAdapter(adapter);
        }
    }

    public void showNoAlarmBlock() {
        if (DataAlarmProvider.getArray() == null || DataAlarmProvider.getArray().isEmpty()) {
            alarmImage.setVisibility(View.VISIBLE);
            noAlarmText.setVisibility(View.VISIBLE);
        } else {
            alarmImage.setVisibility(View.GONE);
            noAlarmText.setVisibility(View.GONE);
        }
    }
}
