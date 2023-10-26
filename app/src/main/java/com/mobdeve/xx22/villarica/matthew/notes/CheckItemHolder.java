package com.mobdeve.xx22.villarica.matthew.notes;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.ChecklistItemBinding;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CheckItemHolder extends RecyclerView.ViewHolder {


    ChecklistItemBinding binding;
    //private onItemLongClickListener onItemLongClickListener;
    public CheckItemHolder(@NonNull ChecklistItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;




    }

    public void bindData(ChecklistItemModel checklistItem) {
        binding.checkBox.setChecked(checklistItem.getIsChecked());
        binding.editText.setText(checklistItem.getText());
    }




}
