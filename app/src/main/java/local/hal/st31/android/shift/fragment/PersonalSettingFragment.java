package local.hal.st31.android.shift.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.Toast;

import local.hal.st31.android.shift.BlackListActivity;
import local.hal.st31.android.shift.R;


public class PersonalSettingFragment extends Fragment {
    private View fragmentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       fragmentView = inflater.inflate(R.layout.fragment_personal_setting,container,false);
       init();
       return fragmentView;

    }

    private void init(){
        TableRow blackListRow = fragmentView.findViewById(R.id.blackListBlock);
        blackListRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BlackListActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
