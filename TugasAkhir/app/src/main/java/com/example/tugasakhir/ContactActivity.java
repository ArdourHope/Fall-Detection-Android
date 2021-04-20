package com.example.tugasakhir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    private Button AddContact,DeleteContact;
    private ListView mListView;

    DatabaseHelper mDatabasehelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mDatabasehelper = new DatabaseHelper(this);

        AddContact = findViewById(R.id.Addcontact);
        DeleteContact = findViewById(R.id.Deletecontact);

        mListView = (ListView) findViewById(R.id.listView);

        populateListView();
        AddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(ContactActivity.this, AddContact.class);
                ContactActivity.this.startActivity(myIntent);
            }
        });
        DeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabasehelper.onReset();
                finish();
                Intent myIntent = new Intent(ContactActivity.this, MainActivity.class);
                ContactActivity.this.startActivity(myIntent);
            }
        });



    }

    private void populateListView() {

        //get the data and append to a list
        Cursor data = mDatabasehelper.getData();
        data.moveToFirst();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView

    }
}
