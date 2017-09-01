package com.example.registerloogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.IOException;
import circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class orderDetail1 extends AppCompatActivity {
    private String status;
    private String  msg;
    private String title;
    private String content;
    private String servant;
    private String applicant_name;
    private String money;
    private String time;
    private String deadline;
    private String applicantPhone;
    private String servantPhone;
    private int id;
    private String phone;
    private int state;
    private Button button;
    InformationAdapter context_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        id = intent.getIntExtra("extra_id",0);
        phone=intent.getStringExtra("extra_phone");
        state = intent.getIntExtra("extra_state", 0);

        Button backButton =(Button)findViewById(R.id.back);
        button = (Button) findViewById(R.id.cancel);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CircleImageView icon;
        icon= (CircleImageView) findViewById(R.id.circleImageView);


        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        //.addHeader("token","$2y$10$v5TNNyHCkC1IhG1XFdIdbO4MGhYUDoZA3fZ2z5SFEjdr3rUL")
                        .addHeader("phone",phone)
                        .addHeader("id", String.valueOf(id))
                        .url("http://Bang.cloudshm.com/order/showDetail")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData= response.body().string();
                    parseJSONWithJSONObject1(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private void parseJSONWithJSONObject1(String jsonData){
        try{
            JSONObject jsonObject = new JSONObject(jsonData);

            msg=jsonObject.getString("msg");
            status= jsonObject.getString("status");
            Log.d("MARSHAL", status);



            if ( status.equals("200") ){
                JSONObject newData1= jsonObject.getJSONObject("data1");
                JSONObject newData2 = jsonObject.getJSONObject("data2");

                state=newData1.getInt("state");
                title= newData1.getString("title");
                content= newData1.getString("content");
                money = newData1.getString("money");
                time= newData1.getString("updated_at");
                deadline= newData1.getString("close_time");
                applicantPhone=newData1.getString("applicant");
                servantPhone=newData1.getString("servant");
                servant = newData2.getString("servant_name");
                applicant_name = newData2.getString("applicant_name");
            }




            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView Title = (TextView) findViewById(R.id.title);
                    TextView Content = (TextView) findViewById(R.id.content);
                    TextView Name = (TextView) findViewById(R.id.name);
                    TextView Price = (TextView) findViewById(R.id.price);
                    TextView Phone = (TextView) findViewById(R.id.phone);
                    TextView Time1 = (TextView) findViewById(R.id.order_time2);
                    TextView Time2 = (TextView) findViewById(R.id.deadline2);
                    TextView State= (TextView) findViewById(R.id.order_status);




                    if (status.equals("404")){
                        Title.setText("空");
                        Content.setText("订单信息为空哦~亲可以回到订单页面刷新重试哦~");
                        Name.setText("发起人/接单人:无/无");
                        Price.setText("0");
                        Phone.setText("空");
                        Time1.setText("空");
                        Time2.setText("空");
                        State.setText("空");


                    }else{
                        Title.setText(title);
                        Content.setText(content);
                        Name.setText("发起人/接单人："+applicant_name+"/"+servant);
                        Price.setText(money+"元");
                        Phone.setText("发起人"+":"+applicantPhone+"/"+"接单人"+":"+servantPhone);
                        Time1.setText(time);
                        Time2.setText(deadline);
                        switch (state){
                            case 1:
                                State.setText("等待接单");
                                break;
                            case 2:
                                State.setText("正在服务");
                                break;
                            case 3:
                                State.setText("服务完成");
                                break;
                            default:
                                State.setText("服务完成");
                                break;
                        }

                    }

                    if(state==2){
                        button.setText("正在服务");
                    }else if(state==3){
                        button.setText("立即评价");
                        button.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                Intent m_intent = new Intent(orderDetail1.this,EvaluateActivity.class);
                                m_intent.putExtra("extra_id", id);
                                m_intent.putExtra("extra_phone", phone);
                                orderDetail1.this.startActivity(m_intent);
                                finish();
                            }
                        });
                    } else if (state == 1){
                        button.setText("取消订单");
                        button.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(orderDetail1.this);
                                builder .setTitle("确认取消？");
                                builder.setMessage("取消将扣除您的信用值");
                                builder .setPositiveButton("取消", null);
                                builder .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {


                                                OkHttpClient client= new OkHttpClient();
                                                RequestBody requestBody= new FormBody.Builder()
                                                        .add("phone",phone)
                                                        //.add("token","$2y$10$v5TNNyHCkC1IhG1XFdIdbO4MGhYUDoZA3fZ2z5SFEjdr3r")
                                                        .add("id", String.valueOf(id))
                                                        .build();
                                                Request request = new Request.Builder()
                                                        .addHeader("Content-Type","application/json")
                                                        .url("http://Bang.cloudshm.com/order/cancelOrder")
                                                        .delete(requestBody)
                                                        .build();
                                                try {
                                                    Response response = client.newCall(request).execute();
                                                    String responseData =response.body().string();
                                                    parseJSONWithJSONObject2(responseData);
                                                    finish();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }).start();

                                    }
                                }).show();
                            }
                        });
                    } else {
                        button.setText("");
                    }



                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parseJSONWithJSONObject2(String jsonData) {

        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            status = jsonObject.getString("status");
            msg=jsonObject.getString("msg");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("cancel",status+":"+msg);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
