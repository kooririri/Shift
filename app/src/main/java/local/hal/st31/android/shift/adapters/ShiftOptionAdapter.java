package local.hal.st31.android.shift.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import local.hal.st31.android.shift.R;
import local.hal.st31.android.shift.beans.ShiftTypeBean;

public class ShiftOptionAdapter extends RecyclerView.Adapter<ShiftOptionAdapter.ShiftOptionViewHolder> {

    private LayoutInflater mInflater;
    private List<ShiftTypeBean> shiftList;
    private onShiftTypeClickListener listener;

    static class ShiftOptionViewHolder extends RecyclerView.ViewHolder{
        LinearLayout shiftBlock;
        TextView optionBlock;
        TextView startTime;
        TextView endTime;
        public ShiftOptionViewHolder(@NonNull View itemView) {
            super(itemView);
            shiftBlock = itemView.findViewById(R.id.shift_block);
            optionBlock = itemView.findViewById(R.id.shift_option);
            startTime = itemView.findViewById(R.id.tx_start_time);
            endTime = itemView.findViewById(R.id.tx_end_time);
        }
    }



    public ShiftOptionAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }
    public void setShiftList(List<ShiftTypeBean> shiftList){
        this.shiftList = shiftList;
    }

    @NonNull
    @Override
    public ShiftOptionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.grid_shift_option,viewGroup,false);
        return new ShiftOptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftOptionViewHolder shiftOptionViewHolder, final int position) {
        final ShiftTypeBean res = shiftList.get(position);
        shiftOptionViewHolder.optionBlock.setText(res.getTypeName());
        shiftOptionViewHolder.startTime.setText(res.getBeginTime());
        shiftOptionViewHolder.endTime.setText(res.getEndTime());

        shiftOptionViewHolder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                listener.onItemClick(position,res);
                notifyDataSetChanged();
            }

        });

        if (res.getSelectedFlag() == 1) {
            shiftOptionViewHolder.itemView.setBackgroundColor(Color.BLUE);
        }else{
            shiftOptionViewHolder.itemView.setBackgroundColor(Color.WHITE);
        }
    }


    @Override
    public int getItemCount() {
        return shiftList == null ? 0 : shiftList.size();
    }

    public interface onShiftTypeClickListener{
        void onItemClick(int position,ShiftTypeBean shiftTypeBean);
    }

    public onShiftTypeClickListener getListener() {
        return listener;
    }

    public void setListener(onShiftTypeClickListener listener) {
        this.listener = listener;
    }

}

