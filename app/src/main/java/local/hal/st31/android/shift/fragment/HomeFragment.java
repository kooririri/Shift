package local.hal.st31.android.shift.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Date;

import local.hal.st31.android.shift.R;
import local.hal.st31.android.shift.adapters.SelfScheduleAdapter;
import local.hal.st31.android.shift.adapters.ShiftOptionAdapter;
import local.hal.st31.android.shift.beans.SelfScheduleBean;
import local.hal.st31.android.shift.calendar_customize.ShiftDecorator;
import local.hal.st31.android.shift.db.DataAccess;
import local.hal.st31.android.shift.db.DatabaseHelper;
import local.hal.st31.android.shift.popup.NewShiftPopup;
import local.hal.st31.android.shift.utils.DateUtils;

public class HomeFragment extends Fragment {
    private View fragmentView;
    private MaterialCalendarView calendarView;
    public String selectedDate = DateUtils.date2String(new Date(),"yyyy-MM-dd");;
    private TextView dateLabel;
    private DatabaseHelper _helper;
    private RecyclerView selfScheduleRecyclerView;
    private SelfScheduleAdapter selfScheduleAdapter;
    private SQLiteDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_home,container,false);
        _helper = new DatabaseHelper(getContext());
        initFragmentView();
        handleRecyclerView();
        popup();
        return fragmentView;
    }

    //Viewを初期化
    private void initFragmentView() {
        calendarView = fragmentView.findViewById(R.id.calendarView);
        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.addDecorators(new ShiftDecorator());
        dateLabel = fragmentView.findViewById(R.id.date_label);
        calendarView.setSelectedDate(CalendarDay.today());
        selectedDate = DateUtils.date2String(new Date(),"yyyy-MM-dd");
        dateLabel.setText(selectedDate);
        selfScheduleRecyclerView = fragmentView.findViewById(R.id.self_schedule_recyclerView);
    }

    private void handleRecyclerView(){
        db = _helper.getWritableDatabase();
        ArrayList<SelfScheduleBean> data = DataAccess.selfScheduleSelectByDate(db, selectedDate);
        selfScheduleAdapter = new SelfScheduleAdapter(getContext());
        selfScheduleAdapter.setData(data);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//横並び
        selfScheduleRecyclerView.setLayoutManager(layoutManager);
        selfScheduleRecyclerView.setAdapter(selfScheduleAdapter);
    }

    //日付をクッリクした場合、Popupを出す
    private void popup(){
        calendarView.setOnDateChangedListener(new OnDateSelectedListener(){
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = date.getDate().toString();
                dateLabel.setText(selectedDate);
                ArrayList<SelfScheduleBean> data = DataAccess.selfScheduleSelectByDate(db, selectedDate);
                selfScheduleAdapter.setData(data);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//横並び
                selfScheduleRecyclerView.setLayoutManager(layoutManager);
                selfScheduleRecyclerView.setAdapter(selfScheduleAdapter);
                Log.e("pxl",selectedDate);
//                new NewShiftPopup(getContext()).showPopupWindow();

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
