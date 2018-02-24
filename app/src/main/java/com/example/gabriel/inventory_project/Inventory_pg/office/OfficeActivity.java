package com.example.gabriel.inventory_project.Inventory_pg.office;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gabriel.inventory_project.Inventory_pg.floor.FloorActivity;
import com.example.gabriel.inventory_project.R;
import android.content.Intent;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

/**
 * Created by gabriel on 09.02.18.
 */

public class OfficeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ListView listView;
    private ArrayList<Office> offices;
    private OfficeAdapter officeAdapter;
    private FloatingActionButton Badd;
    private Toolbar toolbar;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_list);

        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Offices");
        searchView = findViewById(R.id.search_view);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);

        offices = new ArrayList<Office>();
        Badd = findViewById(R.id.new_object);
        listView = findViewById(R.id.list);
        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OfficeActivity.this, NewOffice.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Office office = offices.get(position);
                Intent intent  = new Intent(OfficeActivity.this, FloorActivity.class);
                intent.putExtra("id_Office", office.getId());
                intent.putExtra("name_Office", office.getName());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(OfficeActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_menu, null);
                Button refract = mView.findViewById(R.id.refract);
                Button delete = mView.findViewById(R.id.delete);
                final Office office = offices.get(position);
                refract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OfficeActivity.this, ChangeOffice.class);
                        intent.putExtra("id_Office", office.getId());
                        startActivity(intent);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myRef.child("Offices").child(office.getId()).removeValue();
                    }
                });
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                //If closed Search View , lstView will return default
                officeAdapter = new OfficeAdapter(OfficeActivity.this, offices);
                listView.setAdapter(officeAdapter);
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
                    ArrayList<Office> offices_search = new ArrayList<Office>();
                    for(Office office:offices){
                        if(office.getName().contains(newText)|| office.getLocation().contains(newText)){
                            offices_search.add(office);
                        }
                    }
                    officeAdapter = new OfficeAdapter(OfficeActivity.this, offices_search);
                    listView.setAdapter(officeAdapter);
                }
                return false;
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

    @Override
    protected void onStart() {
    super.onStart();
        myRef.child("Offices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                offices.clear();
                for(DataSnapshot officeSnapshot: dataSnapshot.getChildren()){
                    Office office = officeSnapshot.getValue(Office.class);
                    offices.add(office);
                }
                officeAdapter = new OfficeAdapter(OfficeActivity.this, offices);
                listView.setAdapter(officeAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
