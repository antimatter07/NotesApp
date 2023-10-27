package com.mobdeve.xx22.villarica.matthew.notes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.FolderItemBinding;

import java.util.ArrayList;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityViewHolder> {

    private ArrayList<FolderModel> folderData;
    private ActivityResultLauncher<Intent> viewFolderLauncher;

//    TODO: add ActivityResultLauncher<Intent> viewFolderLauncher
    public MainActivityAdapter(ArrayList<FolderModel> data) {
        this.folderData = data;
//        this.viewFolderLauncher = viewFolderLauncher;
    }
    @NonNull
    @Override
    public MainActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FolderItemBinding folderItemBinding = FolderItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new MainActivityViewHolder(folderItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityViewHolder holder, int position) {
        holder.bindFolderData(folderData.get(position));

    }

    @Override
    public int getItemCount() {
        return folderData.size();
    }
}
