package com.example.gabriel.inventory_project.Inventory_pg.thing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.inventory_project.History.AddHistory;
import com.example.gabriel.inventory_project.History.Record;
import com.example.gabriel.inventory_project.QRCode;
import com.example.gabriel.inventory_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class NewThing extends AppCompatActivity {
    static final int GALLERY_REQUEST = 1;
    private Thing thing;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private StorageReference mStorageRef;
    private EditText ETname;
    private EditText ETtype;
    private EditText ETprice;
    private TextView TVexpiration_date;
    private String userId;
    private Button Badd;
    private Button BsetImage;
    private ImageButton Bplus;
    private ImageButton Bminus;
    private String id_Office;
    private String name_Office;
    private String id_Floor;
    private String name_Floor;
    private String id_Room;
    private String name_Room;
    private ImageView IVsetImage;
    private Bitmap bitmap;
    private Uri selectedImage;
    private int counter;
    private AddHistory history;
    private String code;
    private QRCode qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_thing);

        history = new AddHistory();

        Intent intent = getIntent();
        id_Office = intent.getStringExtra("id_Office");
        name_Office = intent.getStringExtra("name_Office");
        id_Floor = intent.getStringExtra("id_Floor");
        name_Floor = intent.getStringExtra("name_Floor");
        id_Room = intent.getStringExtra("id_Room");
        name_Room = intent.getStringExtra("name_Room");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        ETname = findViewById(R.id.et_name_thing);
        ETtype = findViewById(R.id.et_type);
        ETprice = findViewById(R.id.et_price);
        TVexpiration_date = findViewById(R.id.view_counter);
        counter = 0;
        TVexpiration_date.setText(String.valueOf(counter));
        Badd = findViewById(R.id.add_thing);
        Bplus = findViewById(R.id.plus);
        Bminus = findViewById(R.id.minus);
        BsetImage = findViewById(R.id.bt_set_image);
        IVsetImage = findViewById(R.id.iv_set_image);

        Bplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TVexpiration_date.getText().toString().equals("100")){
                    TVexpiration_date.setText(String.valueOf(++counter));
                }
            }
        });

        Bminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TVexpiration_date.getText().toString().equals("0")){
                    TVexpiration_date.setText(String.valueOf(--counter));
                }
            }
        });

        BsetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });


        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addThings();
            }
        });
    }

    private void uploadImage(String id_image){
        if(selectedImage != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = mStorageRef.child(id_image);
            ref.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(NewThing.this, "Upload", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(NewThing.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progres = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progres+"%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        bitmap = null;
        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    IVsetImage.setImageBitmap(bitmap);
                }
        }
    }

    private void addThings() {
        //getting the values to save
        String name = ETname.getText().toString().trim();
        String type = ETtype.getText().toString().trim();
        String price =ETprice.getText().toString().trim();
        String date_of_delete = TVexpiration_date.getText().toString();
        int expiration_date = Integer.parseInt(date_of_delete);

        //checking if the value is provided
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(type)&& !TextUtils.isEmpty(price)&&!(expiration_date==0)) {

            //getting a unique id using push().getKey() method
            String id = myRef.push().getKey();
            String id_image = myRef.push().getKey();

            uploadImage(id_image);
            //creating an Thing Object
            thing = new Thing(id, name, type, price ,expiration_date);
            if (selectedImage!=null){
                thing.setId_image(id_image);
            }
            //Saving the Office
            myRef.child(userId).child("Offices").child(id_Office).child("Floors").
                    child(id_Floor).child("Rooms").child(id_Room).child("Things").child(id).setValue(thing);

            code = id_Office+"/"+name_Office+"/"+id_Floor+"/"+name_Floor+"/"+
                    id_Room+"/"+name_Room+"/"+thing.getId()+"/"+thing.getName()+"/"+
                    thing.getType()+"/"+thing.getPrice()+"/"+thing.getDate_of_add()+"/"+
                    thing.getDate_of_delete()+"/"+thing.getId_image();
            qrCode = new QRCode(thing.getId(),code);
            myRef.child(userId).child("QRCodes").child(qrCode.getId()).setValue(qrCode);
            //setting edittext to blank again
            ETname.setText("");
            ETtype.setText("");
            ETprice.setText("");
            counter=0;
            TVexpiration_date.setText(String.valueOf(counter));

            //displaying a success toast
            Toast.makeText(this, "Thing added", Toast.LENGTH_LONG).show();
            history.addrecord(new Record("Add thing "+thing.getName()));
            history.addrecordThing(new Record("Add thing "+thing.getName()),
                    id_Office,id_Floor,id_Room,id);
        }else if(TextUtils.isEmpty(name)){
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(type)){
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a type", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(price)){
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a price", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Please enter a date of delete", Toast.LENGTH_LONG).show();
        }
    }
}
