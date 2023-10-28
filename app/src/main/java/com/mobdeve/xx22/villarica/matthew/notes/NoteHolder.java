package com.mobdeve.xx22.villarica.matthew.notes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.NoteItemBinding;

public class NoteHolder extends RecyclerView.ViewHolder{

    NoteItemBinding binding;

    public NoteHolder(@NonNull NoteItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindData(ParentNoteModel noteData) {
        binding.dateModTv.setText(noteData.getDateModified());
        binding.noteTitleTv.setText(noteData.getTitle());

    }
}
