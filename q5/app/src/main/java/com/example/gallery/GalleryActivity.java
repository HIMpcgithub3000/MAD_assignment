package com.example.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private List<File> imageFiles;
    private String folderPath;
    private TextView tvFolderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        tvFolderName = findViewById(R.id.tvFolderName);
        recyclerView = findViewById(R.id.recyclerImages);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns grid

        folderPath = getIntent().getStringExtra("FOLDER_PATH");
        if (folderPath != null) {
            File folder = new File(folderPath);
            tvFolderName.setText(folder.getName());
            loadImagesFromFolder(folder);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload images when returning from image detail page (after possible deletion)
        if (folderPath != null) {
            loadImagesFromFolder(new File(folderPath));
        }
    }

    private void loadImagesFromFolder(File folder) {
        imageFiles = new ArrayList<>();

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                        name.endsWith(".png") || name.endsWith(".gif");
            });

            if (files != null) {
                // Sort by date (newest first)
                Arrays.sort(files, Comparator.comparing(File::lastModified).reversed());
                imageFiles.addAll(Arrays.asList(files));
            }
        }

        adapter = new GalleryAdapter(imageFiles);
        recyclerView.setAdapter(adapter);
    }

    private class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageViewHolder> {
        private List<File> fileList;

        public GalleryAdapter(List<File> fileList) {
            this.fileList = fileList;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            final File imageFile = fileList.get(position);

            // Load image with Glide
            Glide.with(GalleryActivity.this)
                    .load(imageFile)
                    .centerCrop()
                    .into(holder.imageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GalleryActivity.this, ImageDetailActivity.class);
                    intent.putExtra("IMAGE_PATH", imageFile.getAbsolutePath());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return fileList.size();
        }

        class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}
