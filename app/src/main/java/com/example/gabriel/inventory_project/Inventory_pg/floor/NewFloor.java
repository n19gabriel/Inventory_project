package com.example.gabriel.inventory_project.Inventory_pg.floor;

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

public class NewFloor extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private EditText ETname;
    private Button Badd;
    private String userId;
    private String id_Office;
    private AddHistory history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_floor);
        Intent intent = getIntent();
        id_Office = intent.getStringExtra("id_Office");

        history = new AddHistory();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference();

        ETname = findViewById(R.id.et_flor_name);
        Badd = findViewById(R.id.add_floor);

        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFloor();
            }
        });
    }

    private void addFloor() {
        //getting the values to save
        String name = ETname.getText().toString();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            String id = myRef.push().getKey();

            //creating an Floor Object
            Floor floor = new Floor(id, name);

            //Saving the Floor
            myRef.child(userId).child("Offices").child(id_Office).child("Floors").child(id).setValue(floor);

            //setting editText to blank again
            ETname.setText("");

            //displaying a success toast
            Toast.makeText(this, "Floor added", Toast.LENGTH_LONG).show();
            history.addrecord(new Record("Add floor "+floor.getName()));
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}
