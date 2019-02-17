package com.example.maize;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdaptor extends BaseAdapter {

    private ArrayList<Store> array;
    private Context c;

    public CustomAdaptor(Context c, ArrayList<Store> list){
        array = list;
        this.c = c;
    }


    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Store getItem(int i) {
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
            row = inflater.inflate(R.layout.custom_list_view, viewGroup,false);
        }else{
            row = view;
        }

        Store store = getItem(i);
        TextView name = (TextView)row.findViewById(R.id.listViewName);
        TextView address = (TextView)row.findViewById(R.id.listViewStatus);
        ImageView logo = (ImageView)row.findViewById(R.id.imageView);

        if(store.getName().equals("Ralphs")) {
            logo.setImageResource(R.drawable.pic_ralphs_logo);
        }
        else if(store.getName().equals("Vons")){
            logo.setImageResource(R.drawable.pic_vons_logo);
        }

        name.setText(store.getName());
        address.setText(store.getAddress());

        return row;
    }
}
