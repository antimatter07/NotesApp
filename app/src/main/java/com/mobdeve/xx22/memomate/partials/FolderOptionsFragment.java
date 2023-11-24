package com.mobdeve.xx22.memomate.partials;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

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
import com.mobdeve.xx22.memomate.databinding.ModalFolderOptionsBinding;
import com.mobdeve.xx22.memomate.folder.ViewFolderActivity;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FolderOptionsFragment extends DialogFragment {

    private ModalFolderOptionsBinding binding;
    private int folderColor = R.color.folderDefault;
    private int folderId = -1;
    private int folderPos;
    private String folderName;

    private ExecutorService executorService;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = ModalFolderOptionsBinding.inflate(inflater);

        executorService = Executors.newFixedThreadPool(2);

        binding.changeColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Bundle data = new Bundle();
                data.putInt(ViewFolderActivity.folderPosition, folderPos);
                data.putInt(FolderDatabaseHandler.FOLDER_ID, folderId);
                data.putInt(FolderDatabaseHandler.FOLDER_COLOR, folderColor);

                ChooseFolderColorFragment chooseFolderColorFragment = new ChooseFolderColorFragment();
                chooseFolderColorFragment.setArguments(data);
                chooseFolderColorFragment.show(getActivity().getSupportFragmentManager(), "FolderColorDialog");
            }
        });

        binding.deleteFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                getActivity().finish();

                // delete the folder and its notes
                if(folderId != -1) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            // Deletes the notes in the folder
                            NoteDatabase ndb = new NoteDatabase(getContext());
                            ndb.deleteNotesInFolder(folderId);
                            // Deletes the folder itself
                            FolderDatabase fdb = new FolderDatabase(getContext());

                            fdb.deleteFolder(folderId);

                        }
                    });
                    Toast.makeText(getContext(), "Deleted Folder: " + folderName,
                    Toast.LENGTH_SHORT).show();
                }
            }
        });

        View view = binding.getRoot();
        builder.setView(view);

        return builder.create();
    }

    public void setFolder(int pos, int id, String name, int color) {
        folderPos = pos;
        folderId = id;
        folderName = name;
        folderColor = color;
    }
}
