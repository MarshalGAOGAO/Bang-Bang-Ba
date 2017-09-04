package com.example.bangbangba;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HelpModule extends AppCompatActivity {
    private String Title;
    private String Phone;
    private String Price;
    private String Content;
    private String Token;
    private String Deadline;
    private long category;

    private List<String> list = new ArrayList<String>();

    private Spinner mySpinner;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findhelp);

        Button backButton = (Button)findViewById(R.id.button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list.add("1-跑腿");
        list.add("2-悬赏提问");
        list.add("3-学习辅导");
        list.add("4-技术服务");
        list.add("5-生活服务");
        list.add("6-其他");

        mySpinner = (Spinner)findViewById(R.id.category);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=adapter.getItemId(position);

                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(HelpModule.this,"请选择服务分类",Toast.LENGTH_SHORT).show();
                parent.setVisibility(View.VISIBLE);
            }
        });



        Button post = (Button) findViewById(R.id.commit_order);
        post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(HelpModule.this);
                builder .setTitle("确认发布？");
                builder.setMessage("发布后将无法修改");
                builder .setPositiveButton("取消", null);
                builder .setNegativeButton("确定",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EditText title =(EditText)findViewById(R.id.title);
                                    EditText phone =(EditText)findViewById(R.id.phone);
                                    EditText price =(EditText)findViewById(R.id.price);
                                    EditText content =(EditText)findViewById(R.id.content);
                                    EditText deadline =(EditText)findViewById(R.id.deadline);

                                    Title=title.getText().toString();
                                    Phone=phone.getText().toString();
                                    Price=price.getText().toString();
                                    Content=content.getText().toString();
                                    Token = "$2y$10$v5TNNyHCkC1IhG1XFdIdbO4MGhYUDoZA3fZ2z5SFEjdr3r";
                                    Deadline= deadline.getText().toString();

                                    Log.d("cat", String.valueOf(category));

                                    OkHttpClient client= new OkHttpClient();
                            /*JSONObject json=new JSONObject();
                            try {
                                json.put("phone","15968804215");
                                json.put("token","$2y$10$v5TNNyHCkC1IhG1XFdIdbO4MGhYUDoZA3fZ2z5SFEjdr3r");
                                json.put("title","求帮忙取快递");
                                json.put("category",0);
                                json.put("content","取快递");
                                json.put("close_time","2017-07-06 12:00:00");
                                json.put("money","2.50");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/

                           /* String json="{"+"\n"+
                                    "\"token\":\"$2y$10$v5TNNyHCkC1IhG1XFdIdbO4MGhYUDoZA3fZ2z5SFEjdr3r\"," +
                                    "\n"+
                                    "\"phone\":\"15968804215\"," +"\n"+
                                    "\"title\":\"求帮忙取快递\","+"\n"+
                                    "\"category\": \"0\" ,"+"\n"+
                                    "\"content\":\"内容是取快递\","+"\n"+
                                    "\"close_time\":\"2017-07-06 12:00:00\","+"\n"+
                                    "\"money\":\"2.50\""+"\n"+
                                    "}";
                            final MediaType JSON = MediaType.parse("application/json;null");
                            RequestBody body = RequestBody.create(JSON,json.toString());

                            Request request = new Request.Builder()
                                    .addHeader("Content-Type","application/json")
                                    .url("http://Bang.cloudshm.com/askForHelp/createOrder")
                                    .post(body)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseData =response.body().string();
                            showResponse(responseData); */

                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("token",Token)
                                            .add("phone",Phone)
                                            .add("title",Title)
                                            .add("category", String.valueOf(category+1))
                                            .add("content",Content)
                                            .add("close_time",Deadline)
                                            .add("money",Price)
                                            .build();
                                    Request request = new Request.Builder()
                                            .addHeader("Content-Type","application/json")
                                            .url("http://Bang.cloudshm.com/askForHelp/createOrder")
                                            .post(requestBody)
                                            .build();
                                    Response response = client.newCall(request).execute();
                                    String responseData =response.body().string();
                                    showResponse(responseData);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }).show();
            }
        });
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("create",response);
            }

        });
    }
}
