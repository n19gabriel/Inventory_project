package com.example.gabriel.inventory_project.Inventory_pg.thing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChangeThing extends AppCompatActivity {
    static final int GALLERY_REQUEST = 1;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private EditText ETname;
    private EditText ETtype;
    private EditText ETprice;
    private TextView TVexpiration_date;
    private TextView tx_warranty;
    private View line;
    private StorageReference mStorageRef;
    private Button Badd;
    private Button BsetImage;
    private ImageButton Bplus;
    private ImageButton Bminus;
    private ImageView IVsetImage;
    private String id_Office;
    private String id_Floor;
    private String id_Room;
    private String id_Thing;
    private Bitmap bitmap;
    private Uri selectedImage;
    private LinearLayout linearLayout;
    private int counter;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_thing);

        Intent intent = getIntent();
        id_Office = intent.getStringExtra("id_Office");
        id_Floor = intent.getStringExtra("id_Floor");
        id_Room = intent.getStringExtra("id_Room");
        id_Thing = intent.getStringExtra("id_Thing");

        tx_warranty =findViewById(R.id.tx_warranty);
        linearLayout = findViewById(R.id.liner_layout_counter);
        line = findViewById(R.id.line);
        linearLayout.setVisibility(View.GONE);
        tx_warranty.setVisibility(View.GONE);
        line.setVisibility(View.GONE);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);

        ETname = findViewById(R.id.et_name_thing);
        ETtype = findViewById(R.id.et_type);
        ETprice = findViewById(R.id.et_price);
        TVexpiration_date = findViewById(R.id.view_counter);
        counter = 0;
        TVexpiration_date.setText(String.valueOf(counter));
        Badd = findViewById(R.id.add_thing);
        BsetImage = findViewById(R.id.bt_set_image);
        IVsetImage = findViewById(R.id.iv_set_image);
        Bplus = findViewById(R.id.plus);
        Bminus = findViewById(R.id.minus);
        Badd.setText("Save change");
        BsetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                flag = true;
            }
        });

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

        Badd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChange();
            }
        });
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

    private void uploadImage(String id){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        if(selectedImage != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = mStorageRef.child(id);
            ref.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ChangeThing.this, "Upload", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ChangeThing.this, "Failed", Toast.LENGTH_SHORT).show();
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
    private void addChange() {
        //getting the values to save
        String name = ETname.getText().toString().trim();
        String type = ETtype.getText().toString().trim();
        String price =ETprice.getText().toString().trim();
        String date_of_delete = TVexpiration_date.getText().toString();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {
            //Saving the Office change
            myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor)
                    .child("Rooms").child(id_Room).child("Things").child(id_Thing).
                    child("name").setValue(name);
            Toast.makeText(this, "Name change", Toast.LENGTH_LONG).show();
            ETname.setText("");
        }

        if (!TextUtils.isEmpty(type)) {
            //Saving the Office change
            myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor)
                    .child("Rooms").child(id_Room).child("Things").child(id_Thing).
                    child("type").setValue(type);
            Toast.makeText(this, "Type change", Toast.LENGTH_LONG).show();
            ETname.setText("");
        }

        if (!TextUtils.isEmpty(price)) {
            //Saving the Office change
            myRef.child("Offices").child(id_Office).child("Floors").child(id_Floor)
                    .child("Rooms").child(id_Room).child("Things").child(id_Thing).
                    child("price").setValue(price);
            Toast.makeText(this, "Price change", Toast.LENGTH_LONG).show();
            ETname.setText("");
        }
        if(flag){
            mStorageRef = FirebaseStorage.getInstance().getReference().child(id_Thing);
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
            uploadImage(id_Thing);
            flag=false;
        }

        if(TextUtils.isEmpty(name)&&TextUtils.isEmpty(type)&&TextUtils.isEmpty(date_of_delete)&&flag==false){
            Toast.makeText(this, "Please enter a change", Toast.LENGTH_LONG).show();
        }
        onBackPressed();
    }
}


