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

public class ChangeRoom extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private EditText ETname;
    private Button Badd;
    private String id_Office;
    private String id_Floor;
    private String id_Room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room);
        Intent intent = getIntent();
        id_Office = intent.getStringExtra("id_Office");
        id_Floor = intent.getStringExtra("id_Floor");
        id_Room = intent.getStringExtra("id_Room");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);

        ETname = findViewById(R.id.et_room_name);
        Badd = findViewById(R.id.add_room);
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
        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {
            //Saving the Office change
            myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor)
                    .child("Rooms").child(id_Room).child("name").setValue(name);
            Toast.makeText(this, "Name change", Toast.LENGTH_LONG).show();
            ETname.setText("");
        }else{
            Toast.makeText(this, "Please enter a change", Toast.LENGTH_LONG).show();
        }
    }
}
