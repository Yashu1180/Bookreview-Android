package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLogin extends AppCompatActivity {
    EditText editTextId, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1C97D6")));
        editTextId = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    public void login(View view) {
        String adminId = editTextId.getText().toString();
        String adminPassword = editTextPassword.getText().toString();

        // Check if the entered ID and password match the expected values
        if (adminId.equals("123") && adminPassword.equals("123")) {
            // Credentials are correct, perform your action (e.g., navigate to another activity)
            Intent intent = new Intent(this, AdminHome.class);
            startActivity(intent);
        } else {
            // Incorrect credentials, show a toast message or perform other actions
            Toast.makeText(this, "Invalid ID or password", Toast.LENGTH_SHORT).show();
        }
    }
}