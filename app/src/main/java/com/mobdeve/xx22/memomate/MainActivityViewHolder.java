package com.mobdeve.xx22.memomate;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.mobdeve.xx22.memomate.databinding.FolderItemBinding;
import com.mobdeve.xx22.memomate.model.FolderModel;

public class MainActivityViewHolder extends RecyclerView.ViewHolder {

    private FolderItemBinding folderBinding;

    public MainActivityViewHolder(@NonNull FolderItemBinding folderView) {
        super(folderView.getRoot());
        this.folderBinding = folderView;
    }


    public void bindFolderData(FolderModel folderData) {
        folderBinding.folderNameTv.setText(folderData.getName());
        folderBinding.folderNoteCountTv.setText(String.valueOf(folderData.getNoteCount()));
        int iconColor = ContextCompat.getColor(folderBinding.folderIv.getContext(), folderData.getColorResId());
        folderBinding.folderIv.setColorFilter(iconColor);
    }
}
