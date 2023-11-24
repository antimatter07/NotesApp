package com.mobdeve.xx22.memomate.partials;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mobdeve.xx22.memomate.MainActivity;
import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.database.FolderDatabase;
import com.mobdeve.xx22.memomate.database.FolderDatabaseHandler;
import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.database.NoteDatabaseHandler;
import com.mobdeve.xx22.memomate.databinding.ModalChooseFolderColorBinding;
import com.mobdeve.xx22.memomate.databinding.ModalNewFolderBinding;
import com.mobdeve.xx22.memomate.folder.FolderAdapter;
import com.mobdeve.xx22.memomate.folder.ViewFolderActivity;
import com.mobdeve.xx22.memomate.model.FolderModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;


public class ChooseFolderColorFragment extends DialogFragment {

    private ModalChooseFolderColorBinding binding;

    // Temporarily holds values for the new folder
    private int folderPos;
    private int folderId;
    private int folderColor = R.color.folderDefault;

    ExecutorService executorService;
    Handler handler;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = ModalChooseFolderColorBinding.inflate(inflater);

        View view = binding.getRoot();

        executorService = Executors.newFixedThreadPool(2);
        handler = new Handler();

        Map<ImageButton, Integer> colorBtns = new HashMap<>();
        colorBtns.put(binding.colorDefaultBtn, R.color.folderDefault);
        colorBtns.put(binding.colorRedBtn, R.color.folderRed);
        colorBtns.put(binding.colorOrangeBtn, R.color.folderOrange);
        colorBtns.put(binding.colorYellowBtn, R.color.folderYellow);
        colorBtns.put(binding.colorGreenBtn, R.color.folderGreen);
        colorBtns.put(binding.colorCyanBtn, R.color.folderCyan);
        colorBtns.put(binding.colorBlueBtn, R.color.folderBlue);
        colorBtns.put(binding.colorPurpleBtn, R.color.folderPurple);

        // select the current folder's color by default
        Bundle args = getArguments();
        folderPos = args.getInt(ViewFolderActivity.folderPosition, folderPos);
        folderId = args.getInt(FolderDatabaseHandler.FOLDER_ID);
        folderColor = args.getInt(FolderDatabaseHandler.FOLDER_COLOR);
        selectCurrentColor(colorBtns);

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
                .setPositiveButton("Select", (dialog, which) -> {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            FolderDatabase db = new FolderDatabase(getContext());
                            db.updateFolderColor(folderId, folderColor);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // update the ui of ViewFolderActivity
                                    ViewFolderActivity activity = (ViewFolderActivity) getActivity();
                                    activity.updateHeaderColor(folderColor);
                                    activity.updateGridView();

                                }
                            });

                        }
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Remove modal
                });

        return builder.create();
    }

    private void selectCurrentColor(Map<ImageButton, Integer> colorBtns) {
        for (Map.Entry<ImageButton, Integer> colorBtn : colorBtns.entrySet()) {
            if (colorBtn.getValue() == folderColor) {
                colorBtn.getKey().setImageResource(R.drawable.ic_check);
                break;
            }
        }
    }
}
