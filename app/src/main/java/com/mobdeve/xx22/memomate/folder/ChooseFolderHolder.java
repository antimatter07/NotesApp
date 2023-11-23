package com.mobdeve.xx22.memomate.folder;

import android.graphics.Color;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.databinding.ModalChangeFolderItemBinding;
import com.mobdeve.xx22.memomate.model.FolderModel;

public class ChooseFolderHolder extends RecyclerView.ViewHolder {

    private ModalChangeFolderItemBinding folderItemBinding;

    public ChooseFolderHolder(@NonNull ModalChangeFolderItemBinding folderView) {
        super(folderView.getRoot());
        this.folderItemBinding = folderView;

    }

    public void bindFolderData(FolderModel folderData) {
        folderItemBinding.folderNameTv.setText(folderData.getName());

        // set the "default folder" folder icon to the logo
        // otherwise, only change to folder color
        if (folderData.getFolderId() == -1) {
            folderItemBinding.folderNameTv.setTypeface(folderItemBinding.folderNameTv.getTypeface(), Typeface.BOLD);
            folderItemBinding.folderIv.setImageResource(R.drawable.ic_logo);
            folderItemBinding.folderIv.setColorFilter(Color.TRANSPARENT);
        }
        else {
            int iconColor = ContextCompat.getColor(folderItemBinding.folderIv.getContext(), folderData.getColorResId());
            folderItemBinding.folderIv.setColorFilter(iconColor);
        }
    }

    public void setBackgroundColor(boolean isSelected) {
        if (isSelected) {
            int bgColor = ContextCompat.getColor(folderItemBinding.folderIv.getContext(), R.color.cyanAccent);
            folderItemBinding.modalFolderItemCl.setBackgroundColor(bgColor);
        }
        else folderItemBinding.modalFolderItemCl.setBackgroundColor(Color.TRANSPARENT);
    }

}
