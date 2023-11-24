package com.mobdeve.xx22.memomate.folder;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.databinding.ModalChangeFolderItemBinding;
import com.mobdeve.xx22.memomate.model.FolderModel;

import java.util.ArrayList;
import java.util.List;

public class ChooseFolderAdapter extends RecyclerView.Adapter<ChooseFolderHolder> {

    private ArrayList<FolderModel> folderData;
    private int selectedFolderPos = RecyclerView.NO_POSITION;
    private int previousSelectedFolder = RecyclerView.NO_POSITION;

    public ChooseFolderAdapter(ArrayList<FolderModel> data) {
        this.folderData = data;
    }
    @NonNull
    @Override
    public ChooseFolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ModalChangeFolderItemBinding modalChangeFolderItemBinding = ModalChangeFolderItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new ChooseFolderHolder(modalChangeFolderItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseFolderHolder holder, int position) {

        if (position == selectedFolderPos) {
            holder.setBackgroundColor(true);
        }
        else if (position == previousSelectedFolder) {
            holder.setBackgroundColor(false);
        }
        else {
            // Bind data
            FolderModel folder = folderData.get(position);
            holder.bindFolderData(folder);

            // Set up click listener
            holder.itemView.setOnClickListener(view -> {
                setSelectedFolder(position);
            });
        }

    }

    @Override
    public int getItemCount() {
        return folderData.size();
    }

    // update the recycler view in the fragment that the selected
    // folder has changed
    private void setSelectedFolder(int newSelected) {
        previousSelectedFolder = selectedFolderPos;
        selectedFolderPos = newSelected;

        notifyItemChanged(previousSelectedFolder);
        notifyItemChanged(selectedFolderPos);
    }

    public FolderModel getSelectedFolder() {
        if (selectedFolderPos != RecyclerView.NO_POSITION)
            return folderData.get(selectedFolderPos);
        else return null;
    }
}
