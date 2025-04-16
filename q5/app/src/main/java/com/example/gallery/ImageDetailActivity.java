package com.example.gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageDetailActivity extends AppCompatActivity {
    private ImageView ivFullImage;
    private TextView tvImageName;
    private TextView tvImagePath;
    private TextView tvImageSize;
    private TextView tvImageDate;
    private Button btnDelete;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ivFullImage = findViewById(R.id.ivFullImage);
        tvImageName = findViewById(R.id.tvImageName);
        tvImagePath = findViewById(R.id.tvImagePath);
        tvImageSize = findViewById(R.id.tvImageSize);
        tvImageDate = findViewById(R.id.tvImageDate);
        btnDelete = findViewById(R.id.btnDelete);

        imagePath = getIntent().getStringExtra("IMAGE_PATH");
        if (imagePath != null) {
            displayImageDetails(imagePath);
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAndDeleteImage();
            }
        });
    }

    private void displayImageDetails(String path) {
        File imageFile = new File(path);
        if (imageFile.exists()) {
            // Display image
            Glide.with(this)
                    .load(imageFile)
                    .into(ivFullImage);

            // Display image details
            tvImageName.setText("Name: " + imageFile.getName());
            tvImagePath.setText("Path: " + imageFile.getAbsolutePath());

            // Calculate size in KB
            long fileSizeInBytes = imageFile.length();
            long fileSizeInKB = fileSizeInBytes / 1024;
            tvImageSize.setText("Size: " + fileSizeInKB + " KB");

            // Format date
            long lastModified = imageFile.lastModified();
            String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
                    .format(new Date(lastModified));
            tvImageDate.setText("Date: " + formattedDate);
        }
    }

    private void confirmAndDeleteImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Image");
        builder.setMessage("Are you sure you want to delete this image?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteImage();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteImage() {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            if (imageFile.delete()) {
                // Notify the media scanner that the file has been deleted
                MediaScannerConnection.scanFile(this,
                        new String[]{imagePath}, null, null);

                Toast.makeText(this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
                finish(); // Go back to gallery
            } else {
                Toast.makeText(this, "Failed to delete image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}