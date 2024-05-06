package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View; // Correct import statement
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class settings extends AppCompatActivity {
    private ImageView imageViewAccount, imageViewPassword, imageViewLogout;
    private TextView textViewAccount, textViewPassword, textViewLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Receive username from intent
        String username = getIntent().getStringExtra("username");

        // Display a toast message with the username
        Toast.makeText(this, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();

        imageViewAccount = findViewById(R.id.imageViewAccount);
        imageViewPassword = findViewById(R.id.imageViewPassword);
        imageViewLogout = findViewById(R.id.imageViewLogout);

        textViewAccount = findViewById(R.id.textViewAccount);
        textViewPassword = findViewById(R.id.textViewPassword);
        textViewLogout = findViewById(R.id.textViewLogout);

        // Set click listeners
        textViewAccount.setOnClickListener(new android.view.View.OnClickListener() { // Changed to android.view.View.OnClickListener
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this, edit.class);
                intent.putExtra("username", username);
                startActivity(intent);            }
        });

        textViewPassword.setOnClickListener(new android.view.View.OnClickListener() { // Changed to android.view.View.OnClickListener
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this, ChangePassword.class);
                intent.putExtra("username", username);
                startActivity(intent);            }
        });
        textViewLogout.setOnClickListener(new android.view.View.OnClickListener() { // Changed to android.view.View.OnClickListener
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this, MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);            }
        });
    }
}
