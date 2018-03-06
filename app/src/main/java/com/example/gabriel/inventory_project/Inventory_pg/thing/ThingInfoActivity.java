package com.example.gabriel.inventory_project.Inventory_pg.thing;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gabriel.inventory_project.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ThingInfoActivity extends AppCompatActivity {

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

    private ImageView image_things;
    private TextView TV_name;
    private TextView TV_type;
    private TextView TV_price;
    private TextView TV_path;
    private TextView TV_date_of_add;
    private TextView TV_date_of_delete;
    private Button delete_thing;
    private Button refract_thing;

    private StorageReference mStorageRef;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_info);
        if (getIntent().getBooleanExtra("finish", false)) finish();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);

        Intent intent = getIntent();
        id_Office = intent.getStringExtra("id_Office");
        name_Office = intent.getStringExtra("name_Office");
        id_Floor = intent.getStringExtra("id_Floor");
        name_Floor = intent.getStringExtra("name_Floor");
        id_Room = intent.getStringExtra("id_Room");
        name_Room = intent.getStringExtra("name_Room");
        id_Thing = intent.getStringExtra("id_Thing");
        name_Thing = intent.getStringExtra("name_Thing");
        type_Thing = intent.getStringExtra("type_Thing");
        price_Thing = intent.getStringExtra("price_Thing");
        date_of_add_Thing = intent.getStringExtra("date_of_add_Thing");
        date_of_delete_Thing = intent.getStringExtra("date_of_delete_Thing");
        id_image = intent.getStringExtra("id_image_Thing");

        image_things = findViewById(R.id.image_things);
        if(id_image!=null) {
            mStorageRef = FirebaseStorage.getInstance().getReference().child(id_image);
            Glide.with(ThingInfoActivity.this)
                    .using(new FirebaseImageLoader())
                    .load(mStorageRef)
                    .into(image_things);
        }else {
            image_things.setVisibility(View.GONE);
        }

        TV_name = findViewById(R.id.TV_name);
        TV_name.setText("Name: "+name_Thing);
        TV_type = findViewById(R.id.TV_type);
        TV_type.setText("Type: "+type_Thing);
        TV_price = findViewById(R.id.TV_price);
        TV_price.setText("Price: "+price_Thing+"$");
        TV_path = findViewById(R.id.TV_path);
        TV_path.setText("Path: "+name_Office+"/"+name_Floor+"/"+name_Room+"/"+name_Thing);
        TV_date_of_add = findViewById(R.id.TV_date_of_add);
        TV_date_of_add.setText("Date of add: "+date_of_add_Thing);
        TV_date_of_delete = findViewById(R.id.TV_date_of_delete);
        TV_date_of_delete.setText("Date of delete: "+date_of_delete_Thing);

        delete_thing = findViewById(R.id.delete_thing);
        delete_thing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id_image!=null) {
                    mStorageRef = FirebaseStorage.getInstance().getReference().child(id_image);
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
                        .child("Rooms").child(id_Room).child("Things").child(id_Thing)
                        .removeValue();
                onBackPressed();
            }
        });

        refract_thing = findViewById(R.id.refract_thing);
        refract_thing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThingInfoActivity.this, ChangeThing.class);
                intent.putExtra("id_Office",id_Office);
                intent.putExtra("id_Floor",id_Floor);
                intent.putExtra("id_Room",id_Room);
                intent.putExtra("id_Thing",id_Thing);
                intent.putExtra("name_Thing",name_Thing);
                intent.putExtra("type_Thing",type_Thing);
                intent.putExtra("price_Thing",price_Thing);
                intent.putExtra("id_image_Thing",id_image);
                startActivity(intent);
            }
        });
    }
}
