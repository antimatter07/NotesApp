package com.mobdeve.xx22.memomate.partials;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import com.mobdeve.xx22.memomate.databinding.NoteOptionsModalBinding;


public class NoteOptionsFragment extends DialogFragment {

    private NoteOptionsModalBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = NoteOptionsModalBinding.inflate(inflater);

        binding.lockNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                LockNoteFragment lockNoteFragment = new LockNoteFragment();
                lockNoteFragment.show(getActivity().getSupportFragmentManager(), "LockNoteDialog");
            }
        });

        binding.changeFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ChangeFolderFragment changeFolderFragment = new ChangeFolderFragment();
                changeFolderFragment.show(getActivity().getSupportFragmentManager(), "LockNoteDialog");
            }
        });

        View view = binding.getRoot();
        builder.setView(view);

        return builder.create();
    }
}
