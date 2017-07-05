package com.example.registerloogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/6/27.
 */

public class LoadActivity extends AppCompatActivity  {

    private TextView textView;
    private Button buttonLogin;
    private Button buttonRegister;
    private EditText editUsername;
    private EditText editPassword;
    private String status;
    private String msg;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        editUsername=(EditText) findViewById(R.id.ed_username);
        editPassword=(EditText) findViewById(R.id.ed_password);
        buttonLogin = (Button) findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(new MyListener());
        buttonRegister = (Button) findViewById(R.id.btn_register);
        buttonRegister.setOnClickListener(new MyListener());
    }



    private void loginSendInformation() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String userNmae = editUsername.getText().toString();
                    String password = editPassword.getText().toString();

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://Bang.cloudshm.com/registerAndLogin/login")
                            .addHeader("phone",userNmae)
                            .addHeader("password",password)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private  void parseJSONWithJSONObject(String jsonData){
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            status = jsonObject.getString("status");
            msg = jsonObject.getString("msg");
            if(status=="200"){
                JSONObject data = jsonObject.getJSONObject("data");
                token = data.getString("token");
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (status) {
                        case "404":
                            Toast.makeText(LoadActivity.this, "用户不存在！", Toast.LENGTH_SHORT).show();
                            break;
                        case "402":
                            Toast.makeText(LoadActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                            break;
                        case "200":
                            Toast.makeText(LoadActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyListener implements View.OnClickListener {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_login:
                    loginSendInformation();
                    break;
                case R.id.btn_register:
                    //Toast.makeText(LoadActivity.this, "hello", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoadActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

}