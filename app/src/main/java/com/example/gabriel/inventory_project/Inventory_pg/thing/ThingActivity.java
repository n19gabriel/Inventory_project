package com.example.gabriel.inventory_project.Inventory_pg.thing;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.example.gabriel.inventory_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.melnykov.fab.FloatingActionButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class ThingActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    private ListView listView;
    private ArrayList<Thing> things;
    private ThingAdapter thingAdapter;
    private String id_Office;
    private String name_Office;
    private String id_Floor;
    private String name_Floor;
    private String id_Room;
    private String name_Room;
    private FloatingActionButton Badd;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private AddHistory history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_list);

        history = new AddHistory();

        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Things");
        searchView = findViewById(R.id.search_view);

        Intent intent = getIntent();
        id_Office = intent.getStringExtra("id_Office");
        name_Office = intent.getStringExtra("name_Office");
        id_Floor = intent.getStringExtra("id_Floor");
        name_Floor = intent.getStringExtra("name_Floor");
        id_Room = intent.getStringExtra("id_Room");
        name_Room = intent.getStringExtra("name_Room");


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);
        things = new ArrayList<Thing>();
        Badd = findViewById(R.id.new_object);
        listView = findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Thing thing = things.get(position);
            Intent intent  = new Intent(ThingActivity.this, ThingInfoActivity.class);
            intent.putExtra("id_Office",id_Office);
            intent.putExtra("name_Office", name_Office);
            intent.putExtra("id_Floor",id_Floor);
            intent.putExtra("name_Floor",name_Floor);
            intent.putExtra("id_Room",id_Room);
            intent.putExtra("name_Room",name_Room);
            intent.putExtra("id_Thing",thing.getId());
            intent.putExtra("name_Thing",thing.getName());
            intent.putExtra("type_Thing",thing.getType());
            intent.putExtra("price_Thing",thing.getPrice());
            intent.putExtra("date_of_add_Thing",thing.getDate_of_add());
            intent.putExtra("date_of_delete_Thing",thing.getDate_of_delete());
            intent.putExtra("id_image_Thing",thing.getId_image());
            startActivity(intent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ThingActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_menu, null);
                Button refract = mView.findViewById(R.id.refract);
                Button delete = mView.findViewById(R.id.delete);
                final Thing thing = things.get(position);
                refract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ThingActivity.this, ChangeThing.class);
                        intent.putExtra("id_Office",id_Office);
                        intent.putExtra("id_Floor",id_Floor);
                        intent.putExtra("id_Room",id_Room);
                        intent.putExtra("id_Thing",thing.getId());
                        intent.putExtra("name_Thing",thing.getName());
                        intent.putExtra("type_Thing",thing.getType());
                        intent.putExtra("price_Thing",thing.getPrice());
                        intent.putExtra("id_image_Thing",thing.getId_image());
                        startActivity(intent);

                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(thing.getId_image()!=null) {
                            mStorageRef = FirebaseStorage.getInstance().getReference().child(thing.getId_image());
                            mStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // File deleted successfully
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    //  an error occurred!
                                }
                            });
                        }
                        myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor)
                                .child("Rooms").child(id_Room).child("Things").child(thing.getId())
                                .removeValue();
                        history.addrecord(new Record("Delete thing "+thing.getName()));
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
                Intent intent = new Intent(ThingActivity.this, NewThing.class);
                intent.putExtra("id_Office", id_Office);
                intent.putExtra("id_Floor", id_Floor);
                intent.putExtra("id_Room", id_Room);
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
                thingAdapter = new ThingAdapter(ThingActivity.this, things);
                listView.setAdapter(thingAdapter);
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
                    ArrayList<Thing> things_search = new ArrayList<Thing>();
                    for(Thing thing:things){
                        if(thing.getName().contains(newText)|| thing.getType().contains(newText)){
                            things_search.add(thing);
                        }
                    }
                    thingAdapter = new ThingAdapter(ThingActivity.this, things_search);
                    listView.setAdapter(thingAdapter);
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
                .child(id_Room).child("Things")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        things.clear();
                        for(DataSnapshot thingSnapshot: dataSnapshot.getChildren()){
                            Thing thing = thingSnapshot.getValue(Thing.class);
                            things.add(thing);
                        }
                        thingAdapter = new ThingAdapter(ThingActivity.this, things);
                        listView.setAdapter(thingAdapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}
