package com.example.gabriel.inventory_project.Inventory_pg.thing.Info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.gabriel.inventory_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ThingInfoActivity extends AppCompatActivity implements InfoFragment.OnFragmentInteractionListener,
HistoryFragment.OnFragmentInteractionListener{
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

    private ArrayList<String> dates;
    private FirebaseAuth mAuth;
    private ListView listView;
    private DatabaseReference myRef;
    private PagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_info);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference(userId);
        dates = new ArrayList<String>();


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


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
         adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),
                id_Office,name_Office,id_Floor,name_Floor,id_Room,name_Room,id_Thing,name_Thing,type_Thing,
                price_Thing,date_of_add_Thing,date_of_delete_Thing,id_image);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}