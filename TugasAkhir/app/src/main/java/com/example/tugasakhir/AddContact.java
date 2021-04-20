package com.example.tugasakhir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddContact extends AppCompatActivity {

    private Button SaveButton;

    private EditText EtName,EtEmail;

    DatabaseHelper mDatabasehelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        mDatabasehelper = new DatabaseHelper(this);

        EtName = findViewById(R.id.Nametext);
        EtEmail = findViewById(R.id.Numbertext);

        SaveButton = findViewById(R.id.Savebutton);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Etname = EtName.getText().toString();
                String Etemail = EtEmail.getText().toString();

                mDatabasehelper.addData(Etname,Etemail);


                finish();
                Intent myIntent = new Intent(AddContact.this,ContactActivity.class);
                AddContact.this.startActivity(myIntent);
            }
        });
    }
}
