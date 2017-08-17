package com.example.registerloogin;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.tencent.open.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

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
    private Button buttonQQLogin;
    private Button buttonRegister;
    private EditText editUsername;
    private EditText editPassword;
    private String status;
    private String msg;
    //private String data;
    //private String token;
    private Tencent mTencent;
    private static boolean isServerSideLogin = false;

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
        buttonQQLogin = (Button) findViewById(R.id.btn_qqlogin);
        buttonQQLogin.setOnClickListener(new MyListener());
    }



    private void loginSendInformation() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String userName = editUsername.getText().toString();
                    String password = editPassword.getText().toString();

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://Bang.cloudshm.com/registerAndLogin/login")
                            .addHeader("phone",userName)
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
            /**
             * token解析
             */
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
                            //Toast.makeText(LoadActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                            String phone = editUsername.getText().toString();
                            //Intent intent = new Intent(LoadActivity.this, OrderPageActivity.class);
                            //intent.putExtra("extra_token", token);
                            Intent m_intent = new Intent(LoadActivity.this, HomePageActivity.class);
                            m_intent.putExtra("extra_phone", phone);
                            startActivity(m_intent);
                            break;
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    IUiListener loginListener = new BaseUiListener() {

    };


    private void qqLogin(){
        mTencent = Tencent.createInstance("1106260747", this.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
            isServerSideLogin = false;
            Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        } else {
            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
                mTencent.logout(this);
                mTencent.login(this, "all", loginListener);
                isServerSideLogin = false;
                Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                return;
            }
            mTencent.logout(this);
           // updateUserInfo();
            // updateLoginButton();
        }
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
              //  Util.showResultDialog(LoadActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
              //  Util.showResultDialog(LoadActivity.this, "返回为空", "登录失败");
                return;
            }
          //  Util.showResultDialog(LoadActivity.this, response.toString(), "登录成功");
            // 有奖分享处理
           // handlePrizeShare();
            doComplete((JSONObject)response);

        }

        protected void doComplete(JSONObject values) {
            Intent m_intent = new Intent(LoadActivity.this, HomePageActivity.class);
            startActivity(m_intent);
        }

        @Override
        public void onError(UiError e) {
           /*  Util.toastMessage(LoadActivity.this, "onError: " + e.errorDetail);
            Util.dismissDialog();*/
        }

        @Override
        public void onCancel() {
           /* Util.toastMessage(LoadActivity.this, "onCancel: ");
            Util.dismissDialog();
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }*/
        }
    }

    public class MyListener implements View.OnClickListener {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_login:
                    loginSendInformation();
                    break;
                case R.id.btn_register:
                    //Toast.makeText(LoadActivity.this, "mhello", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoadActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    break;
                case  R.id.btn_qqlogin:
                    qqLogin();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
    }

}