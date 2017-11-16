package com.example.douchebag.da_project_android.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.douchebag.da_project_android.R;
import com.example.douchebag.da_project_android.java.*;
import com.example.douchebag.da_project_android.sqlite.DatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private DatabaseHelper databaseHelper;

    private ArrayList<Diary> theList = new ArrayList<>();

    private EditText editSearch;
    private ListView listDiary;

    @Override
    protected void onCreate(Bundle savedInstenceState){
        super.onCreate(savedInstenceState);
        setContentView(R.layout.activity_main_pm);
        init();
    }

    @Override
    protected void onStop(){
        super.onStop();
        theList.clear();
        listDiary.clearTextFilter();
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
        editSearch = (EditText) findViewById(R.id.editSearch);
        listDiary = (ListView) findViewById(R.id.listDiary);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void afterTextChanged(Editable editable) {
                String keyword = "%" + editSearch.getText().toString() + "%";
                search(keyword);
            }
        });

        databaseHelper = new DatabaseHelper(this);
        showList();
    }

    private void showList(){
        getList(null);
        listDiary.setAdapter(new AdapterListView(this, theList));
    }

    public void search(String keyword){
        getList(keyword);
        listDiary.setAdapter(new AdapterListView(this, theList));
    }

    private void getList(String keyword){
        Cursor data;
        if (keyword == null) {
            data = databaseHelper.getListContent();
        }else{
            data = databaseHelper.getListContentBySearch(keyword);
        }
        theList.clear();

        if(data.getCount() != 0){
            data.moveToFirst();
            while (data.moveToNext()){
                theList.add(new Diary(
                        data.getInt(data.getColumnIndex("id")),
                        data.getString(data.getColumnIndex("header")),
                        data.getString(data.getColumnIndex("content")),
                        data.getString(data.getColumnIndex("date"))
                ));
            }
        }
    }

    public void createDiary(View view){
        Intent intent = new Intent(this, CreateDiaryActivity.class);
        startActivity(intent);
    }

    public void editDiary(int id){
        Log.d("sssss", "EDIT");
    }

    public void deleteDiary(int id){
        boolean result = databaseHelper.deleteDiary(id);
        showList();
    }
}