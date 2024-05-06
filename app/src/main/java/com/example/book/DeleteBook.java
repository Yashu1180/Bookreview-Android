package com.example.book;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.List;

public class DeleteBook extends AppCompatActivity {

    private LinearLayout cardContainer;
    private Button deleteButton;

    private List<String> selectedBooks;
    private List<View> cardViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_book);

        cardContainer = findViewById(R.id.cardContainer);
        deleteButton = findViewById(R.id.deleteButton);

        selectedBooks = new ArrayList<>();
        cardViews = new ArrayList<>();

        // Execute AsyncTask to fetch data from the URL
        new FetchDataTask().execute();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call delete method with selected book names
                new DeleteTask().execute(selectedBooks.toArray(new String[0]));
            }
        });
    }

    private class FetchDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonData = null;

            try {
                URL url = new URL("https://book8080.000webhostapp.com/fb.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonData = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);

            if (jsonData != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String bookName = jsonObject.getString("book_name");
                        String authorName = jsonObject.getString("author_name");
                        addCardView(bookName, authorName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addCardView(String bookName, String authorName) {
        View cardView = getLayoutInflater().inflate(R.layout.card_layout, null);
        CheckBox checkBox = cardView.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedBooks.add(bookName);
            } else {
                selectedBooks.remove(bookName);
            }
        });

        TextView bookTextView = cardView.findViewById(R.id.bookTextView);
        TextView authorTextView = cardView.findViewById(R.id.authorTextView);

        bookTextView.setText(bookName);
        authorTextView.setText(authorName);

        cardContainer.addView(cardView);
        cardViews.add(cardView);
    }

    private void removeCardViews(List<View> viewsToRemove) {
        for (View view : viewsToRemove) {
            cardContainer.removeView(view);
        }
        cardViews.removeAll(viewsToRemove);
    }

    private class DeleteTask extends AsyncTask<String, Void, Void> {

        private List<View> viewsToRemove = new ArrayList<>();

        @Override
        protected Void doInBackground(String... bookNames) {
            // Make the delete request here using HttpURLConnection or a networking library
            for (String bookName : bookNames) {
                // Concatenate the book name to the delete URL
                String deleteUrl = "https://book8080.000webhostapp.com/delete.php?bookName=" + bookName;

                // Perform delete request
                try {
                    URL url = new URL(deleteUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();

                    // Handle response code if needed
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Successful deletion
                        viewsToRemove.addAll(getCardViewsByBookName(bookName));
                    } else {
                        // Failed deletion
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            removeCardViews(viewsToRemove);
            selectedBooks.clear();
            Toast.makeText(DeleteBook.this, "Books deleted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private List<View> getCardViewsByBookName(String bookName) {
        List<View> views = new ArrayList<>();
        for (View view : cardViews) {
            TextView textView = view.findViewById(R.id.bookTextView);
            if (textView.getText().toString().equals(bookName)) {
                views.add(view);
            }
        }
        return views;
    }
}
