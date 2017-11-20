package com.example.douchebag.da_project_android.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.douchebag.da_project_android.R;
import com.example.douchebag.da_project_android.service.Receiver;

import java.text.DateFormat;
import java.util.Calendar;

import static android.app.AlarmManager.INTERVAL_DAY;

public class SettingActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private FrameLayout frameLayout;
    private ScrollView scrollView;
    private TextView txtCode, txtNotification;
    private EditText editNotification;
    private Switch switchCode, switchNotification;

    private boolean code, notification;
    private int hour, minute;
    String strHour, strMinute;

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
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        txtCode = (TextView) findViewById(R.id.txtCode);
        txtNotification = (TextView) findViewById(R.id.txtNotification);
        editNotification = (EditText) findViewById(R.id.editNotification);
        switchCode = (Switch) findViewById(R.id.swtCode);
        switchNotification = (Switch) findViewById(R.id.swtNotification);

        sharedpreferences = getSharedPreferences("CODE_DIRECTORY", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        scrollView.smoothScrollTo(0, 0);

        code = sharedpreferences.getBoolean("CODE_ENABLE", false);
        notification = sharedpreferences.getBoolean("NOTIFICATION", false);
        hour = sharedpreferences.getInt("HOUR", 0);
        minute = sharedpreferences.getInt("MINUTE", 0);

        if((hour + "").length() == 1) { strHour = "0" + hour; }
        else { strHour = hour + ""; }

        if((minute + "").length() == 1) { strMinute = "0" + minute; }
        else { strMinute = minute + ""; }

        editNotification.setText(strHour + ":" + strMinute);

        txtCode.setText("Code Lock : " + status(code));
        if(code) switchCode.setChecked(true);

        txtNotification.setText("Notification : " + status(notification));
        if(notification){
            switchNotification.setChecked(true);
        } else {
            frameLayout.setVisibility(frameLayout.INVISIBLE);
        }

        switchCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!sharedpreferences.getString("CODE", "").equals("")) {
                    txtCode.setText("Code Lock : " + status(b));
                    switchCode.setChecked(b);
                    editor.putBoolean("CODE_ENABLE", b);
                    editor.commit();
                }else {
                    switchCode.setChecked(!b);
                    new android.support.v7.app.AlertDialog.Builder(SettingActivity.this)
                            .setMessage(getResources().getString(R.string.popup))
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(SettingActivity.this, CreateCodeActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                txtNotification.setText("Notification : " + status(b));
                switchNotification.setChecked(b);
                editor.putBoolean("NOTIFICATION", b);
                editor.commit();
                notification = b;

                if(notification){
                    frameLayout.setVisibility(frameLayout.VISIBLE);
                    notification(true);
                } else {
                    frameLayout.setVisibility(frameLayout.GONE);
                    notification(false);
                }
            }
        });
    }

    private String status(boolean status){
        if(status)
            return "Enable";
        else
            return "Disable";
    }

    public void createCode(View view){
        Intent intent = new Intent(this, CreateCodeActivity.class);
        startActivity(intent);
    }

    public void setTimeNotification(View view){
        TimePickerDialog timePickerDialog = new TimePickerDialog(SettingActivity.this,
                AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                editor.putInt("HOUR", h);
                editor.putInt("MINUTE", m);
                editor.commit();
                hour = h;
                minute = m;

                if((hour + "").length() == 1) { strHour = "0" + hour; }
                else { strHour = hour + ""; }

                if((minute + "").length() == 1) { strMinute = "0" + minute; }
                else { strMinute = minute + ""; }

                editNotification.setText(strHour + ":" + strMinute);
                notification(true);
            }
        }, hour, minute, true);
        timePickerDialog.setTitle("SELECE TIME");
        timePickerDialog.show();
    }

    private void notification(boolean statusReq){
        Intent intent = new Intent(this, Receiver.class);
        intent.putExtra("ACTION", "NOTIFICATION");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 00);

        pendingIntent = PendingIntent.getBroadcast(this, 1234, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(statusReq) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL_DAY, pendingIntent);
            Log.d("NOTIFICATIONTEST", "1");
        } else {
            alarmManager.cancel(pendingIntent);
            Log.d("NOTIFICATIONTEST", "-1");
        }
    }
}
