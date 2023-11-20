package com.mobdeve.xx22.memomate.note.note_checklist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.memomate.model.ChecklistItemModel;
import com.mobdeve.xx22.memomate.databinding.ChecklistItemBinding;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<CheckItemHolder>{
    private ArrayList<ChecklistItemModel> data;

    public ChecklistAdapter(ArrayList<ChecklistItemModel> data) {
        this.data = data;
    }

    @Override
    public CheckItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChecklistItemBinding checklistItemBinding = ChecklistItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );




        return new CheckItemHolder(checklistItemBinding, data, this);
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
