package com.example.douchebag.da_project_android.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.douchebag.da_project_android.R;
import com.example.douchebag.da_project_android.service.DatabaseHelper;

import java.util.Calendar;

public class CreateDiaryActivity extends AppCompatActivity{

    private DatabaseHelper database;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private EditText editDate, editHead, editBody;

    private int year, mount, day;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstenceState){
        super.onCreate(savedInstenceState);
        setContentView(R.layout.activity_create_diary);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init(){
        editDate = (EditText) findViewById(R.id.editDate);
        editHead = (EditText) findViewById(R.id.editHead);
        editBody = (EditText) findViewById(R.id.editBody);
        database = new DatabaseHelper(this);

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        mount = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        setDateSetListener();
    }

    private void setDateSetListener(){
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d){
                day = d;
                year = y;
                mount = m;

                editDate.setText(day + "/" + (mount + 1) + "/" + year);
            }
        };
    }

    public void setDate(View view){
        DatePickerDialog dialog = new DatePickerDialog(
                CreateDiaryActivity.this,
                AlertDialog.THEME_HOLO_DARK,
                dateSetListener,
                year, mount, day
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void addDiary(View view){
        if(!editHead.getText().toString().equals("") && !editBody.getText().toString().equals("")
                && !editDate.getText().toString().equals("")) {
            String[] data = {
                    editHead.getText().toString(),
                    editBody.getText().toString(),
                    editDate.getText().toString()
            };

            boolean insertData = database.addDiary(data);

            if (insertData) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Oop, Someting went wrong.", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(editHead.getText().toString().equals("") && editBody.getText().toString().equals("")
                    && editDate.getText().toString().equals("")) {
                Toast.makeText(this, "Can't save, Please input some data.", Toast.LENGTH_SHORT).show();
            }else if(editDate.getText().toString().equals("")){
                Toast.makeText(this, "Date is empty.", Toast.LENGTH_SHORT).show();
            }else if (editHead.getText().toString().equals("")){
                Toast.makeText(this, "Header is empty.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Content is empty..", Toast.LENGTH_SHORT).show();
            }
        }
    }
}