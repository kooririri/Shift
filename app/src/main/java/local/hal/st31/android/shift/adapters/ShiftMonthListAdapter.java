package local.hal.st31.android.shift.adapters;

import android.annotation.SuppressLint;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import local.hal.st31.android.shift.R;

import local.hal.st31.android.shift.beans.ShiftTypeBean;
import local.hal.st31.android.shift.beans.TempBean;
import local.hal.st31.android.shift.utils.DateUtils;
import local.hal.st31.android.shift.utils.GlobalUtils;

public class ShiftMonthListAdapter extends RecyclerView.Adapter<ShiftMonthListAdapter.ShiftMonthListViewHolder> {

//    private List<TempBean> list;
    private  List<List<ShiftTypeBean>> list;
    private LayoutInflater mInflater;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ShiftMonthListViewHolder shiftMonthListViewHolder, int position) {
//        TempBean tempBean = list.get(position);
        List<ShiftTypeBean> data = list.get(position);
        shiftMonthListViewHolder.textView.setText((position+1) + "日");

        Map<String, JSONArray> kaburuMap = GlobalUtils.getInstance().kaburuMap;
        JSONArray jsonArray = kaburuMap.get("2");
        Log.e("ppqq",jsonArray.length()+"");
        String tempPositon = String.valueOf(position);
        if(position <10){
            tempPositon = "0" + position;
        }
        for (int i =0;i<jsonArray.length();i++){
            try {
                String day = jsonArray.getString(i);
                if(tempPositon.equals(day.substring(8))){
                    shiftMonthListViewHolder.textView.setBackgroundColor(R.color.sblue);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        shiftMonthListViewHolder.textView.setBackgroundColor(R.color.sblue);
        ShiftOptionAdapter shiftOptionAdapter = new ShiftOptionAdapter(viewGroup.getContext());
        shiftOptionAdapter.setShiftList(data);
        shiftOptionAdapter.notifyDataSetChanged();
        LinearLayoutManager layoutManager = new LinearLayoutManager(viewGroup.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//横並び
        shiftMonthListViewHolder.recyclerView.setLayoutManager(layoutManager);
        shiftMonthListViewHolder.recyclerView.setAdapter(shiftOptionAdapter);
//        shiftOptionAdapter.notifyDataSetChanged();
//        shiftHopeList = new ArrayList<>();
        shiftOptionAdapter.setListener(new ShiftOptionAdapter.onShiftTypeClickListener() {
            @Override
            public void onItemClick(int i, ShiftTypeBean res) {

                if (res.getSelectedFlag() == 0) {
                    res.setSelectedFlag(1);
                } else {
                    res.setSelectedFlag(0);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<List<ShiftTypeBean>> list) {
        this.list = list;
        notifyDataSetChanged();
    }




}

