package com.mobdeve.xx22.villarica.matthew.notes;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.mobdeve.xx22.villarica.matthew.notes.databinding.FolderItemBinding;

public class MainActivityViewHolder extends RecyclerView.ViewHolder {

    private FolderItemBinding folderBinding;

    public MainActivityViewHolder(@NonNull FolderItemBinding folderView) {
        super(folderView.getRoot());
        this.folderBinding = folderView;
    }


    public void bindFolderData(FolderModel folderData) {
        folderBinding.folderNameTv.setText(folderData.getName());
        folderBinding.folderNoteCountTv.setText("0"); // TODO: set correct data
        // TODO: redo this once all folder colors are established
        if(folderData.getColorResId() == R.color.folderDefault) {
            int iconColor = ContextCompat.getColor(folderBinding.folderIv.getContext(), folderData.getColorResId());
            folderBinding.folderIv.setColorFilter(iconColor);
        }
        else
            folderBinding.folderIv.setColorFilter(folderData.getColorResId());
    }
}
