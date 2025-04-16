package com.example.gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;
    private Button btnCapture;
    private File storageDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnCapture = findViewById(R.id.btnCapture);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFolderAndTakePhoto();
            }
        });
    }

    private void chooseFolderAndTakePhoto() {
        // Create a dialog for folder selection or creation
        final String[] options = {"Default Photos Folder", "Create New Folder", "Choose Existing Folder"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Photo Storage Location");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Default Photos Folder
                        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        dispatchTakePictureIntent();
                        break;
                    case 1: // Create New Folder
                        showNewFolderDialog();
                        break;
                    case 2: // Choose Existing Folder
                        // In a real app, you would open a folder picker here
                        // For simplicity, we'll use a predefined folder
                        storageDir = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "PhotoGallery");
                        if (!storageDir.exists()) {
                            storageDir.mkdirs();
                        }
                        dispatchTakePictureIntent();
                        break;
                }
            }
        });
        builder.show();
    }

    private void showNewFolderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Folder Name");

        final android.widget.EditText input = new android.widget.EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String folderName = input.getText().toString();
                if (!folderName.isEmpty()) {
                    storageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), folderName);
                    if (!storageDir.exists()) {
                        storageDir.mkdirs();
                    }
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(CameraActivity.this, "Folder name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.photogallery.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        if (storageDir == null) {
            // Use default directory if none selected
            storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Photo saved to: " + currentPhotoPath, Toast.LENGTH_LONG).show();

            // Add the photo to the MediaStore for easy access
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            // View the gallery with the new image
            viewGallery();
        }
    }

    private void viewGallery() {
        // Open gallery view with the current folder
        Intent intent = new Intent(CameraActivity.this, GalleryActivity.class);
        intent.putExtra("FOLDER_PATH", storageDir.getAbsolutePath());
        startActivity(intent);
        finish();
    }
}
