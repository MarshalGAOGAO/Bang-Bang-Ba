package com.example.registerloogin;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class setPasswordActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register2);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }



        final EditText editText1 =(EditText)findViewById(R.id.set_password1);
        final EditText editText2 =(EditText)findViewById(R.id.set_password2);

        Button identityActivity = (Button) findViewById(R.id.btn_next2);
        Intent intent = getIntent();
        final String phone = intent.getStringExtra("extra_phone");
        identityActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (editText1.getText().toString().equals(editText2.getText().toString())&&
                        editText1.getText().toString().length()!=0&&editText2.getText().toString().length()!=0){
                    String psw = editText2.getText().toString();
                    Intent intent= new Intent(setPasswordActivity.this, identityActivity.class);
                    intent.putExtra("password",psw);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                } else if (editText1.getText().toString().length()==0&&
                        editText2.getText().toString().length()==0)
                        Toast.makeText(setPasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(setPasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            }
        });





    }
}
