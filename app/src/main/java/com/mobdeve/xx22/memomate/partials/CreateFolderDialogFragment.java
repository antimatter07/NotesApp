package com.mobdeve.xx22.memomate.partials;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mobdeve.xx22.memomate.MainActivityAdapter;
import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.database.FolderDataHelper;
import com.mobdeve.xx22.memomate.database.FolderDatabase;
import com.mobdeve.xx22.memomate.database.FolderDatabaseHandler;
import com.mobdeve.xx22.memomate.databinding.ModalNewFolderBinding;
import com.mobdeve.xx22.memomate.folder.ViewFolderActivity;
import com.mobdeve.xx22.memomate.model.FolderModel;

import java.util.HashMap;
import java.util.Map;


public class CreateFolderDialogFragment extends DialogFragment {

    private ModalNewFolderBinding binding;
    private FolderDatabase folderDatabase;
    private MainActivityAdapter mainActivityAdapter;

    // Temporarily holds values for the new folder
    private String folderName = "New Folder";
    int folderColor = R.color.folderDefault;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = ModalNewFolderBinding.inflate(inflater);

        folderDatabase = new FolderDatabase(this.getContext());

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
                    // add folder into the folder DB
                    FolderModel folder = new FolderModel(folderName, folderColor);
                    int folderId = folderDatabase.addFolder(folder);
                    folder.setFolderId(folderId);

                    // update main activity UI w/ the new folder
                    mainActivityAdapter.addFolderItem(folder);

                    // switch to ViewFolderActivity
                    Intent intent = new Intent(getActivity(), ViewFolderActivity.class);
                    intent.putExtra(ViewFolderActivity.folderNameKey, folderName);
                    intent.putExtra(ViewFolderActivity.folderColorKey, folderColor);
                    startActivity(intent);
                    dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Remove partial
                });

        return builder.create();
    }

    public void setAdapter(MainActivityAdapter adapter) {
        this.mainActivityAdapter = adapter;
    }
}
