package com.example.gabriel.inventory_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.gabriel.inventory_project.Inventory_pg.floor.Floor;
import com.example.gabriel.inventory_project.Inventory_pg.office.Office;
import com.example.gabriel.inventory_project.Inventory_pg.room.Room;
import com.example.gabriel.inventory_project.Inventory_pg.thing.Thing;
import com.example.gabriel.inventory_project.Inventory_pg.thing.Info.ThingInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class ScannerActivity extends AppCompatActivity {
    private FloatingActionButton scanner_btn;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ArrayList<Office> offices;
    private ArrayList<Floor> floors;
    private ArrayList<Room> rooms;
    private ArrayList<Thing> things;
    private String id_Office;
    private String name_Office;
    private String id_Floor;
    private String name_Floor;
    private String id_Room;
    private String name_Room;
    private String id_Thing;
    private IntentResult result;
    private SearcHellper searcHellper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("finish", false)) finish();
        setContentView(R.layout.activity_scanner);

        offices = new ArrayList<Office>();
        floors = new ArrayList<Floor>();
        rooms = new ArrayList<Room>();
        things = new ArrayList<Thing>();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);
        scanner_btn = findViewById(R.id.scanner_btn);
        searcHellper = new SearcHellper();
        final Activity activity = this;

        scanner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(ScannerActivity.this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else{
                getThing();
                //Toast.makeText(ScannerActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                //searchQRCode();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void getThing(){
        String[] arrayId = result.getContents().toString().split("/");
        id_Office = arrayId[0];
        name_Office = arrayId[1];
        id_Floor = arrayId[2];
        name_Floor = arrayId[3];
        id_Room = arrayId[4];
        name_Room = arrayId[5];
        id_Thing = arrayId[6];
        myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor).child("Rooms")
                .child(id_Room).child("Things")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot thingSnapshot: dataSnapshot.getChildren()){
                            Thing thing = thingSnapshot.getValue(Thing.class);
                            if(id_Thing.equals(thing.getId())){
                                Intent intent  = new Intent(ScannerActivity.this, ThingInfoActivity.class);
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
                                startActivity(intent);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    private void searchQRCode() {
        offices = searcHellper.getOffices();
        Toast.makeText(ScannerActivity.this,offices.size()+" ", Toast.LENGTH_LONG).show();
        for(Office office:offices){
            Toast.makeText(ScannerActivity.this,office.getName()+" office", Toast.LENGTH_LONG).show();
            id_Office = office.getId();
            name_Office = office.getName();
            floors = searcHellper.getFloors(id_Office);
            for (Floor floor:floors){
                Toast.makeText(ScannerActivity.this,floor.getName()+" floors", Toast.LENGTH_LONG).show();
                id_Floor = floor.getId();
                name_Floor = floor.getName();
                rooms = searcHellper.getRooms(id_Office, id_Floor);
                for (Room room:rooms){
                    Toast.makeText(ScannerActivity.this,room.getName()+" rooms", Toast.LENGTH_LONG).show();
                    id_Room = room.getId();
                    name_Room = room.getName();
                    things = searcHellper.getThings(id_Office, id_Floor, id_Room);
                    for (Thing thing:things){
                        Toast.makeText(ScannerActivity.this,thing.getName()+ "things", Toast.LENGTH_LONG).show();
                        if(result.getContents().equals(thing.getId())){
                            Intent intent = new Intent(ScannerActivity.this, ThingInfoActivity.class);
                            intent.putExtra("id_Office", id_Office);
                            intent.putExtra("name_Office", name_Office);
                            intent.putExtra("id_Floor", id_Floor);
                            intent.putExtra("name_Floor", name_Floor);
                            intent.putExtra("id_Room", id_Room);
                            intent.putExtra("name_Room", name_Room);
                            intent.putExtra("id_Thing", thing.getId());
                            intent.putExtra("name_Thing", thing.getName());
                            intent.putExtra("type_Thing", thing.getType());
                            intent.putExtra("price_Thing", thing.getPrice());
                            intent.putExtra("date_of_add_Thing", thing.getDate_of_add());
                            intent.putExtra("date_of_delete_Thing", thing.getDate_of_delete());
                            startActivity(intent);
                            break;
                        }
                    }
                }
            }
        }
       // Toast.makeText(ScannerActivity.this,"lol", Toast.LENGTH_LONG).show();
    }
}
