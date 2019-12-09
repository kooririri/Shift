package local.hal.st31.android.shift.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.Date;

import local.hal.st31.android.shift.NewSelfShiftAddActivity;
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
    private SwipeRecyclerView swipeRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_home,container,false);
        _helper = new DatabaseHelper(getContext());

//        handleRecyclerView();
        return fragmentView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        ImageView imgAdd = fragmentView.findViewById(R.id.btn_add);
//        imgAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), NewSelfShiftAddActivity.class);
//                intent.putExtra("date",selectedDate);
//                startActivity(intent);
//            }
//        });
//    }


    @Override
    public void onStart() {
        super.onStart();
        initFragmentView();
        ImageView imgAdd = fragmentView.findViewById(R.id.btn_add);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewSelfShiftAddActivity.class);
                intent.putExtra("date",selectedDate);
                startActivity(intent);
                selfScheduleAdapter. notifyDataSetChanged();
            }
        });
    }

    //Viewを初期化
    private void initFragmentView() {
        selfScheduleRecyclerView = fragmentView.findViewById(R.id.self_schedule_recyclerView);
        swipeRecyclerView = fragmentView.findViewById(R.id.swipe_menu);
        calendarView = fragmentView.findViewById(R.id.calendarView);
        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.addDecorators(new ShiftDecorator());
        dateLabel = fragmentView.findViewById(R.id.date_label);
        calendarView.setSelectedDate(CalendarDay.today());
        selectedDate = DateUtils.date2String(new Date(),"yyyy-MM-dd");
        dateLabel.setText(selectedDate);

        db = _helper.getWritableDatabase();
        ArrayList<SelfScheduleBean> data = DataAccess.selfScheduleSelectByDate(db, selectedDate);
        selfScheduleAdapter = new SelfScheduleAdapter(getContext());
        selfScheduleAdapter.setData(data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//横並び
        selfScheduleRecyclerView.setLayoutManager(layoutManager);
        swipeRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        selfScheduleRecyclerView.setAdapter(selfScheduleAdapter);




        calendarView.setOnDateChangedListener(new OnDateSelectedListener(){
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                db = _helper.getWritableDatabase();
                selectedDate = date.getDate().toString();
                dateLabel.setText(selectedDate);
                ArrayList<SelfScheduleBean> data = DataAccess.selfScheduleSelectByDate(db, selectedDate);
                selfScheduleAdapter = new SelfScheduleAdapter(getContext());
                selfScheduleAdapter.setData(data);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//横並び
                selfScheduleRecyclerView.setLayoutManager(layoutManager);
                selfScheduleRecyclerView.setAdapter(selfScheduleAdapter);

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = getResources().getDimensionPixelSize(R.dimen.bottom_line_height);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;


            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext()).setBackground(R.color.colorPrimary)
                        .setImage(R.drawable.baseline_cached_24)
                        .setText("修正")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

                SwipeMenuItem addItem = new SwipeMenuItem(getContext()).setBackground(R.color.A400red)
                        .setImage(R.drawable.baseline_delete_24)
                        .setText("削除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
            }
        }
    };

}
