package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View; // Correct import statement
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class menu extends AppCompatActivity {
    private ImageView imageViewAccount, imageViewSettings, imageViewLogout;
    private TextView textViewAccount, textViewSettings, textViewLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Receive username from intent
        String username = getIntent().getStringExtra("username");

        // Display a toast message with the username
        Toast.makeText(this, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();

        imageViewAccount = findViewById(R.id.imageViewAccount);
        imageViewSettings = findViewById(R.id.imageViewSettings);
        imageViewLogout = findViewById(R.id.imageViewLogout);

        textViewAccount = findViewById(R.id.textViewAccount);
        textViewSettings = findViewById(R.id.textViewSettings);
        textViewLogout = findViewById(R.id.textViewLogout);

        // Set click listeners
        textViewAccount.setOnClickListener(new android.view.View.OnClickListener() { // Changed to android.view.View.OnClickListener
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, profile.class);
                intent.putExtra("username", username);
                startActivity(intent);            }
        });


        textViewSettings.setOnClickListener(new android.view.View.OnClickListener() { // Changed to android.view.View.OnClickListener
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, settings.class);
                intent.putExtra("username", username);
                startActivity(intent);            }
        });
        textViewSettings.setOnClickListener(new android.view.View.OnClickListener() { // Changed to android.view.View.OnClickListener
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this,   settings.class);
                intent.putExtra("username", username);
                startActivity(intent);            }
        });


        textViewLogout.setOnClickListener(new android.view.View.OnClickListener() { // Changed to android.view.View.OnClickListener
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this,   MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);            }
        });
    }
}
