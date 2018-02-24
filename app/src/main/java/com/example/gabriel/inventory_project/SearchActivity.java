package com.example.gabriel.inventory_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gabriel.inventory_project.Inventory_pg.floor.Floor;
import com.example.gabriel.inventory_project.Inventory_pg.floor.FloorAdapter;
import com.example.gabriel.inventory_project.Inventory_pg.office.Office;
import com.example.gabriel.inventory_project.Inventory_pg.office.OfficeActivity;
import com.example.gabriel.inventory_project.Inventory_pg.office.OfficeAdapter;
import com.example.gabriel.inventory_project.Inventory_pg.room.Room;
import com.example.gabriel.inventory_project.Inventory_pg.thing.Thing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ArrayList<Office> offices;
    private ArrayList<Floor> floorss;
    private ArrayList<Room> rooms;
    private ArrayList<Thing> things;
    private OfficeAdapter officeAdapter;
    private FloorAdapter floorAdapter;
    private EditText search_edit_text;
    private ImageButton search_ib;
    private ListView list_offices;
    private String searchText;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_edit_text = findViewById(R.id.search_edit_text);
        searchText = search_edit_text.getText().toString().trim();
        search_ib = findViewById(R.id.search_ib);
        list_offices = findViewById(R.id.list_office);
        offices = new ArrayList<Office>();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);

        search_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchText!=null) {
                    search_offices(searchText);
                }
            }
        });
    }

    private void search_offices(String searchText) {
        Query query = myRef.child("Offices").orderByChild("name").equalTo(search_edit_text.getText().toString().trim());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                offices.clear();
                for(DataSnapshot officeSnapshot: dataSnapshot.getChildren()){
                    Office office = officeSnapshot.getValue(Office.class);
                    offices.add(office);
                    Toast.makeText(SearchActivity.this,officeSnapshot.getKey().toString(),Toast.LENGTH_LONG).show();
                }
                officeAdapter = new OfficeAdapter(SearchActivity.this, offices);
                list_offices.setAdapter(officeAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
