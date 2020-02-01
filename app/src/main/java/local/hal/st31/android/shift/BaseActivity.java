package local.hal.st31.android.shift;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import local.hal.st31.android.shift.utils.GlobalUtils;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalUtils.getInstance().addActivity(this);

    }

}
