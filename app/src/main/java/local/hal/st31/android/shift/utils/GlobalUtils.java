package local.hal.st31.android.shift.utils;

import android.content.Context;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import local.hal.st31.android.shift.MainActivity;
import local.hal.st31.android.shift.fragment.HomeFragment;

public class GlobalUtils {

    private static GlobalUtils instance;
    public Context context;
    public MainActivity mainActivity;
    public HomeFragment homeFragment;
    public Map<String, JSONArray> kaburuMap;

    public Map<String, JSONArray> getKaburuMap() {
        return kaburuMap;
    }

    public void setKaburuMap(Map<String, JSONArray> kaburuMap) {
        this.kaburuMap = kaburuMap;
    }

    public static GlobalUtils getInstance() {
        if(instance == null){
            instance = new GlobalUtils();
        }
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String is2String(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        char[] b = new char[1024];
        int line;
        while (0 <= (line = reader.read(b))) {
            sb.append(b, 0, line);
        }
        return sb.toString();
    }
}
