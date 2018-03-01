package com.example.gabriel.inventory_project.Inventory_pg.office;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gabriel.inventory_project.History.AddHistory;
import com.example.gabriel.inventory_project.History.Record;
import com.example.gabriel.inventory_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeOffice extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private EditText ETname;
    private EditText ETlocation;
    private Button Badd;
    private String id_Office;
    private String office_name;
    private String office_location;
    private AddHistory history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_office);

        Intent intent = getIntent();
        id_Office = intent.getStringExtra("id_Office");
        office_name = intent.getStringExtra("name_Office");
        office_location = intent.getStringExtra("location_Office");
        history = new AddHistory();


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);

        ETname = findViewById(R.id.et_new_name);
        ETlocation = findViewById(R.id.et_location);
        Badd = findViewById(R.id.add_office);
        Badd.setText("Save change");
        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChange();
            }
        });
    }
    private void addChange() {
        //getting the values to save
        String name = ETname.getText().toString();
        String location = ETlocation.getText().toString();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {
            //Saving the Office change
            myRef.child("Offices").child(id_Office).child("name").setValue(name);
            Toast.makeText(this, "Name change", Toast.LENGTH_LONG).show();
            ETname.setText("");
        }
        if (!TextUtils.isEmpty(location)) {
            //Saving the Office change
            myRef.child("Offices").child(id_Office).child("location").setValue(location);
            Toast.makeText(this, "Location change", Toast.LENGTH_LONG).show();
            ETlocation.setText("");
        }
        if(TextUtils.isEmpty(name)&&TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please enter a change", Toast.LENGTH_LONG).show();
        }else if(!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(location)){
            history.addrecord(new Record("Refract office: "+ office_name + " => "+name+
            " and "+ office_location + " => " + location));
        }else if(!TextUtils.isEmpty(name)){
            history.addrecord(new Record("Refract office: "+ office_name + " => "+name));
        }else {
            history.addrecord(new Record("Refract office: "+ office_location + " => " + location));
        }
    }
}
