package com.mobdeve.xx22.villarica.matthew.notes;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.ChecklistItemBinding;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CheckItemHolder extends RecyclerView.ViewHolder {


    ChecklistItemBinding binding;
    //private onItemLongClickListener onItemLongClickListener;
    public CheckItemHolder(@NonNull ChecklistItemBinding binding, ArrayList<ChecklistItemModel> data, ChecklistAdapter adapter) {
        super(binding.getRoot());
        this.binding = binding;

        EditText editText = binding.editText;

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // Detect backspace key press
                    if (editText.getText().toString().isEmpty()) {
                        // Remove the corresponding item from the ArrayList
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            data.remove(position);
                            adapter.notifyItemRemoved(position);
                            return true; // Consume the key event
                        }
                    }
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Update the data when the text changes
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    data.get(position).setText(s.toString());
                }
            }
        });


    }



    public void bindData(ChecklistItemModel checklistItem) {
        binding.checkBox.setChecked(checklistItem.getIsChecked());
        binding.editText.setText(checklistItem.getText());
    }




}
