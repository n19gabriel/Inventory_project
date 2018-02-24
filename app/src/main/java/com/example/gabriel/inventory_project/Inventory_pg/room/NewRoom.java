package com.example.gabriel.inventory_project.Inventory_pg.room;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gabriel.inventory_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewRoom extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private EditText ETname;
    private Button Badd;
    private String userId;
    private String id_Office;
    private String id_Floor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room);
        Intent intent = getIntent();
        id_Office = intent.getStringExtra("id_Office");
        id_Floor = intent.getStringExtra("id_Floor");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference();

        ETname = findViewById(R.id.et_room_name);
        Badd = findViewById(R.id.add_room);

        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoom();
            }
        });
    }

    private void addRoom() {
        //getting the values to save
        String name = ETname.getText().toString();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            String id = myRef.push().getKey();

            //creating an Room Object
            Room room = new Room(id, name);

            //Saving the Room
            myRef.child(userId).child("Offices").child(id_Office).child("Floors").
                    child(id_Floor).child("Rooms").child(id).setValue(room);

            //setting editText to blank again
            ETname.setText("");

            //displaying a success toast
            Toast.makeText(this, "Room added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}