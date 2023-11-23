package com.mobdeve.xx22.memomate.partials;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mobdeve.xx22.memomate.database.FolderDatabase;
import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.database.NoteDatabaseHandler;
import com.mobdeve.xx22.memomate.databinding.ModalChangeFolderBinding;
import com.mobdeve.xx22.memomate.databinding.ModalLockNoteBinding;
import com.mobdeve.xx22.memomate.folder.ChooseFolderAdapter;
import com.mobdeve.xx22.memomate.model.FolderModel;
import com.mobdeve.xx22.memomate.note.note_text.TextNoteActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;

public class ChangeFolderFragment extends DialogFragment {

    private ModalChangeFolderBinding binding;
    private ChooseFolderAdapter adapter;

    private int currentNoteId;
    private int currentFolderId;

    private ExecutorService executorService;
    private Handler handler;

    public interface UpdateActivityGridView {
        void updateGridView();
    }

    private UpdateActivityGridView gridViewListener = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = ModalChangeFolderBinding.inflate(inflater);

        // Setup folders recyclerview
        FolderDatabase folderDatabase = new FolderDatabase(this.getContext());
        ArrayList<FolderModel> folders = folderDatabase.getAllFolders();
        adapter = new ChooseFolderAdapter(folders);
        binding.foldersRl.setAdapter(adapter);
        binding.foldersRl.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        binding.foldersRl.setItemAnimator(null);

        View view = binding.getRoot();

        Bundle args = getArguments();
        currentNoteId = args.getInt(NoteDatabaseHandler.COLUMN_NOTE_ID);
        currentFolderId = args.getInt(NoteDatabaseHandler.COLUMN_FOLDER_KEY);

        executorService = Executors.newFixedThreadPool(2);
        handler = new Handler();

        AlertDialog alertDialog = builder.setView(view)
                                .setPositiveButton("Move", null)
                                .setNegativeButton("Cancel", (dialog, which) -> {
                                    // Remove modal
                                }).create();

        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> onMoveClick(alertDialog));
        });

        return alertDialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // change the interface used based on the current activity
            if (!(context instanceof TextNoteActivity))
                gridViewListener = (UpdateActivityGridView) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement appropriate update interface");
        }
    }

    private void onMoveClick(Dialog dialog) {
        FolderModel selectedFolder = adapter.getSelectedFolder();

        if (selectedFolder != null) {
            dialog.dismiss();

            if (selectedFolder.getFolderId() == currentFolderId) {
                Toast.makeText(getContext(), "Note is already in " + selectedFolder.getName(),
                        Toast.LENGTH_SHORT).show();
            }
            else {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        NoteDatabase db = new NoteDatabase(getContext());
                        db.updateNoteFolder(currentNoteId, selectedFolder.getFolderId());

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (gridViewListener != null) {
                                    gridViewListener.updateGridView();
                                }
                            }
                        });
                    }
                });

                Toast.makeText(getContext(), "Moved note to " + selectedFolder.getName(),
                        Toast.LENGTH_SHORT).show();
            }

        }
        else {
            // no dismiss() prevents the fragment from closing
            Toast.makeText(getContext(), "Select a Folder", Toast.LENGTH_SHORT).show();
        }

    }
}

