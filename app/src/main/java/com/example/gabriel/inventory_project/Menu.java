package com.example.gabriel.inventory_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.example.gabriel.inventory_project.Inventory_pg.office.OfficeActivity;
import com.google.firebase.auth.FirebaseAuth;


public class Menu extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private CardView list_cards;
    private CardView search_card;
    private CardView scanner_card;
    private CardView log_out_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        //defining cards
        list_cards = findViewById(R.id.list_card);
        log_out_card = findViewById(R.id.log_out_card);
        search_card = findViewById(R.id.search_card);
       scanner_card = findViewById(R.id.scanner_card);

        list_cards.setOnClickListener(this);
        search_card.setOnClickListener(this);
        log_out_card.setOnClickListener(this);
        scanner_card.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.list_card : intent = new Intent(Menu.this, OfficeActivity.class);
                startActivity(intent);
                break;
            case R.id.search_card : intent = new Intent(Menu.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.scanner_card : intent = new Intent(Menu.this, ScannerActivity.class);
                startActivity(intent);
                break;
            case R.id.log_out_card : mAuth.signOut();
                Toast.makeText(Menu.this, "Signing Out...", Toast.LENGTH_SHORT).show();
                intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
