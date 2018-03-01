package com.example.gabriel.inventory_project.History;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class HistoryActivity extends AppCompatActivity {
    private ArrayList<String> dates;
    private FirebaseAuth mAuth;
    private ListView listView;
    private DatabaseReference myRef;
    private FloatingActionButton Badd;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_list);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);

        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Date");
        searchView = findViewById(R.id.search_view);

        dates = new ArrayList<String>();
        Badd = findViewById(R.id.new_object);
        Badd.setVisibility(View.GONE);
        listView = findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String date = dates.get(position);
                Intent intent  = new Intent(HistoryActivity.this, TimeActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                //If closed Search View , lstView will return default
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(HistoryActivity.this, R.layout.support_simple_spinner_dropdown_item, dates);
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
                    ArrayList<String> date_search = new ArrayList<String>();
                    for(String date:dates){
                        if(date.contains(newText)){
                            date_search.add(date);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(HistoryActivity.this, R.layout.support_simple_spinner_dropdown_item, date_search);
                    listView.setAdapter(adapter);
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        myRef.child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dates.clear();
                for(DataSnapshot officeSnapshot: dataSnapshot.getChildren()){
                    String date = officeSnapshot.getKey().toString();
                    dates.add(date);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(HistoryActivity.this, R.layout.support_simple_spinner_dropdown_item, dates);

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
}

