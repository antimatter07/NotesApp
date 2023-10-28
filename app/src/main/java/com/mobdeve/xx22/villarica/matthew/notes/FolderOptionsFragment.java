package com.mobdeve.xx22.villarica.matthew.notes;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.ModalFolderOptionsBinding;

public class FolderOptionsFragment extends DialogFragment {

    private ModalFolderOptionsBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = ModalFolderOptionsBinding.inflate(inflater);

        binding.changeColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ChooseFolderColorFragment chooseFolderColorFragment = new ChooseFolderColorFragment();
                chooseFolderColorFragment.show(getActivity().getSupportFragmentManager(), "FolderColorDialog");
            }
        });

        View view = binding.getRoot();
        builder.setView(view);

        return builder.create();
    }
}
