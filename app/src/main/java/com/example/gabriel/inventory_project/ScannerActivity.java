package com.example.gabriel.inventory_project;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gabriel.inventory_project.Inventory_pg.thing.Thing;
import com.example.gabriel.inventory_project.Inventory_pg.thing.ThingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.encoder.QRCode;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class ScannerActivity extends AppCompatActivity {
    private FloatingActionButton scanner_btn;
    private ArrayList<Thing> things;
    private ThingAdapter thingAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference roomRef;
    private String id_Office;
    private String id_Floor;
    private String id_Room;
    private com.google.zxing.qrcode.encoder.QRCode qrCode;
    private String id_Thing;
    private String QRCode;
    private IntentResult result;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        things = new ArrayList<Thing>();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);
        roomRef = FirebaseDatabase.getInstance().getReference(userId);

        scanner_btn = findViewById(R.id.scanner_btn);
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
                Toast.makeText(ScannerActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                searchQRCode();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void searchQRCode(){
        things.clear();
        myRef.child("Offices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot officeSnapshot: dataSnapshot.getChildren()){
                    id_Office = officeSnapshot.getKey().toString();
                    myRef.child("Offices").child(id_Office).child("Floors").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot floorSnapshot: dataSnapshot.getChildren()){
                                id_Floor=floorSnapshot.getKey().toString();
                                myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor).child("Rooms")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot roomSnapshot: dataSnapshot.getChildren()){
                                                    id_Room=roomSnapshot.getKey().toString();
                                                    //Toast.makeText(ScannerActivity.this, roomSnapshot.getKey()+" Room", Toast.LENGTH_LONG).show();
                                                    myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor).child("Rooms")
                                                            .child(id_Room).child("Things").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot thingSnapshot: dataSnapshot.getChildren()){
                                                                id_Thing = dataSnapshot.getKey();
                                                                Thing thing = thingSnapshot.getValue(Thing.class);

                                                                if(result.getContents().equals(thing.getId())){
                                                                    Toast.makeText(ScannerActivity.this, thingSnapshot.getKey()+" Thing", Toast.LENGTH_LONG).show();
                                                                    things.add(thing);
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });
                                                }
                                            }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                //Toast.makeText(ScannerActivity.this, floorSnapshot.getKey()+" floor", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
                thingAdapter = new ThingAdapter(ScannerActivity.this, things);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
