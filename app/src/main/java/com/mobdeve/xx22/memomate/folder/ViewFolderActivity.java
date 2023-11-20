package com.mobdeve.xx22.memomate.folder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.mobdeve.xx22.memomate.partials.CreateNoteDialogFragment;
import com.mobdeve.xx22.memomate.partials.FolderOptionsFragment;
import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.search.SearchActivity;
import com.mobdeve.xx22.memomate.partials.SortingOptionsDialogFragment;
import com.mobdeve.xx22.memomate.databinding.FolderActivityBinding;

public class ViewFolderActivity extends AppCompatActivity {

    public static final String folderNameKey = "FOLDER_NAME_KEY",
                            folderColorKey = "FOLDER_COLOR_KEY";


    //name to display
    private String folderName;
    private int folderColor;

    private FolderActivityBinding viewBinding;
    private boolean isOrderAscending = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.viewBinding = FolderActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        //set folder to name in intent
        Intent intent = getIntent();
        folderName = intent.getStringExtra(folderNameKey);
        viewBinding.folderNameTv.setText(folderName);
        folderColor = ContextCompat.getColor(viewBinding.menuBarLl.getContext(), intent.getIntExtra(folderColorKey, R.color.folderDefault));
        viewBinding.menuBarLl.setBackgroundColor(folderColor);
        // viewBinding.newNoteBtn.setBackgroundTintList(ColorStateList.valueOf(folderColor));


        //TODO: set up notes recycler view to show notes in folder

        // Setup Search Button
        viewBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewFolderActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        // Setup Sorting Options Button
        viewBinding.sortBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            SortingOptionsDialogFragment sortingOptionsFragment = new SortingOptionsDialogFragment();
            sortingOptionsFragment.show(fm, "SettingsDialog");


        });

        // Setup Create New Note Button
        viewBinding.newNoteBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            CreateNoteDialogFragment createNoteDialogFragment = new CreateNoteDialogFragment();
            createNoteDialogFragment.show(fm, "NewNoteDialog");
        });

        viewBinding.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOrderAscending = !isOrderAscending;
                if(isOrderAscending) {
                    viewBinding.orderBtn.setBackgroundResource(R.drawable.ic_ascend);
                }
                else {
                    viewBinding.orderBtn.setBackgroundResource(R.drawable.ic_descend);
                }
            }
        });

        // Setup Folder Options Button
        viewBinding.folderOptionsBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            FolderOptionsFragment folderOptionsFragment = new FolderOptionsFragment();
            folderOptionsFragment.show(fm, "FolderOptionsDialog");
        });


    }
}
