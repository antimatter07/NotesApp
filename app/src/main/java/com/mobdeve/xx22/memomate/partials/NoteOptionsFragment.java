package com.mobdeve.xx22.memomate.partials;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.database.NoteDatabaseHandler;
import com.mobdeve.xx22.memomate.databinding.ModalNoteOptionsBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NoteOptionsFragment extends DialogFragment {

    private ModalNoteOptionsBinding binding;
    /**
     * noteID to delete, lock, etc.
     */
    private int currentNoteID = -1;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = ModalNoteOptionsBinding.inflate(inflater);

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
                Bundle data = new Bundle();
                data.putInt(NoteDatabaseHandler.COLUMN_NOTE_ID, currentNoteID);

                ChangeFolderFragment changeFolderFragment = new ChangeFolderFragment();
                changeFolderFragment.setArguments(data);
                changeFolderFragment.show(getActivity().getSupportFragmentManager(), "ChangeFolderDialog");
            }
        });

        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentNoteID != -1) {

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            NoteDatabase db = new NoteDatabase(requireContext());
                            db.deleteNote(currentNoteID);


                        }
                    });
                    dismiss();

                }

            }
        });

        View view = binding.getRoot();
        builder.setView(view);

        return builder.create();
    }

    /**
     * Set noteID so that ID to perform operation on is visible within fragment
     * @param noteID
     */
    public void setNoteID(int noteID) {
        this.currentNoteID = noteID;
    }
}
