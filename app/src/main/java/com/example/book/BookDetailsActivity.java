package com.example.book;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BookDetailsActivity extends AppCompatActivity {

    private TextView textViewContent;
    private String bookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        ImageView imageViewBook = findViewById(R.id.imageViewBook);
        textViewContent = findViewById(R.id.textViewContent);

        // Get book image URL and content from intent
        String imageUrl = getIntent().getStringExtra("book_image_url");
        String content = getIntent().getStringExtra("book_content");
        bookName = getIntent().getStringExtra("book_name");

        // Load book image into ImageView using Glide
        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.books) // Placeholder image while loading
                        .error(R.drawable.books) // Error image if loading fails
                        .diskCacheStrategy(DiskCacheStrategy.ALL)) // Cache the image to improve performance
                .into(imageViewBook);

        // Set book content to TextView
        textViewContent.setText(content);

        // Call AsyncTask to fetch average rating and number of reviews for the book
        new FetchBookDetailsTask().execute();
    }

    private class FetchBookDetailsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Construct the URL with parameters
                String urlString = "https://book8080.000webhostapp.com/avg.php?book_name=" + bookName;
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // Read the response from the server
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                // Close the streams
                bufferedReader.close();
                inputStream.close();
                conn.disconnect();

                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    // Parse the JSON response
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("average_rating") && jsonResponse.has("num_reviews")) {
                        String averageRating = jsonResponse.getString("average_rating");
                        int numReviews = jsonResponse.getInt("num_reviews");
                        // Display the average rating and number of reviews above the content
                        textViewContent.setText("Average Rating: " + averageRating + "\nNumber of Reviews: " + numReviews + "\n\n" + textViewContent.getText());
                    } else if (jsonResponse.has("error")) {
                        // Display error message if book not found
                        String errorMessage = jsonResponse.getString("error");
                        Toast.makeText(BookDetailsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Display error message if response is null
                Toast.makeText(BookDetailsActivity.this, "Failed to fetch book details", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
