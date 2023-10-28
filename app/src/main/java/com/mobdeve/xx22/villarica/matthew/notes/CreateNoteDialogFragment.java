package com.mobdeve.xx22.villarica.matthew.notes;

import java.time.LocalDate;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.ModalNewNoteBinding;


public class CreateNoteDialogFragment extends DialogFragment {

    private ModalNewNoteBinding binding;

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
                intent.putExtra(ChecklistActivity.TITLE_KEY, "New Checklist");
                intent.putExtra(ChecklistActivity.ITEMLIST_KEY, new ArrayList<ChecklistItemModel>());
                startActivity(intent);
                dismiss();
            }
        });


        builder.setView(view);

        return builder.create();
    }
}
