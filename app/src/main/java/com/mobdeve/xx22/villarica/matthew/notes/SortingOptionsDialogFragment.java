package com.mobdeve.xx22.villarica.matthew.notes;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import com.mobdeve.xx22.villarica.matthew.notes.databinding.SortingModalLayoutBinding;

public class SortingOptionsDialogFragment extends DialogFragment {

    private SortingModalLayoutBinding binding; // Declare a variable to hold the ViewBinding instance

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = SortingModalLayoutBinding.inflate(inflater);

        View view = binding.getRoot();
        builder.setView(view)
                .setPositiveButton("Select", (dialog, which) -> {
                    // Handle sorting option
                    int checkedId = binding.sortingOptionsRg.getCheckedRadioButtonId();

                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Remove modal
                });

        return builder.create();
    }
}
