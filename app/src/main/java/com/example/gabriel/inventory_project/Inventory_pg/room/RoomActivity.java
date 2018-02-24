package com.example.gabriel.inventory_project.Inventory_pg.room;

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

import com.example.gabriel.inventory_project.Inventory_pg.thing.ThingActivity;
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

public class RoomActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ListView listView;
    private ArrayList<Room> rooms;
    private RoomAdapter roomAdapter;
    private String id_Office;
    private String name_Office;
    private String id_Floor;
    private String name_Floor;
    private FloatingActionButton Badd;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_list);

        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rooms");
        searchView = findViewById(R.id.search_view);

        Intent intent = getIntent();
        id_Office = intent.getStringExtra("id_Office");
        name_Office = intent.getStringExtra("name_Office");
        id_Floor = intent.getStringExtra("id_Floor");
        name_Floor = intent.getStringExtra("name_Floor");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);
        rooms = new ArrayList<Room>();
        Badd = findViewById(R.id.new_object);
        listView = findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Room room = rooms.get(position);
                Intent intent  = new Intent(RoomActivity.this, ThingActivity.class);
                intent.putExtra("id_Office", id_Office);
                intent.putExtra("name_Office", name_Office);
                intent.putExtra("id_Floor", id_Floor );
                intent.putExtra("name_Floor", name_Floor );
                intent.putExtra("id_Room", room.getId());
                intent.putExtra("name_Room", room.getName());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RoomActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_menu, null);
                Button refract = mView.findViewById(R.id.refract);
                Button delete = mView.findViewById(R.id.delete);
                final Room room = rooms.get(position);
                refract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RoomActivity.this, ChangeRoom.class);
                        intent.putExtra("id_Office", id_Office);
                        intent.putExtra("id_Floor", id_Floor);
                        intent.putExtra("id_Room", room.getId());
                        startActivity(intent);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor)
                                .child("Rooms").child(room.getId())
                                .removeValue();
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
                Intent intent = new Intent(RoomActivity.this, NewRoom.class);
                intent.putExtra("id_Office", id_Office);
                intent.putExtra("id_Floor", id_Floor);
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
                roomAdapter = new RoomAdapter(RoomActivity.this, rooms);
                listView.setAdapter(roomAdapter);
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
                    ArrayList<Room> rooms_search = new ArrayList<Room>();
                    for(Room room:rooms){
                        if(room.getName().contains(newText)){
                            rooms_search.add(room);
                        }
                    }
                    roomAdapter = new RoomAdapter(RoomActivity.this, rooms_search);
                    listView.setAdapter(roomAdapter);
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
        myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor).child("Rooms")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rooms.clear();
                for(DataSnapshot floorSnapshot: dataSnapshot.getChildren()){
                    Room room = floorSnapshot.getValue(Room.class);
                    rooms.add(room);
                }
                RoomAdapter roomAdapter = new RoomAdapter(RoomActivity.this, rooms);
                listView.setAdapter(roomAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
