package com.example.registerloogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class registerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register1);

        Button startSetPasswordActivity = (Button) findViewById(R.id.btn_register1);
        startSetPasswordActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent= new Intent(registerActivity.this, setPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}
