package com.example.registerloogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText phoneET;
    private EditText phoneCodeET;
    private Button getCode;
    private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register1);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        phoneET = (EditText) findViewById(R.id.ed_phoneid);
        phoneCodeET = (EditText) findViewById(R.id.ed_phonepassword);
        getCode = (Button) findViewById(R.id.btn_huoqu);
        register = (Button) findViewById(R.id.btn_register1);

        getCode.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_huoqu:
                String phone = phoneET.getText().toString();
                //if (phone.isEmpty()) {
                    //Toast.makeText(RegisterActivity.this, "请输入手机号！", Toast.LENGTH_SHORT).show();
                //} else {
                    //Toast.makeText(RegisterActivity.this, "验证码已发送！", Toast.LENGTH_SHORT).show();
                    getPassword(phone);
                //}
                break;
            case R.id.btn_register1:
                String same_phone = phoneET.getText().toString();
                String code = phoneCodeET.getText().toString();
                //Toast.makeText(RegisterActivity.this,"mhello",Toast.LENGTH_SHORT).show();
                if (code.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "请输入验证码！", Toast.LENGTH_SHORT).show();
                } else {
                    sendCode(same_phone, code);
                }
            default:
                break;
        }
    }

    private void getPassword(final String phone) {

        String baseUrl = "http://Bang.cloudshm.com/registerAndLogin/getCode";
        final String my_url = baseUrl + "?phone=" + phone;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(my_url)
                            .addHeader("phone", phone)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Result result = parseJSONWithGSON(responseData);
                    showResultToast(result);
                    //Log.d("HELLO", responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private Result parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        Result result = gson.fromJson(jsonData, Result.class);
        //Log.d("HELLO", result.getMsg());
        //Log.d("HELLO", result.getStatus());
        return result;
    }

    private void resultToast(Result result) {
        switch (result.getStatus()) {
            case "400":
                Toast.makeText(RegisterActivity.this, "请输入手机号！", Toast.LENGTH_SHORT).show();
                break;
            case "404":
                Toast.makeText(RegisterActivity.this, "该手机号已被注册！", Toast.LENGTH_SHORT).show();
                break;
            case "200":
                Toast.makeText(RegisterActivity.this, "短信发送成功！", Toast.LENGTH_SHORT).show();
                break;
            case "402":
                Toast.makeText(RegisterActivity.this, "手机格式错误或短信发送失败！", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void showResultToast(final Result result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultToast(result);
            }
        });
    }

    private void sendCode(final String phone, final String code) {

        String baseUrl = "http://Bang.cloudshm.com/registerAndLogin/verify";
        final String my_url = baseUrl + "?phone=" + phone + "&code=" + code;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(my_url)
                            .addHeader("phone", phone)
                            .addHeader("code", code)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Result result = parseJSONWithGSON(responseData);
                    showFinalResult(result);
                    //Log.d("HELLO", responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void resultCodeToast(Result result) {
        switch (result.getStatus()) {
            case "200":
                String phone = phoneET.getText().toString();
                Intent intent = new Intent(RegisterActivity.this, setPasswordActivity.class);
                intent.putExtra("extra_phone", phone);
                startActivity(intent);
                break;
            case "402":
                Toast.makeText(RegisterActivity.this, "验证码已过期，请重新获取！", Toast.LENGTH_SHORT).show();
                break;
            case "404":
                Toast.makeText(RegisterActivity.this, "验证码错误！", Toast.LENGTH_SHORT).show();
                break;
            case "400":
                Toast.makeText(RegisterActivity.this, "请将手机号填写完整！", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void showFinalResult(final Result result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultCodeToast(result);
            }
        });
    }

}
