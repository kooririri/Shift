package local.hal.st31.android.shift;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import local.hal.st31.android.shift.beans.SelfScheduleBean;
import local.hal.st31.android.shift.db.DataAccess;
import local.hal.st31.android.shift.db.DatabaseHelper;
import local.hal.st31.android.shift.fragment.HomeFragment;
import local.hal.st31.android.shift.popup.NewShiftPopup;
import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class NewSelfShiftAddActivity extends AppCompatActivity {
    private LinearLayout LLStart;
    private LinearLayout LLEnd;
    private TextView startTime;
    private TextView endTime;
    private String startHour;
    private String endHour;
    private String startMinute;
    private String endMinute;
    private TextFieldBoxes tfbOccupation;
    private TextFieldBoxes tfbMemo;
    private String occupation;
    private String memo;
    private DatabaseHelper _helper;
    private Button sendButton;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_self_shift_add);
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        initView();
    }

    private void initView() {
        _helper = new DatabaseHelper(getApplicationContext());
        sendButton = findViewById(R.id.btn_send);
        LLStart = findViewById(R.id.self_shift_start_time);
        LLStart.setOnClickListener(new NewSelfShiftAddActivity.onTimeBlockClickListener());
        LLEnd = findViewById(R.id.self_shift_end_time);
        LLEnd.setOnClickListener(new NewSelfShiftAddActivity.onTimeBlockClickListener());
        startTime = findViewById(R.id.label_start_time);
        endTime = findViewById(R.id.label_end_time);
        tfbOccupation = findViewById(R.id.text_field_boxes_occupation);
        tfbMemo = findViewById(R.id.text_field_boxes_memo);
        tfbOccupation.setHasClearButton(true);
        tfbMemo.setHasClearButton(true);
        tfbOccupation.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                occupation = theNewText;
            }
        });
        tfbMemo.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                memo = theNewText;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = _helper.getWritableDatabase();
                SelfScheduleBean selfScheduleBean = new SelfScheduleBean();
                selfScheduleBean.setWork(occupation);
                selfScheduleBean.setMemo(memo);
                selfScheduleBean.setStartTime(startHour+":"+startMinute);
                selfScheduleBean.setEndTime(endHour+":"+endMinute);
                selfScheduleBean.setDate(date);
                DataAccess.selfScheduleInsert(db,selfScheduleBean);

                Toast.makeText(getApplicationContext(),"登録しました",Toast.LENGTH_LONG).show();
                NewSelfShiftAddActivity.this.finish();
            }
        });
    }
    private class onTimeBlockClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            if(view.getId() == R.id.self_shift_start_time) {
                TimePickerDialog dialog = new TimePickerDialog(NewSelfShiftAddActivity.this, new NewSelfShiftAddActivity.StartTimePickerDialogSetListener(), 0, 0, true);
                dialog.show();
            }else if (view.getId() == R.id.self_shift_end_time){
                TimePickerDialog dialog = new TimePickerDialog(NewSelfShiftAddActivity.this, new NewSelfShiftAddActivity.EndTimePickerDialogSetListener(), 0, 0, true);
                dialog.show();
            }
        }
    }

    private class StartTimePickerDialogSetListener implements TimePickerDialog.OnTimeSetListener{
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            String msg = "開始時間選択ダイアログ：" + hourOfDay + "時" + minute + "分";
//            Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
            if(hourOfDay < 10){
                startHour = "0"+String.valueOf(hourOfDay);
            }else{
                startHour = String.valueOf(hourOfDay);
            }
            if(minute < 10){
                startMinute = "0"+String.valueOf(minute);
            }else{
                startMinute = String.valueOf(minute);
            }

            startTime.setText(startHour+"："+startMinute);
        }
    }

    private class EndTimePickerDialogSetListener implements TimePickerDialog.OnTimeSetListener{
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            String msg = "終了時間選択ダイアログ：" + hourOfDay + "時" + minute + "分";
//            Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
            if(hourOfDay < 10){
                endHour = "0"+String.valueOf(hourOfDay);
            }else{
                endHour = String.valueOf(hourOfDay);
            }
            if(minute < 10){
                endMinute = "0"+String.valueOf(minute);
            }else{
                endMinute = String.valueOf(minute);
            }
            endTime.setText(endHour+"："+endMinute);
        }
    }
}
