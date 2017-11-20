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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.douchebag.da_project_android.R;
import com.example.douchebag.da_project_android.service.DatabaseHelper;

public class EditDiaryActivity extends AppCompatActivity {

    private DatabaseHelper database;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Bundle bundle;

    private EditText editDate, editHead, editBody;

    private int year, mount, day;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstenceState){
        super.onCreate(savedInstenceState);
        setContentView(R.layout.activity_edit_diary);
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
        bundle = getIntent().getExtras();

        editDate.setText(bundle.getString("date"));
        editHead.setText(bundle.getString("header"));
        editBody.setText(bundle.getString("content"));

        final String[] mounthSplit = bundle.getString("date").split("\\/");
        year = Integer.parseInt(mounthSplit[2]);
        mount = Integer.parseInt(mounthSplit[1]) - 1;
        day = Integer.parseInt(mounthSplit[0]);

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
                EditDiaryActivity.this,
                AlertDialog.THEME_HOLO_DARK,
                dateSetListener,
                year, mount, day
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void editDiary(View view){
        if(!editHead.getText().toString().equals("") && !editBody.getText().toString().equals("")
                && !editDate.getText().toString().equals("")) {
            String[] data = {
                    editHead.getText().toString(),
                    editBody.getText().toString(),
                    editDate.getText().toString()
            };

            boolean editData = database.editDairy(
                    bundle.getString("id"),
                    new String[]{
                            editHead.getText().toString(),
                            editBody.getText().toString(),
                            editDate.getText().toString()
                    }
            );

            if(editData){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else{
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