package com.example.gabriel.inventory_project.Inventory_pg.thing.Info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gabriel.inventory_project.History.HistoryAdapter;
import com.example.gabriel.inventory_project.History.Record;
import com.example.gabriel.inventory_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class TimeThingActivity extends AppCompatActivity {
    private ArrayList<String> times;
    private FirebaseAuth mAuth;
    private ListView listView;
    private DatabaseReference myRef;
    private FloatingActionButton Badd;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private String date;
    private String id_Office;
    private String id_Floor;
    private String id_Room;
    private String id_Thing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_list);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        id_Office = intent.getStringExtra("id_Office");
        id_Floor = intent.getStringExtra("id_Floor");
        id_Room = intent.getStringExtra("id_Room");
        id_Thing = intent.getStringExtra("id_Thing");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);

        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Date");
        searchView = findViewById(R.id.search_view);

        times = new ArrayList<String>();
        Badd = findViewById(R.id.new_object);
        Badd.setVisibility(View.GONE);
        listView = findViewById(R.id.list);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                //If closed Search View , lstView will return default
                HistoryAdapter adapter = new HistoryAdapter(TimeThingActivity.this, times);
                listView.setAdapter(adapter);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    ArrayList<String> time_search = new ArrayList<String>();
                    for(String time:times){
                        if(time.contains(newText)){
                            time_search.add(time);
                        }
                    }
                    HistoryAdapter adapter = new HistoryAdapter(TimeThingActivity.this, time_search);
                    listView.setAdapter(adapter);
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        myRef.child("Offices").child(id_Office).child("Floors").
                child(id_Floor).child("Rooms").child(id_Room).child("Things").child(id_Thing).
                child("History").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                times.clear();
                for(DataSnapshot recordSnapshot: dataSnapshot.getChildren()){
                    Record time = recordSnapshot.getValue(Record.class);
                    times.add(time.getTime()+": "+time.getRecord());
                }
                HistoryAdapter adapter = new HistoryAdapter(TimeThingActivity.this, times);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.action_settings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}

