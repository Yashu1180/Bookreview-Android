package com.example.book;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Signup extends AppCompatActivity {

    private EditText usernameEditText, contactNumberEditText, emailEditText, passwordEditText, confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1C97D6")));
        // Initialize EditText fields
        usernameEditText = findViewById(R.id.editTextUsername);
        contactNumberEditText = findViewById(R.id.editTextContactNumber);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);

        Button signupButton = findViewById(R.id.btnSignup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String username = usernameEditText.getText().toString();
                String contactNumber = contactNumberEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                // Check if passwords match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Signup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Encode the data to be sent in URL format
                try {
                    username = URLEncoder.encode(username, "UTF-8");
                    contactNumber = URLEncoder.encode(contactNumber, "UTF-8");
                    email = URLEncoder.encode(email, "UTF-8");
                    password = URLEncoder.encode(password, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // Construct the URL with signup data as query parameters
                String url = "https://book8080.000webhostapp.com/s.php" +
                        "?username=" + username +
                        "&contactNumber=" + contactNumber +
                        "&email=" + email +
                        "&password=" + password;

                // Call AsyncTask to send data to server
                new SignupTask().execute(url);
            }
        });
    }

    private class SignupTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... urls) {
            String url = urls[0];

            try {
                // Create HttpURLConnection
                URL urlObj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

                // Set request method to GET
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();

                // Close the connection
                connection.disconnect();

                return responseCode;
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Toast.makeText(Signup.this, "Signup successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Signup.this, "Signup failed", Toast.LENGTH_SHORT).show();
                Log.e("SignupTask", "Failed with response code: " + responseCode);
            }
        }
    }
}
