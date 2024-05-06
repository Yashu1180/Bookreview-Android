package com.example.book;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class profile extends AppCompatActivity {

    private TextView textViewResult;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = getIntent().getStringExtra("username");
        Toast.makeText(this, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();

        textViewResult = findViewById(R.id.textViewResult);

        // Replace "your_server_url" with your actual server URL
        String url = "https://book8080.000webhostapp.com/p.php";

        // Execute AsyncTask to fetch data from the server
        new FetchDataTask().execute(url);
    }

    private class FetchDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = null;

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    result = stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    StringBuilder builder = new StringBuilder();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String fetchedUsername = jsonObject.getString("username");

                        if (fetchedUsername.equals(username)) {
//                            int id = jsonObject.getInt("id");
                            String contactNumber = jsonObject.getString("contactNumber");
                            String email = jsonObject.getString("email");

//                            builder.append("ID: ").append(id).append("\n");
                            builder.append("Username: ").append(fetchedUsername).append("\n\n");
                            builder.append("Contact Number: ").append(contactNumber).append("\n\n");
                            builder.append("Email: ").append(email).append("\n");
                        }

                    }

                    if (builder.length() > 0) {
                        textViewResult.setText(builder.toString());
                    } else {
                        textViewResult.setText("No user found with username: " + username);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(profile.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
