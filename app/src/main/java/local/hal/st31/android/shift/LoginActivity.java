package local.hal.st31.android.shift;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private static final String ACCESS_URL = "http://10.0.2.2/test/index.php";
    private String userName;
    private String password;
    private String nickName = null;
    EditText etUserName;
    EditText etPassword;
    private int success_flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        init();
    }

    private void init() {
        etUserName = findViewById(R.id.userName);
        etPassword = findViewById(R.id.password);

        SharedPreferences sp = getSharedPreferences("login", getApplicationContext().MODE_PRIVATE);
        String savedUserName = sp.getString("userName",null);
        String savedPassword = sp.getString("password",null);

        if(savedUserName != null && savedPassword !=null){
            LoginThread loginThread = new LoginThread();
            loginThread.execute(ACCESS_URL,savedUserName,savedPassword);
        }
    }

    public void loginButtonClick(View view){
        userName = etUserName.getText().toString();
        password = etPassword.getText().toString();
        LoginThread loginThread = new LoginThread();
        loginThread.execute(ACCESS_URL,userName,password);
    }

    private class LoginThread extends AsyncTask<String, Void, String> {
        private static final String DEBUG_TAG = "loginThread";

        @Override
        protected String doInBackground(String... params) {
            String uri = params[0];
            String strUserName = params[1];
            String strPassword = params[2];

            String postData ="userName="+strUserName+"&password="+strPassword;
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";

            try {
                URL url = new URL(uri);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Content-Type", "application/json; utf-8"); // 追記
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                con.setConnectTimeout(2000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();
                int status = con.getResponseCode();
                if(status != 200){
                    throw new IOException("ステータスコード："+status);
                }
                is = con.getInputStream();
                result = is2String(is);
            } catch (MalformedURLException ex) {
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            } catch (IOException ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                    }
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            String user = null;
            String pass = null;
            try {
                JSONObject object = new JSONObject(result);
                user = object.getString("username");
                pass = object.getString("password");
                nickName = object.getString("nickName");
                success_flag = object.getInt("successFlag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences sp = getSharedPreferences("login", getApplicationContext().MODE_PRIVATE);
            sp.edit().putString("userName",user).putString("password",pass).apply();
            if(success_flag == 1){
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("nickName",nickName);
                startActivity(intent);
            }

        }

    }



    private String is2String(InputStream is) throws IOException {
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
