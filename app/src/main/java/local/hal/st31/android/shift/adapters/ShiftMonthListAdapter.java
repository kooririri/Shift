package local.hal.st31.android.shift.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import local.hal.st31.android.shift.R;
import local.hal.st31.android.shift.beans.ShiftHopeBean;
import local.hal.st31.android.shift.beans.ShiftTypeBean;
import local.hal.st31.android.shift.beans.TempBean;
import local.hal.st31.android.shift.utils.DateUtils;

public class ShiftMonthListAdapter extends RecyclerView.Adapter<ShiftMonthListAdapter.ShiftMonthListViewHolder> {

    private List<TempBean> list;
    private LayoutInflater mInflater;
    private List<ShiftHopeBean> shiftHopeList = new ArrayList<>();
    private ShiftHopeBean shiftHopeBean;
    private ViewGroup viewGroup;

    static class ShiftMonthListViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerView;

        public ShiftMonthListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txDate);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }

    public ShiftMonthListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public ShiftMonthListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.viewGroup = viewGroup;
        View view = mInflater.inflate(R.layout.cell_shift_submit_list, viewGroup, false);
        return new ShiftMonthListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftMonthListViewHolder shiftMonthListViewHolder, int position) {
        TempBean tempBean = list.get(position);
        shiftMonthListViewHolder.textView.setText(tempBean.getDay() + "日");
        ShiftOptionAdapter shiftOptionAdapter = new ShiftOptionAdapter(viewGroup.getContext());
        shiftOptionAdapter.setShiftList(getTestData());

        LinearLayoutManager layoutManager = new LinearLayoutManager(viewGroup.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//横並び
        shiftMonthListViewHolder.recyclerView.setLayoutManager(layoutManager);
        shiftMonthListViewHolder.recyclerView.setAdapter(shiftOptionAdapter);
//        shiftOptionAdapter.notifyDataSetChanged();
//        shiftHopeList = new ArrayList<>();
        shiftOptionAdapter.setListener(new ShiftOptionAdapter.onShiftTypeClickListener() {
            @Override
            public void onItemClick(int i, ShiftTypeBean res) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, i + 1);
                Date date = calendar.getTime();
                String shiftHopeDate = DateUtils.date2String(date, "yyyy-MM-dd");

                shiftHopeBean = new ShiftHopeBean(res.getShiftId() + shiftHopeDate + res.getShiftTypeId());
//                Log.e("pxl","shiftHopeId = " + shiftHopeBean.getShiftHopeId());
                if (res.getSelectedFlag() == 0) {
                    res.setSelectedFlag(1);

                    shiftHopeBean.setDate(shiftHopeDate);
                    shiftHopeBean.setShiftTypeId(res.getShiftTypeId());
                    shiftHopeBean.setShiftId(res.getShiftId());

                    shiftHopeList.add(shiftHopeBean);
                } else {
                    res.setSelectedFlag(0);
//                    for(ShiftHopeBean bean : shiftHopeList){
//                        if(bean.getShiftHopeId().equals(shiftHopeBean.getShiftHopeId())){
//                            shiftHopeList.remove(bean);
//                        }
//                    }
                    //for循环会报错
                    Iterator<ShiftHopeBean> iterator = shiftHopeList.iterator();
                    while (iterator.hasNext()) {
                        ShiftHopeBean temp = iterator.next();
                        if (temp.getShiftHopeId().equals(shiftHopeBean.getShiftHopeId())) {
                            iterator.remove();
                        }
                    }
                }
//                Log.e("pxl",position + "            "+ res.getSelectedFlag() + "       "+res.getTypeName());
                Log.e("pxl", shiftHopeList.size() + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<TempBean> list) {
        this.list = list;
//        notifyDataSetChanged();
    }

    public List<ShiftHopeBean> getShiftHopeList(){
        return shiftHopeList;
    }


    private List<ShiftTypeBean> getTestData() {
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
        return list;
    }
}

