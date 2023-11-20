package com.mobdeve.xx22.memomate.partials;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mobdeve.xx22.memomate.databinding.ModalChangeFolderBinding;
import com.mobdeve.xx22.memomate.databinding.ModalLockNoteBinding;

public class ChangeFolderFragment extends DialogFragment {

    private ModalChangeFolderBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = ModalChangeFolderBinding.inflate(inflater);

        View view = binding.getRoot();
        builder.setView(view)
                .setPositiveButton("Move", (dialog, which) -> {

                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Remove modal
                });

        return builder.create();
    }
}
