package com.example.book;

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

public class edit extends AppCompatActivity {

    private EditText usernameEditText, contactNumberEditText, emailEditText;
    private String username; // Added to store the username received from settings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize EditText fields
        usernameEditText = findViewById(R.id.editTextUsername);
        contactNumberEditText = findViewById(R.id.editTextContactNumber);
        emailEditText = findViewById(R.id.editTextEmail);

        // Receive username from settings activity
        username = getIntent().getStringExtra("username");

        // Set the username in the EditText field
        usernameEditText.setText(username);

        Button editButton = findViewById(R.id.btnEdit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String contactNumber = contactNumberEditText.getText().toString();
                String email = emailEditText.getText().toString();

                // Encode the data to be sent in URL format
                try {
                    username = URLEncoder.encode(username, "UTF-8");
                    contactNumber = URLEncoder.encode(contactNumber, "UTF-8");
                    email = URLEncoder.encode(email, "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // Construct the URL with signup data as query parameters
                String url = "https://book8080.000webhostapp.com/update.php" +
                        "?username=" + username +
                        "&contactNumber=" + contactNumber +
                        "&email=" + email;

                // Call AsyncTask to send data to server
                new UpdateTask().execute(url);
            }
        });
    }

    private class UpdateTask extends AsyncTask<String, Void, Integer> {

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
                Toast.makeText(edit.this, "Update successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(edit.this, "Update failed", Toast.LENGTH_SHORT).show();
                Log.e("UpdateTask", "Failed with response code: " + responseCode);
            }
        }
    }
}
