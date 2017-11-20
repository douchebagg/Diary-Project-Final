package com.example.douchebag.da_project_android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.douchebag.da_project_android.R;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;

    private Handler handler;
    private Runnable runnable;

    private boolean code;

    @Override
    protected void onCreate(Bundle savedIntenceState){
        super.onCreate(savedIntenceState);
        setContentView(R.layout.splash_screen);
        init();
    }

    @Override
    public void onResume(){
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onStop(){
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private void init(){
        sharedpreferences = getSharedPreferences("CODE_DIRECTORY", Context.MODE_PRIVATE);
        code = sharedpreferences.getBoolean("CODE_ENABLE", false);
Log.d("start_to", code + "");
        handler = new Handler();
        if(code) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, CodeActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }else{
            runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
        }
    }
}