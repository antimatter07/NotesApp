package com.mobdeve.xx22.villarica.matthew.notes;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.ModalNewFolderBinding;
import com.mobdeve.xx22.villarica.matthew.notes.databinding.ModalNewNoteBinding;

import java.util.ArrayList;


public class CreateFolderDialogFragment extends DialogFragment {

    private ModalNewFolderBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = ModalNewFolderBinding.inflate(inflater);

        View view = binding.getRoot();

        ArrayList<ImageButton> colorBtns = new ArrayList<>();
        colorBtns.add(binding.colorDefaultBtn);
        colorBtns.add(binding.colorRedBtn);
        colorBtns.add(binding.colorOrangeBtn);
        colorBtns.add(binding.colorYellowBtn);
        colorBtns.add(binding.colorGreenBtn);
        colorBtns.add(binding.colorCyanBtn);
        colorBtns.add(binding.colorBlueBtn);
        colorBtns.add(binding.colorPurpleBtn);

        // If a color is selected, remove the checks from the other colors
        // and set a check on this color
        for (int i = 0; i < 8; i++) {
            ImageButton btn = colorBtns.get(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (ImageButton colorBtn : colorBtns) {
                        // Remove the src drawable from all buttons
                        colorBtn.setImageResource(0);
                    }
                   btn.setImageResource(R.drawable.ic_check);
                }
            });
        }

        builder.setView(view)
                .setPositiveButton("Create", (dialog, which) -> {
                    // TODO: Switch to ViewFolder Activity

                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Remove modal
                });

        return builder.create();
    }
}
