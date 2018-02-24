package com.example.gabriel.inventory_project.Inventory_pg.office;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class NewOffice extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private EditText ETname;
    private EditText ETlocation;
    private Button Badd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_office);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);

        ETname = findViewById(R.id.et_new_name);
        ETlocation = findViewById(R.id.et_location);
        Badd = findViewById(R.id.add_office);

        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOffice();
            }
        });
    }

    private void addOffice() {
        //getting the values to save
        String name = ETname.getText().toString().trim();
        String location = ETlocation.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(location)) {

            //getting a unique id using push().getKey() method
            String id = myRef.push().getKey();

            //creating an Office Object
            Office office = new Office(id, name, location);

            //Saving the Office
            myRef.child("Offices").child(id).setValue(office);

            //setting edittext to blank again
            ETname.setText("");
            ETlocation.setText("");

            //displaying a success toast
            Toast.makeText(this, "Office added", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(name)){
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_LONG).show();
        }
    }
}
