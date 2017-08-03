package com.example.registerloogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.PrivilegedAction;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class showDetail extends AppCompatActivity {

    private int id;
    private String name;
    private Integer credit;
    private String start_time;
    private String detail;
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showdetail);

        Intent intent = getIntent();
        id = intent.getIntExtra("extra_id",0);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
       if (actionBar!=null){
           actionBar.setDisplayHomeAsUpEnabled(true);
           actionBar.setHomeAsUpIndicator(R.drawable.back);
       }

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .addHeader("id", String.valueOf(id))
                        .url("http://Bang.cloudshm.com/helpOthers/showDetail")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData= response.body().string();
                    parseJSONWithJSONObject(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        Button Require = (Button)findViewById(R.id.require);
        Require.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(showDetail.this);
                builder.setMessage("确认接单？");
                builder.setPositiveButton("取消", null);
                builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }).start();
                    }
                }).show();
            }
        });

    }

    private void parseJSONWithJSONObject(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            int status = jsonObject.getInt("status");

            if (status==200){
                JSONObject newData1= jsonObject.getJSONObject("data1");
                JSONObject newData2 = jsonObject.getJSONObject("data2");

                name= newData2.getString("applicant_name");
                credit=newData2.getInt("applicant_credit");
                start_time=newData1.getString("updated_at");
                detail=newData1.getString("content");
                money=newData1.getString("money");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView Name = (TextView)findViewById(R.id.name);
                        TextView Detail = (TextView)findViewById(R.id.content);
                        TextView Credit = (TextView)findViewById(R.id.credit);
                        TextView Opentime = (TextView)findViewById(R.id.open_time);
                        TextView Money = (TextView)findViewById(R.id.money);


                        Name.setText(name);
                        Detail.setText(detail);
                        Credit.setText(credit.toString());
                        Opentime.setText(start_time);
                        Money.setText(money);



                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //实现左上角返回到主页面
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }


}
