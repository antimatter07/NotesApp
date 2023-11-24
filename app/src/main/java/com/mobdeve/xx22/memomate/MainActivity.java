package com.mobdeve.xx22.memomate;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.mobdeve.xx22.memomate.database.FolderDatabase;
import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.database.NoteDatabaseHandler;
import com.mobdeve.xx22.memomate.databinding.ActivityMainBinding;

import com.mobdeve.xx22.memomate.folder.FolderAdapter;

import com.mobdeve.xx22.memomate.folder.ViewFolderActivity;
import com.mobdeve.xx22.memomate.model.CheckListNoteModel;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;
import com.mobdeve.xx22.memomate.model.FolderModel;
import com.mobdeve.xx22.memomate.model.ParentNoteModel;
import com.mobdeve.xx22.memomate.note.NoteAdapter;
import com.mobdeve.xx22.memomate.partials.ChangeFolderFragment;
import com.mobdeve.xx22.memomate.partials.CreateFolderDialogFragment;
import com.mobdeve.xx22.memomate.partials.CreateNoteDialogFragment;
import com.mobdeve.xx22.memomate.partials.SortingOptionsDialogFragment;
import com.mobdeve.xx22.memomate.search.SearchActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
            implements ChangeFolderFragment.UpdateActivityGridView {

    private FolderAdapter folderAdapter;
    private NoteAdapter noteAdapter;
    private ActivityMainBinding viewBinding;
    private ArrayList<ParentNoteModel> data = new ArrayList<>();

    private static final String SORTING_RESULT_KEY = "sortingKey";

//    TEMP data
    private boolean isOrderAscending = true;

    private ActivityResultLauncher<Intent> mainActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.viewBinding.getRoot());

        // setup and load folders
        setupFolderRecyclerView();

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
                handleSortingOption();
            }
        });

        // Setup Create New Folder Button
        viewBinding.newFolderBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            CreateFolderDialogFragment createFolderDialogFragment = new CreateFolderDialogFragment();
            createFolderDialogFragment.show(fm, "NewFolderDialog");
            createFolderDialogFragment.setAdapter(folderAdapter);
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

            // Listen for the sorting result
            fm.setFragmentResultListener(SORTING_RESULT_KEY, this, (requestKey, result) -> {
                int sortingOption = result.getInt("sortingOption", R.id.nameSortingRb);
                handleSortingOption(sortingOption);
            });


        });

        // Setup Create New Note Button
        viewBinding.newNoteBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            CreateNoteDialogFragment createNoteDialogFragment = new CreateNoteDialogFragment();
            createNoteDialogFragment.show(fm, "NewNoteDialog");
        });



        // Setup Notes Recycler View
        NoteDatabase noteDatabase = new NoteDatabase(getApplicationContext());
        data = noteDatabase.getAllNotes(-1);


        FragmentManager fragmentManager = getSupportFragmentManager();

        NoteAdapter noteAdapter = new NoteAdapter(this, data, fragmentManager);
        this.noteAdapter = noteAdapter;

        GridView gridView = viewBinding.notesGv;
        gridView.setAdapter(noteAdapter);


    }

    private void setupFolderRecyclerView() {
        FolderDatabase folderDatabase = new FolderDatabase(getApplicationContext());
        ArrayList<FolderModel> folders = folderDatabase.getAllFolders();
        folderAdapter = new FolderAdapter(folders, mainActivityResultLauncher);
        viewBinding.folderRv.setAdapter(folderAdapter);
        viewBinding.folderRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private String getNoteType(ParentNoteModel note) {
        String fullString = note.getClass().getCanonicalName();
        return fullString.substring(fullString.lastIndexOf(".") + 1);
    }
    

    /**
     * Refreshes main activity with updated db folder data
     */
    private void reloadFolderData() {
        FolderDatabase folderDatabase = new FolderDatabase(getApplicationContext());
        ArrayList<FolderModel> folders = folderDatabase.getAllFolders();

        if (folderAdapter != null) {
            folderAdapter.setFolderData(folders);
        }
    }

    /**
     * Refreshes main activity with updated db note data
     */
    private void reloadNoteData() {
        NoteDatabase noteDatabase = new NoteDatabase(getApplicationContext());
        data = noteDatabase.getAllNotes(-1);

        if (folderAdapter != null) {
            noteAdapter.setData(data);
            noteAdapter.notifyDataSetChanged();
        }

    }


    /**
     * On resume of main activity, refresh screen with new note data
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the data when the activity is resumed, assuming changes are made to notes in db
        reloadNoteData();
        reloadFolderData();
    }

    /**
     * Updates the MainActivity's Grid View of Note items
     */
    @Override
    public void updateGridView() {
        // updates the grid view when a note is moved to a different folder
        reloadNoteData();
    }

    /**
     * Handles sorting option in adapter.
     * @param sortingOption
     */
    private void handleSortingOption(int sortingOption) {
        // Sort notes based on the selected sorting option
        NoteDatabase noteDatabase = new NoteDatabase(getApplicationContext());
        data = noteDatabase.getAllNotes(-1);
        noteAdapter.setData(data);
        noteAdapter.setSortingOption(sortingOption);
        noteAdapter.setSortOrder(isOrderAscending);

        noteAdapter.sortNotes(); // Add a method in your NoteAdapter to perform the sorting
        noteAdapter.notifyDataSetChanged();
    }

    /**
     * Handles sorting option in adapter.
     *
     */
    private void handleSortingOption() {
        // Sort notes based on the selected sorting option
        NoteDatabase noteDatabase = new NoteDatabase(getApplicationContext());
        data = noteDatabase.getAllNotes(-1);
        noteAdapter.setData(data);

        noteAdapter.setSortOrder(isOrderAscending);

        noteAdapter.sortNotes(); // Add a method in your NoteAdapter to perform the sorting
        noteAdapter.notifyDataSetChanged();
    }

}