package com.example.douchebag.da_project_android.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.douchebag.da_project_android.R;
import com.example.douchebag.da_project_android.service.Receiver;

import java.util.Calendar;

import static android.app.AlarmManager.INTERVAL_DAY;

public class SettingActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    private Switch switchCode, switchNotification;

    private boolean code, notification;

    @Override
    protected void onCreate(Bundle savedInstenceState){
        super.onCreate(savedInstenceState);
        setContentView(R.layout.activity_settting);
        init();
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

    private void init(){
        switchCode = (Switch) findViewById(R.id.switchCode);
        switchNotification = (Switch) findViewById(R.id.switchNotification);

        sharedpreferences = getSharedPreferences("CODE_DIRECTORY", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        code = sharedpreferences.getBoolean("CODE_ENABLE", false);
        notification = sharedpreferences.getBoolean("NOTIFICATION", false);

        if(code){ switchCode.setChecked(true); }
        if(notification){
            switchNotification.setChecked(true);
            createNotification();
        }

        switchCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchCode.setChecked(b);
                editor.putBoolean("CODE_ENABLE", b);
                editor.commit();
            }
        });

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchNotification.setChecked(b);
                editor.putBoolean("NOTIFICATION", b);
                editor.commit();
                notification = b;

                if(notification) createNotification();
            }
        });
    }

    public void createCode(View view){
        switchCode.setChecked(true);
        Intent intent = new Intent(this, CreateCodeActivity.class);
        startActivity(intent);
    }

    public void createNotification(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Receiver.class);
        intent.putExtra("ACTION", "NOTIFICATION");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 21);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL_DAY, pendingIntent);
    }
}
