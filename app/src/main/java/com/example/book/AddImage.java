package com.example.book;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddImage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        imageView = findViewById(R.id.imageView);
        Button chooseImageButton = findViewById(R.id.chooseImageButton);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

                // Upload the image
                new ImageUploader().execute(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ImageUploader extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            if (bitmaps.length == 0 || bitmaps[0] == null)
                return null;

            Bitmap bitmap = bitmaps[0];
            String response = null;

            try {
                // Convert bitmap to byte array
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                byte[] imageBytes = outputStream.toByteArray();

                // Create URL and HttpURLConnection
                URL url = new URL("https://book8080.000webhostapp.com/AddImage.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // Write image data to the connection output stream
                OutputStream os = conn.getOutputStream();
                os.write(imageBytes);
                os.flush();
                os.close();

                // Check response code
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Image uploaded successfully
                    InputStream is = conn.getInputStream();
                    response = convertStreamToString(is);
                    is.close();
                } else {
                    // Error uploading image
                    response = "Error uploading image. Server returned HTTP code: " + responseCode;
                }

                // Disconnect connection
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error: " + e.getMessage();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(AddImage.this, result, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddImage.this, AddBook.class);
            startActivity(intent);
            finish();
        }
    }

    private String convertStreamToString(InputStream is) {
        // Convert InputStream to String
        try (java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }
}
