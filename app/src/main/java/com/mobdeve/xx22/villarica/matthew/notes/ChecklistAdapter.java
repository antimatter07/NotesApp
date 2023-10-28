package com.mobdeve.xx22.villarica.matthew.notes;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.ChecklistItemBinding;

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
