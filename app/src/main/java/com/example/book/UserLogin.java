package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class UserLogin extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1C97D6")));

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

    }


    public void onLoginButtonClick(View view) {
        String id = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        // Fetch credentials from the server and compare them with user-entered credentials
        new FetchCredentialsTask().execute(id, password);
    }

    private class FetchCredentialsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String password = params[1];
            String urlString = "https://book8080.000webhostapp.com/fetchUserCredentials.php";

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String response = stringBuilder.toString();

                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String serverId = jsonArray.getJSONObject(i).getString("username");
                    String serverPassword = jsonArray.getJSONObject(i).getString("password");
                    if (id.equals(serverId) && password.equals(serverPassword)) {
                        return id; // return the ID if credentials are correct
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null; // return null if credentials are incorrect
        }

        @Override
        protected void onPostExecute(String editTextUsername) {
            if (editTextUsername != null) { // changed from managerId to editTextUsername
                Intent intent = new Intent(UserLogin.this, UserHome.class);
                intent.putExtra("username", editTextUsername);
                startActivity(intent);
                finish(); // Finish the current activity to prevent user from going back
            } else {
                // Credentials are incorrect, show a toast message
                Toast.makeText(UserLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void link(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
}
