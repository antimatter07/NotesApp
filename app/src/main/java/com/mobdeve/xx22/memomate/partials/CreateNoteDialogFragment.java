package com.mobdeve.xx22.memomate.partials;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mobdeve.xx22.memomate.note.note_checklist.ChecklistActivity;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;
import com.mobdeve.xx22.memomate.databinding.ModalNewNoteBinding;
import com.mobdeve.xx22.memomate.note.note_text.TextNoteActivity;


public class CreateNoteDialogFragment extends DialogFragment {

    private ModalNewNoteBinding binding;
    private int folderId = -1;

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = ModalNewNoteBinding.inflate(inflater);

        View view = binding.getRoot();

        binding.newTextNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to TextNoteActivity
                Intent intent = new Intent(getActivity(), TextNoteActivity.class);
                intent.putExtra("folderKey", folderId);
                intent.putExtra("titleContent", "New Text Note");
                intent.putExtra("noteContent", "");
                startActivity(intent);
                dismiss();
            }
        });

        binding.newChecklistNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to CheckListActivity
                Intent intent = new Intent(getActivity(), ChecklistActivity.class);
                intent.putExtra(ChecklistActivity.FOLDER_KEY, folderId);
                intent.putExtra(ChecklistActivity.TITLE_KEY, "New Checklist");
                intent.putExtra(ChecklistActivity.ITEMLIST_KEY, new ArrayList<ChecklistItemModel>());

                Log.d("new create note dialog", "checklist w folderkey: " + folderId);

                startActivity(intent);
                dismiss();
            }
        });


        builder.setView(view);

        return builder.create();
    }
}
