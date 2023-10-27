package com.mobdeve.xx22.villarica.matthew.notes;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;
import com.mobdeve.xx22.villarica.matthew.notes.databinding.ActivityMainBinding;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<FolderModel> folders;
    private MainActivityAdapter mainAdapter;
    private ActivityMainBinding viewBinding;

//    TEMP data
    private boolean isOrderAscending = true;


    private ActivityResultLauncher<Intent> mainActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.viewBinding.getRoot());

        this.folders = FolderDataHelper.generateFolderData();

        setupRecyclerView();

        // Setup Toggle Order Button
        // TODO: Sort Order functionality
        ToggleButton orderBtn = this.viewBinding.orderBtn;

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOrderAscending = !isOrderAscending;
                if(isOrderAscending) {
                    orderBtn.setBackgroundResource(R.drawable.ic_ascend);
                }
                else {
                    orderBtn.setBackgroundResource(R.drawable.ic_descend);
                }
            }
        });

        // Setup Create New Folder Button
        viewBinding.newFolderBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            CreateFolderDialogFragment createFolderDialogFragment = new CreateFolderDialogFragment();
            createFolderDialogFragment.show(fm, "NewFolderDialog");
        });


        // Setup Search Button
        ImageButton searchBtn = this.viewBinding.searchBtn;
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
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
    }

    private void setupRecyclerView() {  // TODO: add ActivityResultLauncher
        mainAdapter = new MainActivityAdapter(this.folders);
        viewBinding.folderRv.setAdapter(mainAdapter);
        viewBinding.folderRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

}