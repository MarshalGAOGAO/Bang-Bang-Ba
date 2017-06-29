package com.example.registerloogin;

import android.content.Intent;
import android.support.v7.app.ActionBar;
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

public class identityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register3);
        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        final String psw=intent.getStringExtra("password");
        final EditText editName =(EditText)findViewById(R.id.set_name1);
        final EditText editIdNum =(EditText)findViewById(R.id.set_idnumber);

        Button post = (Button) findViewById(R.id.btn_next3);
        post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client= new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("phone","18100175452")
                                    .add("password",psw)
                                    .add("name",editName.getText().toString())
                                    .add("id_number",editIdNum.getText().toString())
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://Bang.cloudshm.com/registerAndLogin/register")
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
                Log.d("finalRegister",response);
            }
            
        });
    }

}
