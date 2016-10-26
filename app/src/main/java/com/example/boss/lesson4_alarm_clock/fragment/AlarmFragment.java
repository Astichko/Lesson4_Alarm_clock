package com.example.boss.lesson4_alarm_clock.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boss.lesson4_alarm_clock.Alarm;
import com.example.boss.lesson4_alarm_clock.R;
import com.example.boss.lesson4_alarm_clock.activity.CreateAlarmActivity;
import com.example.boss.lesson4_alarm_clock.adapter.DelItemAdapter;
import com.example.boss.lesson4_alarm_clock.adapter.MainItemAdapter;
import com.example.boss.lesson4_alarm_clock.provider.Constants;
import com.example.boss.lesson4_alarm_clock.provider.DataAlarmProvider;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment implements View.OnClickListener {


    protected View mView;

    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        mView = view;
        findViewsAndSet();
        DataAlarmProvider.updateArray(getActivity());
        showNoAlarmBlock();
        updateListView();
        return view;
    }

    Button btnNewAlarm;
    Button btnDeleteAlarm;
    Button btnCancelMain;
    Button btnDeleteChecked;
    ListView listView;
    ArrayList<Integer> selectedItems;
    ImageView alarmImage;
    TextView noAlarmText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void onStart() {
        super.onStart();
        showNoAlarmBlock();
        updateListView();
    }

    @Override
    public void onResume() {
        DataAlarmProvider.updateArray(getActivity());
        updateListView();
        showNoAlarmBlock();
        super.onResume();
    }

    public void findViewsAndSet() {
        btnNewAlarm = (Button) mView.findViewById(R.id.btnNewAlarm);
        btnDeleteAlarm = (Button) mView.findViewById(R.id.btnDeleteAlarm);
        btnCancelMain = (Button) mView.findViewById(R.id.btnCancelMain);
        btnDeleteChecked = (Button) mView.findViewById(R.id.btnDeleteChecked);
        listView = (ListView) mView.findViewById(R.id.listView);
        alarmImage = (ImageView) mView.findViewById(R.id.imageAlarmAnimation);
        noAlarmText = (TextView) mView.findViewById(R.id.noAlarmText);

        btnNewAlarm.setOnClickListener(this);
        btnDeleteAlarm.setOnClickListener(this);
        btnDeleteChecked.setOnClickListener(this);
        btnCancelMain.setOnClickListener(this);
        selectedItems = new ArrayList<>();
        listView.setItemsCanFocus(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (view.getId()) {
                    case R.id.itemLinearLayout:
                        editItem(position);
                        break;
                    case R.id.itemDelLinearLayout:
                        saveCheckedItems(position);
                        CheckBox box = (CheckBox) view.findViewById(R.id.itemCheckBox);
                        box.setChecked(!box.isChecked());
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNewAlarm:
                Intent intentCreateAlarm = new Intent(getActivity(), CreateAlarmActivity.class);
                startActivity(intentCreateAlarm);
                break;
            case R.id.btnDeleteAlarm:
                if (!DataAlarmProvider.isEmpty()) {
                    createCheckListView();
                    showAdditionalButtons();
                } else {
                    Toast.makeText(getActivity(), "No Alarms", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnDeleteChecked:
                DataAlarmProvider.updateArray(getActivity());
                deleteCheckedItems();
                showMainButtons();
                showNoAlarmBlock();
                updateListView();
                Toast.makeText(getActivity(), Constants.DELETED, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnCancelMain:
                showMainButtons();
                updateListView();
                break;
        }
    }

    public void saveCheckedItems(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove((Integer) position);
        } else {
            selectedItems.add(position);
        }
    }

    private void updateListView() {
        if (DataAlarmProvider.getArray() != null) {
            ListAdapter la = new MainItemAdapter(getActivity(), R.layout.item_main_listview, DataAlarmProvider.getArray());
            listView = (ListView) mView.findViewById(R.id.listView);
            if (listView != null) {
                listView.setAdapter(la);
            }
        }
    }

    public void editItem(int position) {
        Intent intentCreateAlarm = new Intent(getActivity(), CreateAlarmActivity.class);
        intentCreateAlarm.putExtra(Constants.POSITION, position);
        startActivity(intentCreateAlarm);
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
            DataAlarmProvider.getArray().set(i, null);
        }
        for (Alarm a : DataAlarmProvider.getArray()) {
            if (a == null) {
                DataAlarmProvider.getArray().remove(a);
            }
        }
        DataAlarmProvider.saveArray(getActivity());
        selectedItems.clear();
    }

    public void createCheckListView() {
        ListAdapter la = new DelItemAdapter(getActivity(), R.layout.item_del_listview, DataAlarmProvider.getArray());
        listView.setAdapter(la);
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
