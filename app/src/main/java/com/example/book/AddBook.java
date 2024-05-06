package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class AddBook extends AppCompatActivity {

    EditText bookNameEditText, authorNameEditText, languageEditText, countryEditText, genreEditText, publisherEditText, contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Initialize EditText fields
        bookNameEditText = findViewById(R.id.editTextBookName);
        authorNameEditText = findViewById(R.id.editTextAuthorName);
        languageEditText = findViewById(R.id.editTextLanguage);
        countryEditText = findViewById(R.id.editTextCountry);
        genreEditText = findViewById(R.id.editTextGenre);
        publisherEditText = findViewById(R.id.editTextPublisher);
        contentEditText = findViewById(R.id.editTextContent);

        // Example of how to send data when a button is clicked
        Button submitButton = findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
            }
        });
    }

    private void sendDataToServer() {
        // Get the values from EditText fields
        String bookName = bookNameEditText.getText().toString();
        String authorName = authorNameEditText.getText().toString();
        String language = languageEditText.getText().toString();
        String country = countryEditText.getText().toString();
        String genre = genreEditText.getText().toString();
        String publisher = publisherEditText.getText().toString();
        String content = contentEditText.getText().toString();

        // Send user data to the server
        new SendUserDataToServer(bookName, authorName, language, country, genre, publisher, content).execute();
    }

    private class SendUserDataToServer extends AsyncTask<Void, Void, Void> {
        private String bookName, authorName, language, country, genre, publisher, content;
        private String errorMessage;

        public SendUserDataToServer(String bookName, String authorName, String language, String country, String genre, String publisher, String content) {
            this.bookName = bookName;
            this.authorName = authorName;
            this.language = language;
            this.country = country;
            this.genre = genre;
            this.publisher = publisher;
            this.content = content;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Encode fields for URL
                String encodedBookName = URLEncoder.encode(bookName, "UTF-8");
                String encodedAuthorName = URLEncoder.encode(authorName, "UTF-8");
                String encodedLanguage = URLEncoder.encode(language, "UTF-8");
                String encodedCountry = URLEncoder.encode(country, "UTF-8");
                String encodedGenre = URLEncoder.encode(genre, "UTF-8");
                String encodedPublisher = URLEncoder.encode(publisher, "UTF-8");
                String encodedContent = URLEncoder.encode(content, "UTF-8");

                // Create the URL with parameters
                String urlString = "https://book8080.000webhostapp.com/AddBook.php" +
                        "?book_name=" + encodedBookName +
                        "&author_name=" + encodedAuthorName +
                        "&language=" + encodedLanguage +
                        "&country=" + encodedCountry +
                        "&genre=" + encodedGenre +
                        "&publisher=" + encodedPublisher +
                        "&content=" + encodedContent;
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Set up the connection properties
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                // Check the server response code
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Successful response
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddBook.this, "Data sent successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddBook.this, AdminHome.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    // Handle other response codes
                    errorMessage = "Server returned non-OK response: " + responseCode;
                    Log.e("SendUserDataToServer", errorMessage);
                }

                urlConnection.disconnect();
            } catch (Exception e) {
                errorMessage = "Error sending user data to server";
                Log.e("SendUserDataToServer", errorMessage, e);
            }

            return null;
        }
    }
}
