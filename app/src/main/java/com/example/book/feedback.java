package com.example.book;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class feedback extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText reviewEditText;
    private Button submitButton;
    private String bookName;
    private String username;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ratingBar = findViewById(R.id.ratingBar);
        reviewEditText = findViewById(R.id.reviewEditText);
        submitButton = findViewById(R.id.submitButton);

        // Get book name, username, and position from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bookName = extras.getString("book_name");
            username = extras.getString("username");
            position = extras.getInt("position");
        }

        // Display a toast message with the book name and username
        Toast.makeText(this, "Feedback for book: " + bookName + "\nUser: " + username, Toast.LENGTH_SHORT).show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                String review = reviewEditText.getText().toString();

                // Display toast message with values being sent to server
                Toast.makeText(feedback.this, "Sending data to server...\n" +
                        "Username: " + username + "\n" +
                        "Book Name: " + bookName + "\n" +
                        "Rating: " + rating + "\n" +
                        "Review: " + review, Toast.LENGTH_LONG).show();

                // Call AsyncTask to send data to server
                new SubmitReviewTask().execute(rating, review);
            }
        });
    }

    private class SubmitReviewTask extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {
            float rating = (Float) params[0];
            String review = (String) params[1];

            try {
                // Encode review to handle special characters
                String encodedReview = URLEncoder.encode(review, "UTF-8");

                // Construct the URL with parameters
                String urlString = "https://book8080.000webhostapp.com/reviews.php?" +
                        "username=" + URLEncoder.encode(username, "UTF-8") +
                        "&book_name=" + URLEncoder.encode(bookName, "UTF-8") +
                        "&rating=" + rating +
                        "&review=" + encodedReview;

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(feedback.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(feedback.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
