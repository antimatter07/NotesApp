package com.mobdeve.xx22.memomate.partials;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import com.mobdeve.xx22.memomate.databinding.ModalSortNotesByBinding;

public class SortingOptionsDialogFragment extends DialogFragment {

    private ModalSortNotesByBinding binding; // Declare a variable to hold the ViewBinding instance

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = ModalSortNotesByBinding.inflate(inflater);

        View view = binding.getRoot();
        builder.setView(view)
                .setPositiveButton("Select", (dialog, which) -> {
                    // Handle sorting option
                    int checkedId = binding.sortingOptionsRg.getCheckedRadioButtonId();
                    sendSortingOption(checkedId);

                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Remove modal
                });

        return builder.create();
    }

    private void sendSortingOption(int checkedId) {
        // Create an Intent to send the sorting option back to the MainActivity
        Bundle result = new Bundle();
        result.putInt("sortingOption", checkedId);
        getParentFragmentManager().setFragmentResult("sortingKey", result);
    }
}
