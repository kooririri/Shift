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

import local.hal.st31.android.shift.R;
import local.hal.st31.android.shift.calendar_customize.ShiftDecorator;

public class HomeFragment extends Fragment {
    private View fragmentView;
    private MaterialCalendarView calendarView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_home,container,false);
        initFragmentView();
        return fragmentView;
    }

    private void initFragmentView() {
        calendarView = fragmentView.findViewById(R.id.calendarView);
        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.addDecorators(new ShiftDecorator());
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
