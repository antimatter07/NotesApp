package com.mobdeve.xx22.memomate;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.memomate.databinding.FolderItemBinding;
import com.mobdeve.xx22.memomate.folder.ViewFolderActivity;
import com.mobdeve.xx22.memomate.model.FolderModel;

import java.util.ArrayList;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityViewHolder> {

    private ArrayList<FolderModel> folderData;
    private ActivityResultLauncher<Intent> viewFolderLauncher;

//    TODO: add ActivityResultLauncher<Intent> viewFolderLauncher
    public MainActivityAdapter(ArrayList<FolderModel> data, ActivityResultLauncher<Intent> viewFolderLauncher) {
        this.folderData = data;
        this.viewFolderLauncher = viewFolderLauncher;
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

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ViewFolderActivity.class);
            intent.putExtra(ViewFolderActivity.folderIdKey, folderData.get(holder.getAdapterPosition()).getFolderId());
            intent.putExtra(ViewFolderActivity.folderNameKey, folderData.get(holder.getAdapterPosition()).getName());
            intent.putExtra(ViewFolderActivity.folderColorKey, folderData.get(holder.getAdapterPosition()).getColorResId());

            //TODO: change to launcher in final
             viewFolderLauncher.launch(intent);
        });

    }

    @Override
    public int getItemCount() {
        return folderData.size();
    }

    /*  Handles adding a folder item to the stored array list + updates the UI accordingly.
     *  Note: When a folder item is added, it is added at the end of the RecyclerView.
     */
    public void addFolderItem(FolderModel folder) {
        this.folderData.add(folder);
        notifyItemInserted(this.folderData.size() - 1);
    }
}
