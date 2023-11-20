package com.mobdeve.xx22.memomate.note;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.memomate.databinding.NoteItemBinding;
import com.mobdeve.xx22.memomate.model.ParentNoteModel;

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
