package com.example.douchebag.da_project_android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.douchebag.da_project_android.R;
import com.example.douchebag.da_project_android.service.DatabaseHelper;

public class ViewDiaryActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private Bundle bundle;

    private TextView textHead, textDate, textContent;

    private int id;

    @Override
    protected void onCreate(Bundle savedIntenceState){
        super.onCreate(savedIntenceState);
        setContentView(R.layout.activity_view_diary);
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
        textHead = (TextView) findViewById(R.id.textHead);
        textDate = (TextView) findViewById(R.id.textDate);
        textContent = (TextView) findViewById(R.id.textContent);

        databaseHelper = new DatabaseHelper(this);
        bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        textHead.setText(bundle.getString("header"));
        textContent.setText(bundle.getString("content"));
        textDate.setText(bundle.getString("date"));
    }

    public void editDiary(View view){
        Intent intent = new Intent(this, EditDiaryActivity.class);
        intent.putExtra("id", id + "");
        intent.putExtra("header", bundle.getString("header"));
        intent.putExtra("content", bundle.getString("content"));
        intent.putExtra("date", bundle.getString("date"));
        startActivity(intent);
    }

    public void deleteDiary(View view){
        new AlertDialog.Builder(this)
                .setTitle("DELETE DIARY")
                .setMessage("Are you want to delete a diary.")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean deleteData = databaseHelper.deleteDiary(id + "");
                        if(deleteData) {
                            Intent intent = new Intent(ViewDiaryActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ViewDiaryActivity.this, "Oop, Someting went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
