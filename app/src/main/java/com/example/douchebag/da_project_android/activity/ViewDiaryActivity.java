package com.example.douchebag.da_project_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.douchebag.da_project_android.R;

public class ViewDiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedIntenceState){
        super.onCreate(savedIntenceState);
        setContentView(R.layout.activity_view_diary);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
