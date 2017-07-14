package com.example.registerloogin;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findhelp);

        Button post = (Button) findViewById(R.id.commit_order);
        post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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
                            final int category=0;
                            Price=price.getText().toString();
                            Content=content.getText().toString();
                            Token = "$2y$10$v5TNNyHCkC1IhG1XFdIdbO4MGhYUDoZA3fZ2z5SFEjdr3r";
                            Deadline= deadline.getText().toString();

                           // Log.d("test",Deadline);



                            OkHttpClient client= new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("phone","15968804215")
                                    .add("token","$2y$10$v5TNNyHCkC1IhG1XFdIdbO4MGhYUDoZA3fZ2z5SFEjdr3r")
                                    .add("title","求帮忙取快递")
                                    .add("category", String.valueOf(0))
                                    .add("content","内容是取快递")
                                    .add("close_time","2017-07-06 12:00:00")
                                    .add("money","00000000")
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
