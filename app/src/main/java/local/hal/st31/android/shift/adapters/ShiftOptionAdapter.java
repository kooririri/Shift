package local.hal.st31.android.shift.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import local.hal.st31.android.shift.R;
import local.hal.st31.android.shift.beans.ShiftAttrBean;
import local.hal.st31.android.shift.beans.ShiftTypeBean;

public class ShiftOptionAdapter extends RecyclerView.Adapter<ShiftOptionAdapter.ShiftOptionViewHolder> {

    static class ShiftOptionViewHolder extends RecyclerView.ViewHolder{
        TextView optionBlock;
        public ShiftOptionViewHolder(@NonNull View itemView) {
            super(itemView);
            optionBlock = itemView.findViewById(R.id.shift_option);
        }
    }
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ShiftTypeBean> shiftList;

    public ShiftOptionAdapter( Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }
    public void setShiftList(List<ShiftTypeBean> shiftList){
        this.shiftList = shiftList;
    }

    @NonNull
    @Override
    public ShiftOptionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.grid_shift_option,viewGroup,false);
        ShiftOptionViewHolder shiftOptionViewHolder = new ShiftOptionViewHolder(view);
        return shiftOptionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftOptionViewHolder shiftOptionViewHolder, int i) {
        ShiftTypeBean res = shiftList.get(i);
        shiftOptionViewHolder.optionBlock.setText(res.getTypeName());
    }

    @Override
    public int getItemCount() {
        return shiftList == null ? 0 : shiftList.size();
    }


}

//class ShiftOptionViewHolder extends RecyclerView.ViewHolder{
//    TextView optionBlock;
//
//    public ShiftOptionViewHolder(@NonNull View itemView) {
//        super(itemView);
//        optionBlock = itemView.findViewById(R.id.shift_option);
//    }
//}
