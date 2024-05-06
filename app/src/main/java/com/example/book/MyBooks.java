package com.example.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyBooks extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username"); // Make it final

        recyclerView = findViewById(R.id.recyclerViewBooks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookList, username); // Pass username to the adapter
        recyclerView.setAdapter(bookAdapter);

        fetchDataFromServer(username); // Pass username to fetchDataFromServer method

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bookmarks) {
                    // Already on MyBooks activity, no need to start it again
                    return true;
                } else if (item.getItemId() == R.id.menu) {
                    Intent menuIntent = new Intent(MyBooks.this, menu.class);
                    menuIntent.putExtra("username", username);
                    startActivity(menuIntent);
                    return true;
                }
                return false;
            }
        });
    }

    private void fetchDataFromServer(final String username) {
        String url = "https://book8080.000webhostapp.com/fetchFavBook.php"; // Replace with your server URL
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject bookObject = response.getJSONObject(i);
                                Book book = new Book();
                                book.setId(bookObject.getString("id"));
                                book.setBookName(bookObject.getString("book_name"));
                                book.setAuthorName(bookObject.getString("author_name"));
                                book.setLanguage(bookObject.getString("language"));
                                book.setCountry(bookObject.getString("country"));
                                book.setGenre(bookObject.getString("genre"));
                                book.setPublisher(bookObject.getString("publisher"));
                                book.setContent(bookObject.getString("content"));
                                book.setFilename(bookObject.getString("filename"));
                                bookList.add(book);
                            }
                            bookAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
}
