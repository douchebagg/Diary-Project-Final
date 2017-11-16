package com.example.douchebag.da_project_android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.douchebag.da_project_android.R;

public class CodeActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;

    private EditText editCode;

    private String validCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_pm);
        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init(){
        editCode = (EditText) findViewById(R.id.editCode);
        sharedpreferences = getSharedPreferences("CODE_DIRECTORY", Context.MODE_PRIVATE);
        validCode = sharedpreferences.getString("CODE", "");
    }

    public void codeValid(View view){
        if(editCode.getText().toString().equalsIgnoreCase(validCode)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Password is incorrect. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void forgetCode(View view) {
        Intent intent = new Intent(this, ForgetCodeActivity.class);
        startActivity(intent);
    }
}
