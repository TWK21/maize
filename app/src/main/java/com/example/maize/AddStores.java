package com.example.maize;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class AddStores extends AppCompatActivity {

    private String[] nameList = {"abc","def","ghi"};
    private AutoCompleteTextView url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stores);

        url = (AutoCompleteTextView) findViewById(R.id.URL);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameList);
        url.setAdapter(adapter);
        url.setThreshold(1);
        url.setAdapter(adapter);
    }
}
