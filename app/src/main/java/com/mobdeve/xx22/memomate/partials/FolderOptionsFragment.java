package com.mobdeve.xx22.memomate.partials;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.database.FolderDatabaseHandler;
import com.mobdeve.xx22.memomate.database.NoteDatabaseHandler;
import com.mobdeve.xx22.memomate.databinding.ModalFolderOptionsBinding;
import com.mobdeve.xx22.memomate.folder.ViewFolderActivity;

public class FolderOptionsFragment extends DialogFragment {

    private ModalFolderOptionsBinding binding;
    private int folderColor = R.color.folderDefault;
    private int folderId;
    private int folderPos;

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
                Bundle data = new Bundle();
                data.putInt(ViewFolderActivity.folderPosition, folderPos);
                data.putInt(FolderDatabaseHandler.FOLDER_ID, folderId);
                data.putInt(FolderDatabaseHandler.FOLDER_COLOR, folderColor);

                ChooseFolderColorFragment chooseFolderColorFragment = new ChooseFolderColorFragment();
                chooseFolderColorFragment.setArguments(data);
                chooseFolderColorFragment.show(getActivity().getSupportFragmentManager(), "FolderColorDialog");
            }
        });

        View view = binding.getRoot();
        builder.setView(view);

        return builder.create();
    }

    public void setFolder(int pos, int id, int color) {
        folderPos = pos;
        folderId = id;
        folderColor = color;
    }
}
