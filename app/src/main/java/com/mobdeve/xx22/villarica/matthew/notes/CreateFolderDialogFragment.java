package com.mobdeve.xx22.villarica.matthew.notes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.ModalNewFolderBinding;
import com.mobdeve.xx22.villarica.matthew.notes.databinding.ModalNewNoteBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CreateFolderDialogFragment extends DialogFragment {

    private ModalNewFolderBinding binding;

    // Temporarily holds values for the new folder
    private String folderName = "New Folder";
    int folderColor = R.color.folderDefault;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = ModalNewFolderBinding.inflate(inflater);

        View view = binding.getRoot();

        Map<ImageButton, Integer> colorBtns = new HashMap<>();
        colorBtns.put(binding.colorDefaultBtn, R.color.folderDefault);
        colorBtns.put(binding.colorRedBtn, R.color.folderRed);
        colorBtns.put(binding.colorOrangeBtn, R.color.folderOrange);
        colorBtns.put(binding.colorYellowBtn, R.color.folderYellow);
        colorBtns.put(binding.colorGreenBtn, R.color.folderGreen);
        colorBtns.put(binding.colorCyanBtn, R.color.folderCyan);
        colorBtns.put(binding.colorBlueBtn, R.color.folderBlue);
        colorBtns.put(binding.colorPurpleBtn, R.color.folderPurple);

        // If a color is selected, remove the checks from the other colors
        // and set a check on this color
        for (Map.Entry<ImageButton, Integer> colorBtn : colorBtns.entrySet()) {
            ImageButton btn = colorBtn.getKey();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    folderColor = colorBtn.getValue();

                    for (Map.Entry<ImageButton, Integer> colorBtn : colorBtns.entrySet()) {
                        // Remove the src drawable from all buttons
                        colorBtn.getKey().setImageResource(0);
                    }
                   btn.setImageResource(R.drawable.ic_check);
                }
            });
        }

        builder.setView(view)
                .setPositiveButton("Create", (dialog, which) -> {
                    String name = String.valueOf(this.binding.folderNameEt.getText());
                    if (name.length() != 0){
                        folderName = name;
                    }
                    // add folder to data model (this renders the new folder in main activity)
                    FolderDataHelper.addFolder(folderName, folderColor);


                    // switch to ViewFolderActivity
                    Intent intent = new Intent(getActivity(), ViewFolderActivity.class);
                    intent.putExtra(ViewFolderActivity.folderNameKey, folderName);
                    intent.putExtra(ViewFolderActivity.folderColorKey, folderColor);
                    startActivity(intent);

                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Remove modal
                });

        return builder.create();
    }
}
