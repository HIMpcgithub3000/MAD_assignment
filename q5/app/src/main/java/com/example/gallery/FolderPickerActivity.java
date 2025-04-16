package com.example.gallery;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FolderPickerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FolderAdapter adapter;
    private List<File> folders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_picker);

        recyclerView = findViewById(R.id.recyclerFolders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFolders();
    }

    private void loadFolders() {
        folders = new ArrayList<>();

        // Get Pictures directory
        File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (picturesDir.exists()) {
            folders.add(picturesDir);

            // Add subdirectories
            File[] subDirs = picturesDir.listFiles(File::isDirectory);
            if (subDirs != null) {
                Arrays.sort(subDirs, Comparator.comparing(File::getName));
                folders.addAll(Arrays.asList(subDirs));
            }
        }

        // Add app-specific picture directory
        File appPicturesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (appPicturesDir != null && appPicturesDir.exists()) {
            folders.add(appPicturesDir);
        }

        adapter = new FolderAdapter(folders);
        recyclerView.setAdapter(adapter);
    }

    private class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
        private List<File> folderList;

        public FolderAdapter(List<File> folderList) {
            this.folderList = folderList;
        }

        @NonNull
        @Override
        public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
            return new FolderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
            final File folder = folderList.get(position);
            holder.tvFolderName.setText(folder.getName());

            // Count images in the folder
            File[] imageFiles = folder.listFiles((dir, name) -> {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                        name.endsWith(".png") || name.endsWith(".gif");
            });

            int imageCount = imageFiles != null ? imageFiles.length : 0;
            holder.tvImageCount.setText(imageCount + " images");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FolderPickerActivity.this, GalleryActivity.class);
                    intent.putExtra("FOLDER_PATH", folder.getAbsolutePath());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return folderList.size();
        }

        class FolderViewHolder extends RecyclerView.ViewHolder {
            TextView tvFolderName;
            TextView tvImageCount;

            public FolderViewHolder(@NonNull View itemView) {
                super(itemView);
                tvFolderName = itemView.findViewById(R.id.tvFolderName);
                tvImageCount = itemView.findViewById(R.id.tvImageCount);
            }
        }
    }
}
