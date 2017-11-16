package com.example.douchebag.da_project_android.java;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import com.example.douchebag.da_project_android.R;
import com.example.douchebag.da_project_android.activity.MainActivity;

import java.util.ArrayList;

public class AdapterListView extends ArrayAdapter{

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

        if (position % 2 != 0) {
            holder.layout.setBackgroundResource(R.drawable.m_list_orenge);
            holder.textHeader.setTextColor(Color.parseColor("#ffffff"));
            holder.textContent.setTextColor(Color.parseColor("#ffffff"));
        }else{
            holder.layout.setBackgroundResource(R.drawable.m_list_white);
            holder.textHeader.setTextColor(Color.parseColor("#000000"));
            holder.textContent.setTextColor(Color.parseColor("#000000"));
        }

        holder.textMonth.setText(month);
        holder.textDay.setText(array[0]);

        if (listData.get(position).getHeader().length() > 15) {
            holder.textHeader.setText(listData.get(position).getHeader().substring(0, 16) + " ...");
        } else {
            holder.textHeader.setText(listData.get(position).getHeader());
        }

        if (listData.get(position).getHeader().length() > 20) {
            holder.textContent.setText(listData.get(position).getContent().substring(0, 31) + " ....");
        } else {
            holder.textContent.setText(listData.get(position).getContent());
        }

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getContext(), view);
                popup.inflate(R.menu.popup_menu);
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        MainActivity main = new MainActivity();
                        switch (menuItem.getItemId()){
                            case R.id.menuEdit: main.editDiary(listData.get(position).getId()); break;
                            case R.id.menuDelete: main.deleteDiary(listData.get(position).getId()); break;
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