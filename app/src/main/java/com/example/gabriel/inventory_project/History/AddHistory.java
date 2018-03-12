package com.example.gabriel.inventory_project.History;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gabriel on 28.02.18.
 */

public class AddHistory {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String date;
    public AddHistory() {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy");
        date = simpleDateFormat.format(calendar.getTime()).toString();
    }

    public void addrecord(Record record){
        myRef.child("History").child(date).child(record.getTime()).setValue(record);
    }
    public void addrecordThing(Record record,String id_Office, String id_Floor, String id_Room,
    String id_Thing){
        myRef.child("Offices").child(id_Office).child("Floors").
                child(id_Floor).child("Rooms").child(id_Room).child("Things").child(id_Thing).
                child("History").child(date).child(record.getTime()).setValue(record);
    }
}
