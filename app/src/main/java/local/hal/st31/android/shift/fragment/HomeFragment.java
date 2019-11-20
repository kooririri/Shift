package local.hal.st31.android.shift.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import local.hal.st31.android.shift.R;
import local.hal.st31.android.shift.calendar_customize.ShiftDecorator;
import local.hal.st31.android.shift.popup.NewShiftPopup;

public class HomeFragment extends Fragment {
    private View fragmentView;
    private MaterialCalendarView calendarView;
    private String selectedData;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_home,container,false);
        initFragmentView();
        popup();
        return fragmentView;
    }

    //Viewを初期化
    private void initFragmentView() {
        calendarView = fragmentView.findViewById(R.id.calendarView);
        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.addDecorators(new ShiftDecorator());
    }

    //日付をクッリクした場合、Popupを出す
    private void popup(){
        calendarView.setOnDateChangedListener(new OnDateSelectedListener(){

            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedData = date.getDate().toString();
                new NewShiftPopup(getContext()).showPopupWindow();
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
