package com.example.douchebag.da_project_android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.douchebag.da_project_android.R;

public class SendSuccessActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;

    private TextView textSent;

    private String email;

    @Override
    protected void onCreate(Bundle savedIntenceState){
        super.onCreate(savedIntenceState);
        setContentView(R.layout.activity_send_success_pm);
        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, CodeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init(){
        sharedpreferences = getSharedPreferences("CODE_DIRECTORY", Context.MODE_PRIVATE);
        email = sharedpreferences.getString("EMAIL_FOR_FORGET", "");

        textSent = (TextView) findViewById(R.id.textSent);
        textSent.setText("Send email to\n\n" + email + "\n\nsuccessfully");
    }
}
