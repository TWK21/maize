package com.example.maize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdaptorItems extends BaseAdapter {

    private ArrayList<ShopItem> array;
    private Context c;

    public CustomAdaptorItems(Context c, ArrayList<ShopItem> list){
        array = list;
        this.c = c;
    }


    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public ShopItem getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = null;
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null){
            row = inflater.inflate(R.layout.custom_list_view_item, viewGroup,false);
        }else{
            row = view;
        }

        ShopItem store = getItem(i);
        TextView name = (TextView)row.findViewById(R.id.listViewName);
        TextView address = (TextView)row.findViewById(R.id.listViewStatus);

        name.setText(store.getName());
        address.setText(store.getDescription());

        return row;
    }
}
