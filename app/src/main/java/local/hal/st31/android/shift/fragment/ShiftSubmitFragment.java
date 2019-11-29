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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import local.hal.st31.android.shift.R;
import local.hal.st31.android.shift.adapters.ShiftMonthListAdapter;
import local.hal.st31.android.shift.adapters.ShiftOptionAdapter;
import local.hal.st31.android.shift.beans.ShiftAttrBean;
import local.hal.st31.android.shift.beans.ShiftTypeBean;
import local.hal.st31.android.shift.beans.TempBean;
import local.hal.st31.android.shift.utils.DateUtils;

public class ShiftSubmitFragment extends Fragment {
    private View fragmentView;

    private ListView shiftList;
    private List<TempBean> mShiftData;
    private ShiftMonthListAdapter shiftMonthListAdapter;
    private TextView dateLabel;

    private RecyclerView recyclerView;
    private ShiftOptionAdapter shiftOptionAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_shift_submit,container,false);
        shiftList = fragmentView.findViewById(R.id.shiftListView);
        recyclerView = fragmentView.findViewById(R.id.recyclerView);
        initView();
        return fragmentView;
    }

    private void initView(){
        //次の月（シフト）の日数を取得
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+2;
        int days = DateUtils.getDaysByYearMonth(year,month);

        mShiftData = new ArrayList<>();
        TempBean tempBean = new TempBean();
        for(int i = 1 ; i <= days; i++){
           tempBean.setDay(i);
           tempBean.setRecyclerView(recyclerView);
           mShiftData.add(tempBean);
           tempBean = new TempBean();
        }
        shiftMonthListAdapter = new ShiftMonthListAdapter(getContext());
        shiftMonthListAdapter.setList(mShiftData);
        shiftList.setAdapter(shiftMonthListAdapter);

//        shiftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("pxl","日にちは"+position);
//            }
//        });

        dateLabel = fragmentView.findViewById(R.id.day_text);
        dateLabel.setText(year+"年"+month+"月");
        dateLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("aaa",shiftMonthListAdapter.getShiftHopeList().size()+"");
            }
        });
    }

    private void initRecyclerView(){
//        recyclerView = fragmentView.findViewById(R.id.recyclerView);
//        shiftOptionAdapter = new ShiftOptionAdapter(getContext());
//        shiftOptionAdapter.setShiftList(getTestData());

//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//横並び
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(shiftOptionAdapter);
//        shiftOptionAdapter.notifyDataSetChanged();

    }

}
