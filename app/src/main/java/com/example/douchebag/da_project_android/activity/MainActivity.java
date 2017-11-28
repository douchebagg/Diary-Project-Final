package com.example.douchebag.da_project_android.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.example.douchebag.da_project_android.R;
import com.example.douchebag.da_project_android.java.*;
import com.example.douchebag.da_project_android.service.DatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private DatabaseHelper databaseHelper;

    private ArrayList<Diary> theList = new ArrayList<>();

    private EditText editSearch;
    private ListView listDiary;

    @Override
    protected void onCreate(Bundle savedInstenceState){
        super.onCreate(savedInstenceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onStop(){
        super.onStop();
        theList.clear();
        listDiary.requestLayout();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showList();
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

    public void setting(View view){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void showList(){
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
            do{
                theList.add(new Diary(
                        data.getInt(data.getColumnIndex("id")),
                        data.getString(data.getColumnIndex("header")),
                        data.getString(data.getColumnIndex("content")),
                        data.getString(data.getColumnIndex("date"))
                ));
            } while (data.moveToNext());
        }
    }

    public void createDiary(View view){
        Intent intent = new Intent(this, CreateDiaryActivity.class);
        startActivity(intent);
    }

    public void editDiary(String id, String[] item){
        Intent intent = new Intent(this, EditDiaryActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("header", item[0]);
        intent.putExtra("content", item[1]);
        intent.putExtra("date", item[2]);
        startActivity(intent);
    }

    public void deleteDiary(String id){
        databaseHelper.deleteDiary(id);
        showList();
    }

    class AdapterListView extends ArrayAdapter {

        private Activity activity;

        private HolderListAdaptive holder;

        private ArrayList<Diary> listData;
        private final String[] MONTH = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG",
                "SEP", "OCT", "NOV", "DEC"};

        public AdapterListView(Activity activity, ArrayList<Diary> listData){
            super(activity, 0, listData);
            this.activity = activity;
            this.listData = listData;
        }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            holder = new HolderListAdaptive();
            final String[] array = listData.get(position).getData().split("\\/");
            String month = setMonth(array[1]);

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                convertView = inflater.inflate(R.layout.adaptive_listview, parent, false);
            }

            holder.layout = convertView.findViewById(R.id.layout);
            holder.textMonth = convertView.findViewById(R.id.textMonth);
            holder.textDay = convertView.findViewById(R.id.textDay);
            holder.textHeader = convertView.findViewById(R.id.textHead);
            holder.textContent = convertView.findViewById(R.id.textContent);
            holder.menu = convertView.findViewById(R.id.menu);
            holder.menuLayout = convertView.findViewById(R.id.menuLayout);

            if (position % 2 != 0) {
                holder.layout.setBackgroundResource(R.drawable.m_list_orenge);
                holder.menu.setBackgroundResource(R.drawable.fc_dot);
                holder.textHeader.setTextColor(Color.parseColor("#ffffff"));
                holder.textContent.setTextColor(Color.parseColor("#ffffff"));
            }else{
                holder.layout.setBackgroundResource(R.drawable.m_list_white);
                holder.menu.setBackgroundResource(R.drawable.fc_dot);
                holder.textHeader.setTextColor(Color.parseColor("#000000"));
                holder.textContent.setTextColor(Color.parseColor("#000000"));
            }

            holder.textMonth.setText(month);
            holder.textDay.setText(array[0]);

            ArrayList<String> content = new ArrayList<>();
            for(String test : listData.get(position).getContent().split("\n")) {
                content.add(test);
            }

            if (listData.get(position).getHeader().length() > 15) {
                holder.textHeader.setText(listData.get(position).getHeader().substring(0, 16) + " ...");
            } else {
                holder.textHeader.setText(listData.get(position).getHeader());
            }

            if (content.get(0).length() > 30) {
                holder.textContent.setText(content.get(0).substring(0, 31) + " ....");
            } else {
                if(content.size() == 1)
                    holder.textContent.setText(content.get(0));
                else
                    holder.textContent.setText(content.get(0) + " ....");
            }

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ViewDiaryActivity.class);
                    intent.putExtra("id", listData.get(position).getId());
                    intent.putExtra("header", listData.get(position).getHeader());
                    intent.putExtra("content", listData.get(position).getContent());
                    intent.putExtra("date", listData.get(position).getData());
                    startActivity(intent);
                }
            });

            holder.menuLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.menu.callOnClick();
                }
            });

            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(getContext(), view);
                    popup.inflate(R.menu.popup_menu);
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                            switch (menuItem.getItemId()){
                                case R.id.menuEdit:
                                    editDiary(
                                            listData.get(position).getId() + "",
                                            new String[]{
                                                    listData.get(position).getHeader(),
                                                    listData.get(position).getContent(),
                                                    listData.get(position).getData()
                                            }
                                    );
                                    break;
                                case R.id.menuDelete:
                                    deleteDiary(listData.get(position).getId() + "");
                                    break;
                            }
                            return true;
                        }
                    });
                }
            });

            convertView.setTag(holder);
            return convertView;
        }

        private String setMonth(String month){
            switch (month){
                case "1": month = MONTH[0]; break;
                case "2": month = MONTH[1]; break;
                case "3": month = MONTH[2]; break;
                case "4": month = MONTH[3]; break;
                case "5": month = MONTH[4]; break;
                case "6": month = MONTH[5]; break;
                case "7": month = MONTH[6]; break;
                case "8": month = MONTH[7]; break;
                case "9": month = MONTH[8]; break;
                case "10": month = MONTH[9]; break;
                case "11": month = MONTH[10]; break;
                case "12": month = MONTH[11]; break;
            }
            return month;
        }
    }
}