package com.example.gabriel.inventory_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gabriel.inventory_project.Inventory_pg.thing.Info.ThingInfoActivity;
import com.example.gabriel.inventory_project.Inventory_pg.thing.Thing;
import com.example.gabriel.inventory_project.Inventory_pg.thing.ThingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private EditText search_edit_text;
    private ImageButton search_ib;
    private ListView result_list;
    private ArrayList<Thing> things;
    private ThingAdapter thingAdapter;
    String searchText;
    private String id_Office;
    private String name_Office;
    private String id_Floor;
    private String name_Floor;
    private String id_Room;
    private String name_Room;
    private String id_Thing;
    private String name_Thing;
    private String type_Thing;
    private String price_Thing;
    private String date_of_add_Thing;
    private String date_of_delete_Thing;
    private String id_image;
    private ArrayList<QRCode> qrcodes;
    private ArrayList<QRCode> qrcodes_result;
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        search_edit_text = findViewById(R.id.search_edit_text);
        searchText = search_edit_text.getText().toString().trim();
        myRef = FirebaseDatabase.getInstance().getReference(userId);
        search_ib = findViewById(R.id.search_ib);
        result_list = findViewById(R.id.result_list);
        qrcodes = new ArrayList<QRCode>();
        qrcodes_result = new ArrayList<QRCode>();
        things = new ArrayList<Thing>();


        search_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = search_edit_text.getText().toString().trim();
                if(searchText!=null) {
                    //Toast.makeText(SearchActivity.this, searchText+"!", Toast.LENGTH_LONG).show();
                    //ArrayList<Thing> hh = new ArrayList<>();
                    //hh.add(new Thing("sdf","sfgsg","sdfsdf","sdfsfd", 2));
                    //hh.add(new Thing("sdf","sfgsg","sdfsdf","sdfsfd", 2));
                    search_Things(searchText);
                    //thingAdapter = new ThingAdapter(SearchActivity.this, hh);
                    //result_list.setAdapter(thingAdapter);
                }
            }
        });

        result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Thing thing = things.get(position);
                Intent intent  = new Intent(SearchActivity.this, ThingInfoActivity.class);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        myRef.child("QRCodes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot QRCSnapshot: dataSnapshot.getChildren()){
                    QRCode qrcode = QRCSnapshot.getValue(QRCode.class);
                    qrcodes.add(qrcode);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void search_Things(String searchText){
        things.clear();
        for(QRCode qrCode: qrcodes){
            String[] arrayId = qrCode.getQrcode().split("/");
            id_Office = arrayId[0];
            name_Office = arrayId[1];
            id_Floor = arrayId[2];
            name_Floor = arrayId[3];
            id_Room = arrayId[4];
            name_Room = arrayId[5];
            id_Thing = arrayId[6];
            name_Thing = arrayId[7];
            type_Thing = arrayId[8];
            price_Thing = arrayId[9];
            date_of_add_Thing = arrayId[10];
            date_of_delete_Thing = arrayId [11];
            id_image = arrayId[12];
            Toast.makeText(SearchActivity.this, ""+name_Thing.equals(search_edit_text), Toast.LENGTH_LONG).show();
            if(name_Thing.equals(searchText)){
                //Toast.makeText(SearchActivity.this, name_Thing, Toast.LENGTH_LONG).show();
                //qrcodes_result.add(qrCode);
                things.add(new Thing(id_Thing, name_Thing, type_Thing, price_Thing, date_of_add_Thing,
                       date_of_delete_Thing,id_image));
           }
        }
        thingAdapter = new ThingAdapter(SearchActivity.this, things);
        result_list.setAdapter(thingAdapter);
        //i=-1;
        //searchHelper();

    }

    private void searchHelper(){
        i++;
        String[] arrayId = qrcodes.get(i).getQrcode().split("/");
        if(i==qrcodes.size()){
            thingAdapter = new ThingAdapter(SearchActivity.this, things);
            result_list.setAdapter(thingAdapter);
        }else {
            id_Office = arrayId[0];
            name_Office = arrayId[1];
            id_Floor = arrayId[2];
            name_Floor = arrayId[3];
            id_Room = arrayId[4];
            name_Room = arrayId[5];
            id_Thing = arrayId[6];
            name_Thing = arrayId[7];
            myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor).child("Rooms")
                    .child(id_Room).child("Things")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot thingSnapshot : dataSnapshot.getChildren()) {
                                Thing thing = thingSnapshot.getValue(Thing.class);
                                if (thing.getName().equals(searchText)) {
                                    thing.setPuth(id_Office,name_Office,id_Floor,name_Floor,
                                            id_Room,name_Room);
                                    things.add(thing);

                                }
                                searchHelper();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
    }
}
