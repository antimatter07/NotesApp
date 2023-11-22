package com.mobdeve.xx22.memomate.note.note_checklist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;
import com.mobdeve.xx22.memomate.databinding.ChecklistItemBinding;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<CheckItemHolder>{
    private ArrayList<ChecklistItemModel> data;
    private Context context;
    private NoteDatabase noteDatabase;

    public ChecklistAdapter(Context context, ArrayList<ChecklistItemModel> data, NoteDatabase db) {
        this.data = data;
        this.context = context;
        this.noteDatabase = db;


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
        holder.bindData(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }






}
