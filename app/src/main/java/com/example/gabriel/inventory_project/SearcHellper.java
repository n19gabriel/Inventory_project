package com.example.gabriel.inventory_project;

import com.example.gabriel.inventory_project.Inventory_pg.floor.Floor;
import com.example.gabriel.inventory_project.Inventory_pg.office.Office;
import com.example.gabriel.inventory_project.Inventory_pg.room.Room;
import com.example.gabriel.inventory_project.Inventory_pg.thing.Thing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by gabriel on 25.02.18.
 */

public class SearcHellper {
    private ArrayList<Office> offices;
    private ArrayList<Floor> floors;
    private ArrayList<Room> rooms;
    private ArrayList<Thing> things;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    public SearcHellper() {
        innit();
    }

    private  void innit(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);
        offices = new ArrayList<Office>();
        floors = new ArrayList<Floor>();
        rooms = new ArrayList<Room>();
        things = new ArrayList<Thing>();
    }

    private void officeSearch(){
        myRef.child("Offices").getClass();
    }

    public ArrayList<Office> getOffices(){
        officeSearch();
        while (offices.isEmpty()) {}
        return offices;
    }

    public void floorSearch(String id_Office){
        myRef.child("Offices").child(id_Office).child("Floors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                floors.clear();
                for (DataSnapshot floorSnapshot : dataSnapshot.getChildren()) {
                    Floor floor = floorSnapshot.getValue(Floor.class);
                    floors.add(floor);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public ArrayList<Floor> getFloors(String id_Office){
        floorSearch(id_Office);
        while (floors.isEmpty()) {}
        return floors;
    }

    public void roomSearch(String id_Office, String id_Floor){
        myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor).child("Rooms")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        rooms.clear();
                        for(DataSnapshot floorSnapshot: dataSnapshot.getChildren()){
                            Room room = floorSnapshot.getValue(Room.class);
                            rooms.add(room);
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public ArrayList<Room> getRooms(String id_Office, String id_Floor){
        roomSearch(id_Office, id_Floor);
        while (rooms.isEmpty()) {}
        return rooms;
    }

    public void thingSearch(String id_Office, String id_Floor, String id_Room){
        myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor).child("Rooms")
                .child(id_Room).child("Things")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        things.clear();
                        for(DataSnapshot thingSnapshot: dataSnapshot.getChildren()){
                            Thing thing = thingSnapshot.getValue(Thing.class);
                            things.add(thing);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    public ArrayList<Thing> getThings(String id_Office, String id_Floor,String id_Room){
        thingSearch(id_Office, id_Floor, id_Room);
        while (things.isEmpty()) {}
        return things;
    }
}
