package com.mobdeve.xx22.memomate.note.note_checklist;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.databinding.ChecklistItemBinding;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckItemHolder extends RecyclerView.ViewHolder {


    ChecklistItemBinding binding;
    //private onItemLongClickListener onItemLongClickListener

    private NoteDatabase noteDatabase;
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    private ChecklistActivity activity;

    /**
     * Item id of checklist item
     */
    private int currentItemID;


    public CheckItemHolder(@NonNull ChecklistItemBinding binding, ArrayList<ChecklistItemModel> data, ChecklistAdapter adapter, NoteDatabase noteDatabase) {
        super(binding.getRoot());
        this.binding = binding;
        this.noteDatabase = noteDatabase;


        EditText editText = binding.editText;

        CheckBox checkBox = binding.checkBox;


        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        editText.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                activity.setActiveChecklistItem(binding.editText, currentItemID);
            }
        });


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

                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    noteDatabase.removeChecklistItem(currentItemID);

                                }
                            });


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

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    data.get(position).setChecked(isChecked);

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            noteDatabase.updateChecklistItemChecked(currentItemID, isChecked);
                        }
                    });
                }
            }
        });


    }



    public void bindData(ChecklistItemModel checklistItem, ChecklistActivity activity) {
        binding.checkBox.setChecked(checklistItem.getIsChecked());
        binding.editText.setText(checklistItem.getText());
        binding.editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, checklistItem.getItemSize());

        int fontColor = ContextCompat.getColor(activity.getApplicationContext(), checklistItem.getItemColor());
        binding.editText.setTextColor(fontColor);

        currentItemID = checklistItem.getItemId();
        this.activity = activity;
    }


}
