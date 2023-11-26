package com.mobdeve.xx22.memomate.note.note_checklist;

import android.content.Context;
import android.util.Log;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;
import com.mobdeve.xx22.memomate.databinding.ChecklistItemBinding;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<CheckItemHolder>{
    private ArrayList<ChecklistItemModel> data;
    private Context context;
    private int itemColor;

    private NoteDatabase noteDatabase;
    private ChecklistActivity activity;

    public ChecklistAdapter(Context context, ChecklistActivity activity, ArrayList<ChecklistItemModel> data, NoteDatabase db, int color) {
        this.data = data;
        this.context = context;
        this.noteDatabase = db;

        this.itemColor = color;

        this.activity = activity;
    }

    @Override
    public CheckItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChecklistItemBinding checklistItemBinding = ChecklistItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );




        return new CheckItemHolder(checklistItemBinding, data, this, noteDatabase);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckItemHolder holder, int position) {
        holder.bindData(data.get(position), activity);
        CheckBox checkBox = holder.binding.checkBox;
        checkBox.setButtonTintList(ColorStateList.valueOf(itemColor));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }






}
