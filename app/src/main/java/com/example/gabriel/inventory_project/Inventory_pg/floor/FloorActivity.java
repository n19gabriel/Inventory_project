package com.example.gabriel.inventory_project.Inventory_pg.floor;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gabriel.inventory_project.History.AddHistory;
import com.example.gabriel.inventory_project.History.Record;
import com.example.gabriel.inventory_project.Inventory_pg.room.RoomActivity;
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

public class FloorActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ListView listView;
    private ArrayList<Floor> floors;
    private String id_Office;
    private String name_Office;
    private FloatingActionButton Badd;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private FloorAdapter floorAdapter;
    private AddHistory history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_list);

        history = new AddHistory();

        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Floors");
        searchView = findViewById(R.id.search_view);

        Intent intent = getIntent();
        id_Office = intent.getStringExtra("id_Office");
        name_Office = intent.getStringExtra("name_Office");
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);
        floors = new ArrayList<Floor>();
        Badd = findViewById(R.id.new_object);
        listView = findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Floor floor = floors.get(position);
                Intent intent  = new Intent(FloorActivity.this, RoomActivity.class);
                intent.putExtra("id_Office", id_Office);
                intent.putExtra("name_Office", name_Office);
                intent.putExtra("id_Floor", floor.getId());
                intent.putExtra("name_Floor", floor.getName());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(FloorActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_menu, null);
                Button refract = mView.findViewById(R.id.refract);
                Button delete = mView.findViewById(R.id.delete);
                final Floor floor = floors.get(position);
                refract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FloorActivity.this, ChangeFloor.class);
                        intent.putExtra("id_Office", id_Office);
                        intent.putExtra("id_Floor", floor.getId());
                        intent.putExtra("name_Floor", floor.getName());
                        startActivity(intent);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myRef.child("Offices").child(id_Office).child("Floors").child(floor.getId())
                                .removeValue();
                        history.addrecord(new Record("Delete floor "+floor.getName()));
                    }
                });
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                return false;
            }
        });
        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FloorActivity.this, NewFloor.class);
                intent.putExtra("id_Office", id_Office);
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
                floorAdapter = new FloorAdapter(FloorActivity.this, floors);
                listView.setAdapter(floorAdapter);
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
                    ArrayList<Floor> floors_search = new ArrayList<Floor>();
                    for(Floor floor:floors){
                        if(floor.getName().contains(newText)){
                            floors_search.add(floor);
                        }
                    }
                    floorAdapter = new FloorAdapter(FloorActivity.this, floors_search);
                    listView.setAdapter(floorAdapter);
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
        myRef.child("Offices").child(id_Office).child("Floors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                floors.clear();
                for (DataSnapshot floorSnapshot : dataSnapshot.getChildren()) {
                    Floor floor = floorSnapshot.getValue(Floor.class);
                    floors.add(floor);
                }
                floorAdapter = new FloorAdapter(FloorActivity.this, floors);
                listView.setAdapter(floorAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
