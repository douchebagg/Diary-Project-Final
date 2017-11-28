package com.example.douchebag.da_project_android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.douchebag.da_project_android.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateCodeActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;

    private EditText editCode, editCodeConfirm, editEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_code);
        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init(){
        sharedpreferences = getSharedPreferences("CODE_DIRECTORY", Context.MODE_PRIVATE);
        editCode = (EditText) findViewById(R.id.editCode);
        editCodeConfirm = (EditText) findViewById(R.id.editCodeConfirm);
        editEmail = (EditText) findViewById(R.id.editEmail);
    }

    private static boolean emailValidFormat(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void createCode(View view){
        if(editCode.getText().toString().equalsIgnoreCase(editCodeConfirm.getText().toString())
                && editCode.getText().toString().length() >= 4 && editCode.getText().toString().length() <= 12){
            if(emailValidFormat(editEmail.getText().toString())) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CODE", editCode.getText().toString());
                editor.putBoolean("CODE_STATUS", true);
                editor.putString("EMAIL_FOR_FORGET", editEmail.getText().toString());
                editor.commit();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else{
                editEmail.requestFocus();
                Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT).show();
            }
        }else if(editCode.getText().toString().length() < 4 ||
                editCode.getText().toString().length() > 12){
            editCode.setText("");
            editCodeConfirm.setText("");
            editCode.requestFocus();

            Toast.makeText(this, "Password must be 4-12 characters.", Toast.LENGTH_SHORT).show();
        }else{
            editCode.setText("");
            editCodeConfirm.setText("");
            editCode.requestFocus();

            Toast.makeText(this, "Oops. Passwords don't match.", Toast.LENGTH_SHORT).show();
        }
    }
}