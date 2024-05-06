package com.example.book;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> bookList;
    private String username;

    public BookAdapter(Context context, List<Book> bookList, String username) {
        this.context = context;
        this.bookList = bookList;
        this.username = username;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bind(book, position);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewBook;
        private TextView textViewBookName;
        private TextView textViewAuthorName;
        private Button buttonRead;
        private Button buttonFeedback;
        private Button buttonFavorite;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBook = itemView.findViewById(R.id.imageViewBook);
            textViewBookName = itemView.findViewById(R.id.textViewBookName);
            textViewAuthorName = itemView.findViewById(R.id.textViewAuthorName);
            buttonRead = itemView.findViewById(R.id.buttonRead);
            buttonFeedback = itemView.findViewById(R.id.buttonFeedback);
            buttonFavorite = itemView.findViewById(R.id.buttonFavorite);
        }

        public void bind(final Book book, final int position) {
            textViewBookName.setText(book.getBookName());
            textViewAuthorName.setText(book.getAuthorName());

            Glide.with(context)
                    .load("https://book8080.000webhostapp.com/uploads/" + book.getFilename())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.books)
                            .error(R.drawable.bookmarks)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(imageViewBook);

            if (book.isFavorite()) {
                buttonFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite, 0, 0, 0);
            } else {
                buttonFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_border, 0, 0, 0);
            }

            buttonRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BookDetailsActivity.class);
                    intent.putExtra("book_image_url", "https://book8080.000webhostapp.com/uploads/" + book.getFilename());
                    intent.putExtra("book_content", book.getContent());
                    intent.putExtra("book_id", book.getId());
                    intent.putExtra("book_name", book.getBookName());
                    context.startActivity(intent);
                }
            });

            buttonFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedback.class);
                    intent.putExtra("book_id", book.getId());
                    intent.putExtra("username", username); // Pass the username
                    intent.putExtra("book_name", book.getBookName()); // Pass the book name
                    intent.putExtra("position", position); // Pass the position
                    context.startActivity(intent);
                }
            });

            buttonFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateFavoriteStatus(book.getId(), !book.isFavorite());
                }
            });
        }

        private void updateFavoriteStatus(String bookId, boolean isFavorite) {
            Book book = bookList.get(getAdapterPosition());
            book.setFavorite(isFavorite);
            notifyItemChanged(getAdapterPosition());

            updateFavoriteStatusInServer(bookId, isFavorite);
        }

        private void updateFavoriteStatusInServer(String bookId, boolean isFavorite) {
            String url = "https://book8080.000webhostapp.com/fav.php?id=" + bookId + "&favorite_status=" + (isFavorite ? "1" : "0");

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error updating favorite status", Toast.LENGTH_SHORT).show();
                        }
                    });

            Volley.newRequestQueue(context).add(stringRequest);
        }
    }
}
