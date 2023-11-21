package com.mobdeve.xx22.memomate;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.mobdeve.xx22.memomate.database.NoteDataHelper;
import com.mobdeve.xx22.memomate.database.FolderDataHelper;
import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.databinding.ActivityMainBinding;
import com.mobdeve.xx22.memomate.model.FolderModel;
import com.mobdeve.xx22.memomate.model.ParentNoteModel;
import com.mobdeve.xx22.memomate.note.NoteAdapter;
import com.mobdeve.xx22.memomate.partials.CreateFolderDialogFragment;
import com.mobdeve.xx22.memomate.partials.CreateNoteDialogFragment;
import com.mobdeve.xx22.memomate.partials.SortingOptionsDialogFragment;
import com.mobdeve.xx22.memomate.search.SearchActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<FolderModel> folders;
    private MainActivityAdapter mainAdapter;
    private ActivityMainBinding viewBinding;

    private ArrayList<ParentNoteModel> data = new ArrayList<>();

//    TEMP data
    private boolean isOrderAscending = true;


    private ActivityResultLauncher<Intent> mainActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    //TODO: handle changes to data in folder

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


        //data.addAll(NoteDataHelper.loadCheckListNote());

        //data.addAll(NoteDataHelper.loadTextNote());

        NoteDatabase noteDatabase = new NoteDatabase(getApplicationContext());
        data = noteDatabase.getAllNotes();


        FragmentManager fragmentManager = getSupportFragmentManager();

        NoteAdapter noteAdapter = new NoteAdapter(this, data, fragmentManager);
        GridView gridView = viewBinding.notesGv;
        gridView.setAdapter(noteAdapter);


    }

    private void setupRecyclerView() {  // TODO: add ActivityResultLauncher
        mainAdapter = new MainActivityAdapter(this.folders, mainActivityResultLauncher);
        viewBinding.folderRv.setAdapter(mainAdapter);
        viewBinding.folderRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private String getNoteType(ParentNoteModel note) {
        String fullString = note.getClass().getCanonicalName();
        return fullString.substring(fullString.lastIndexOf(".") + 1);
    }

}