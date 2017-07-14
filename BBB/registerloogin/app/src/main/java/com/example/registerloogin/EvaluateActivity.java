package com.example.registerloogin;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EvaluateActivity extends AppCompatActivity {

    private Button buttonStar_1;
    private Button buttonStar_2;
    private Button buttonStar_3;
    private Button buttonStar_4;
    private Button buttonStar_5;
    private Button buttonSubmit;
    int star=0;



    private String status;
    private String msg;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        id = intent.getIntExtra("extra_id",0);

        buttonStar_1 = (Button) findViewById(R.id.star_1);
        buttonStar_2 = (Button) findViewById(R.id.star_2);
        buttonStar_3 = (Button) findViewById(R.id.star_3);
        buttonStar_4 = (Button) findViewById(R.id.star_4);
        buttonStar_5 = (Button) findViewById(R.id.star_5);
        buttonSubmit = (Button) findViewById(R.id.submitEvaluate);
        buttonSubmit.setOnClickListener(new MyListener());
        buttonStar_1.setOnClickListener(new MyListener());
        buttonStar_2.setOnClickListener(new MyListener());
        buttonStar_3.setOnClickListener(new MyListener());
        buttonStar_4.setOnClickListener(new MyListener());
        buttonStar_5.setOnClickListener(new MyListener());
    }

    private void submitSendInformation() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("phone","18645234817")
                            .add("token","$2y$10$v5TNNyHCkC1IhG1XFdIdbO4MGhYUDoZA3fZ2z5SFEjdr3r")
                            .add("id", String.valueOf(id))
                            .add("star", String.valueOf(star))
                            .build();
                    Request request = new Request.Builder()
                            .url("http://bang.cloudshm.com/order/comment")
                            .post(requestBody)
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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (status) {
                        case "400":
                            Toast.makeText(EvaluateActivity.this, "参数缺失！", Toast.LENGTH_SHORT).show();
                            break;
                        case "404":
                            if(msg=="order not exists")
                                Toast.makeText(EvaluateActivity.this, "订单不存在！", Toast.LENGTH_SHORT).show();
                            else Toast.makeText(EvaluateActivity.this, "⽤户不存在！", Toast.LENGTH_SHORT).show();
                            break;
                        case "402":
                            Toast.makeText(EvaluateActivity.this, "评价失败！", Toast.LENGTH_SHORT).show();
                            break;
                        case "200":
                            Toast.makeText(EvaluateActivity.this, "评价成功！", Toast.LENGTH_SHORT).show();
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
                case R.id.star_1:
                    star=1;
                    buttonStar_1.setBackgroundResource(R.drawable.star_2);
                    buttonStar_2.setBackgroundResource(R.drawable.star_1);
                    buttonStar_3.setBackgroundResource(R.drawable.star_1);
                    buttonStar_4.setBackgroundResource(R.drawable.star_1);
                    buttonStar_5.setBackgroundResource(R.drawable.star_1);
                    break;
                case R.id.star_2:
                    star=2;
                    buttonStar_1.setBackgroundResource(R.drawable.star_2);
                    buttonStar_2.setBackgroundResource(R.drawable.star_2);
                    buttonStar_3.setBackgroundResource(R.drawable.star_1);
                    buttonStar_4.setBackgroundResource(R.drawable.star_1);
                    buttonStar_5.setBackgroundResource(R.drawable.star_1);
                    break;
                case R.id.star_3:
                    star=3;
                    buttonStar_1.setBackgroundResource(R.drawable.star_2);
                    buttonStar_2.setBackgroundResource(R.drawable.star_2);
                    buttonStar_3.setBackgroundResource(R.drawable.star_2);
                    buttonStar_4.setBackgroundResource(R.drawable.star_1);
                    buttonStar_5.setBackgroundResource(R.drawable.star_1);
                    break;
                case R.id.star_4:
                    star=4;
                    buttonStar_1.setBackgroundResource(R.drawable.star_2);
                    buttonStar_2.setBackgroundResource(R.drawable.star_2);
                    buttonStar_3.setBackgroundResource(R.drawable.star_2);
                    buttonStar_4.setBackgroundResource(R.drawable.star_2);
                    buttonStar_5.setBackgroundResource(R.drawable.star_1);
                    break;
                case R.id.star_5:
                    star=5;
                    buttonStar_1.setBackgroundResource(R.drawable.star_2);
                    buttonStar_2.setBackgroundResource(R.drawable.star_2);
                    buttonStar_3.setBackgroundResource(R.drawable.star_2);
                    buttonStar_4.setBackgroundResource(R.drawable.star_2);
                    buttonStar_5.setBackgroundResource(R.drawable.star_2);
                    break;
                case R.id.submitEvaluate:
                    submitSendInformation();
                    break;

            }
        }
    }

}
