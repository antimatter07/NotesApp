package com.mobdeve.xx22.memomate.partials;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import com.mobdeve.xx22.memomate.MainActivity;
import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.databinding.ModalNoteOptionsBinding;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NoteOptionsFragment extends DialogFragment {

    private ModalNoteOptionsBinding binding;
    /**
     * noteID to delete, lock, etc.
     */
    private int currentNoteID = -1;
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

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
                ChangeFolderFragment changeFolderFragment = new ChangeFolderFragment();
                changeFolderFragment.show(getActivity().getSupportFragmentManager(), "LockNoteDialog");
            }
        });

        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentNoteID != -1) {

                    final WeakReference<Activity> activityRef = new WeakReference<>(requireActivity());

                    executorService.execute(new Runnable() {


                        @Override
                        public void run() {
                            Activity activity = activityRef.get();

                            if(isAdded() && activity != null) {
                                NoteDatabase db = new NoteDatabase(activity);
                                db.deleteNote(currentNoteID);

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(isAdded()) {
                                            Intent intent = new Intent(activity, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                });


                           }

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
