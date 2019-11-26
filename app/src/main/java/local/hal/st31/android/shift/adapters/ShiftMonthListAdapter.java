package local.hal.st31.android.shift.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import local.hal.st31.android.shift.R;
import local.hal.st31.android.shift.beans.ShiftTypeBean;
import local.hal.st31.android.shift.beans.TempBean;
import local.hal.st31.android.shift.utils.DateUtils;

public class ShiftMonthListAdapter extends BaseAdapter {
//    private List<Integer> list;
//    private RecyclerView recyclerView;
    private List<TempBean> list;
    private LayoutInflater mInflater;
    private Context mContext;
    private ShiftOptionAdapter shiftOptionAdapter;

    public List<TempBean> getList() {
        return list;
    }

    public void setList(List<TempBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

//    public void setRecyclerView(RecyclerView recyclerView){
//        this.recyclerView = recyclerView;
//    }

    public ShiftMonthListAdapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.cell_shift_submit_list,null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        TempBean tempBean = (TempBean) getItem(position);
        holder.textView.setText(tempBean.getDay()+"日");
        shiftOptionAdapter = new ShiftOptionAdapter(parent.getContext());
        shiftOptionAdapter.setShiftList(getTestData());

        LinearLayoutManager layoutManager = new LinearLayoutManager(parent.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//横並び
//        GridLayoutManager layoutManager = new GridLayoutManager(parent.getContext(), 4);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(shiftOptionAdapter);
        shiftOptionAdapter.notifyDataSetChanged();
//        int day = (int) getItem(position);
//        holder.textView.setText(day+"日");

        return convertView;
    }


    public static int dip2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);

    }


    private List<ShiftTypeBean> getTestData(){
        List<ShiftTypeBean> list = new ArrayList<>();
        ShiftTypeBean bean = new ShiftTypeBean();
        bean.setShiftId(1);
        bean.setBeginTime("10:00");
        bean.setEndTime("19:00");
        bean.setTypeName("A勤");
        bean.setComment("休憩1時間");
        list.add(bean);
        bean = new ShiftTypeBean();
        bean.setShiftId(2);
        bean.setBeginTime("12:00");
        bean.setEndTime("21:00");
        bean.setTypeName("B勤");
        bean.setComment("休憩1時間");
        list.add(bean);
        bean = new ShiftTypeBean();
        bean.setShiftId(2);
        bean.setBeginTime("12:00");
        bean.setEndTime("21:00");
        bean.setTypeName("B勤");
        bean.setComment("休憩1時間");
        list.add(bean);
        bean = new ShiftTypeBean();
        bean.setShiftId(2);
        bean.setBeginTime("12:00");
        bean.setEndTime("21:00");
        bean.setTypeName("B勤");
        bean.setComment("休憩1時間");
        list.add(bean);
        bean = new ShiftTypeBean();
        bean.setShiftId(2);
        bean.setBeginTime("12:00");
        bean.setEndTime("21:00");
        bean.setTypeName("B勤");
        bean.setComment("休憩1時間");
        list.add(bean);
        bean = new ShiftTypeBean();
        bean.setShiftId(2);
        bean.setBeginTime("12:00");
        bean.setEndTime("21:00");
        bean.setTypeName("B勤");
        bean.setComment("休憩1時間");
        list.add(bean);
        return list;
    }

}
class ViewHolder{
    TextView textView;
    RecyclerView recyclerView;

    public ViewHolder(View itemVIew){
        textView = itemVIew.findViewById(R.id.txDate);
        recyclerView = itemVIew.findViewById(R.id.recyclerView);

    }

}
