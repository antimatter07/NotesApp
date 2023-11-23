package com.mobdeve.xx22.memomate.note.note_checklist;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.databinding.ChecklistItemBinding;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckItemHolder extends RecyclerView.ViewHolder {


    ChecklistItemBinding binding;
    //private onItemLongClickListener onItemLongClickListener

    private NoteDatabase noteDatabase;
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    private int currentItemID;
    public CheckItemHolder(@NonNull ChecklistItemBinding binding, ArrayList<ChecklistItemModel> data, ChecklistAdapter adapter, NoteDatabase noteDatabase) {
        super(binding.getRoot());
        this.binding = binding;
        this.noteDatabase = noteDatabase;



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

                    String updatedText = s.toString();

                    ChecklistItemModel itemModel = data.get(position);



                    //int item_id = itemModel.getItemId();
                    Log.d("IN AFTER TEXT CHANGED", "checklist item new value: " + s.toString());

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            noteDatabase.updateChecklistItemText(currentItemID, updatedText);

                        }
                    });

                }
            }
        });


    }



    public void bindData(ChecklistItemModel checklistItem) {
        binding.checkBox.setChecked(checklistItem.getIsChecked());
        binding.editText.setText(checklistItem.getText());
        Log.d("binding of checklist item", "item id value: " + String.valueOf(checklistItem.getItemId()));
        currentItemID = checklistItem.getItemId();
    }




}
