package com.example.douchebag.da_project_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.douchebag.da_project_android.R;

public class PopupActivity extends AppCompatActivity {

    Button btConfirm, btCancel;

    @Override
    protected void onCreate(Bundle savedInstenceState){
        super.onCreate(savedInstenceState);
        setContentView(R.layout.activity_popup);

        btConfirm = (Button) findViewById(R.id.btConfirm);
        btCancel = (Button) findViewById(R.id.btCancel);

        Window window = this.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.8f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        getWindow().setLayout(
                (int)(displayMetrics.widthPixels * 0.8),
                (int)(displayMetrics.heightPixels * 0.3)
        );
    }

    public void confirm(View view){
        Intent intent = new Intent(this, CreateCodeActivity.class);
        startActivity(intent);
    }

    public void cancel(View view){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.xml.fadein, R.xml.fadeout);
    }
}
