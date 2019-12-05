package local.hal.st31.android.shift.fragment;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import local.hal.st31.android.shift.R;
import local.hal.st31.android.shift.adapters.ShiftMonthListAdapter;
import local.hal.st31.android.shift.beans.ShiftTypeBean;
import local.hal.st31.android.shift.utils.DateUtils;

public class ShiftSubmitFragment extends Fragment {
    private View fragmentView;

    private RecyclerView shiftListView;
    private ShiftMonthListAdapter shiftMonthListAdapter;
    private TextView dateLabel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_shift_submit,container,false);
        shiftListView = fragmentView.findViewById(R.id.shiftListView);
        initView();
        return fragmentView;
    }

    private void initView(){
        //次の月（シフト）の日数を取得
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+2;
        int days = DateUtils.getDaysByYearMonth(year,month);


        shiftMonthListAdapter = new ShiftMonthListAdapter(getContext());
        shiftMonthListAdapter.setList(getTestData());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//縦並び
        shiftListView.setLayoutManager(layoutManager);
        shiftListView.setAdapter(shiftMonthListAdapter);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1);
        dateLabel = fragmentView.findViewById(R.id.day_text);
        dateLabel.setText(cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH)+1)+"月");
        dateLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("aaa",shiftMonthListAdapter.getShiftHopeList().size()+"");
            }
        });
    }

    private  List<List<ShiftTypeBean>> getTestData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+2;
        int days = DateUtils.getDaysByYearMonth(year,month);

        List<List<ShiftTypeBean>> data = new ArrayList<>();

        for(int i = 1;i<=days;i++){
            List<ShiftTypeBean> list = new ArrayList<>();
            ShiftTypeBean bean = new ShiftTypeBean();
            bean.setShiftId(1);
            bean.setShiftTypeId(1);
            bean.setBeginTime("10:00");
            bean.setEndTime("19:00");
            bean.setTypeName("A勤");
            bean.setComment("休憩1時間");
            list.add(bean);
            bean = new ShiftTypeBean();
            bean.setShiftId(2);
            bean.setShiftTypeId(2);
            bean.setBeginTime("12:00");
            bean.setEndTime("21:00");
            bean.setTypeName("B勤");
            bean.setComment("休憩1時間");
            list.add(bean);
            bean = new ShiftTypeBean();
            bean.setShiftId(3);
            bean.setShiftTypeId(3);
            bean.setBeginTime("12:00");
            bean.setEndTime("21:00");
            bean.setTypeName("C勤");
            bean.setComment("休憩1時間");
            list.add(bean);
            bean = new ShiftTypeBean();
            bean.setShiftId(4);
            bean.setShiftTypeId(4);
            bean.setBeginTime("12:00");
            bean.setEndTime("21:00");
            bean.setTypeName("D勤");
            bean.setComment("休憩1時間");
            list.add(bean);
            bean = new ShiftTypeBean();
            bean.setShiftId(5);
            bean.setShiftTypeId(5);
            bean.setBeginTime("12:00");
            bean.setEndTime("21:00");
            bean.setTypeName("E勤");
            bean.setComment("休憩1時間");
            list.add(bean);

            data.add(list);
        }

        return data;
    }

}
