package com.mobdeve.xx22.villarica.matthew.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.FolderActivityBinding;

public class ViewFolderActivity extends AppCompatActivity {

    public static final String FOLDER_NAME_KEY = "FOLDER_NAME_KEY";


    //name to display
    private String folderName;

    private FolderActivityBinding viewBinding;
    private boolean isOrderAscending = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.viewBinding = FolderActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        //set folder to name in intent
        Intent intent = getIntent();
        folderName = intent.getStringExtra(FOLDER_NAME_KEY);
        viewBinding.folderNameTv.setText(folderName);


        //TODO: set up notes recycler view to show notes in folder

        // Setup Search Button
        ImageButton searchBtn = this.viewBinding.searchBtn;
        searchBtn.setOnClickListener(new View.OnClickListener() {
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


    }
}
