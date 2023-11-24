package com.mobdeve.xx22.memomate.folder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.model.ParentNoteModel;
import com.mobdeve.xx22.memomate.note.NoteAdapter;
import com.mobdeve.xx22.memomate.partials.ChangeFolderFragment;
import com.mobdeve.xx22.memomate.partials.CreateNoteDialogFragment;
import com.mobdeve.xx22.memomate.partials.FolderOptionsFragment;
import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.search.SearchActivity;
import com.mobdeve.xx22.memomate.partials.SortingOptionsDialogFragment;
import com.mobdeve.xx22.memomate.databinding.FolderActivityBinding;

import java.util.ArrayList;

public class ViewFolderActivity extends AppCompatActivity
        implements ChangeFolderFragment.UpdateActivityGridView {

    public static final String folderIdKey = "FOLDER_ID",
                            folderNameKey = "FOLDER_NAME_KEY",
                            folderColorKey = "FOLDER_COLOR_KEY";


    //name to display
    private String folderName;
    private int folderColor;
    private int folderId;

    private FolderActivityBinding viewBinding;
    private NoteAdapter noteAdapter;
    private boolean isOrderAscending = true;
    private ArrayList<ParentNoteModel> data = new ArrayList<>();

    private static final String SORTING_RESULT_KEY = "sortingKey";

    @Override
    public void updateGridView() {
        // updates the grid view when a note is moved to a different folder
        reloadNoteData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.viewBinding = FolderActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // set note adapter
        //set folder to name in intent
        Intent intent = getIntent();
        folderId = intent.getIntExtra(folderIdKey, -1);
        folderName = intent.getStringExtra(folderNameKey);
        viewBinding.folderNameTv.setText(folderName);
        folderColor = ContextCompat.getColor(viewBinding.menuBarLl.getContext(), intent.getIntExtra(folderColorKey, R.color.folderDefault));
        viewBinding.menuBarLl.setBackgroundColor(folderColor);
        // viewBinding.newNoteBtn.setBackgroundTintList(ColorStateList.valueOf(folderColor));


        //TODO: set up notes recycler view to show notes in folder
        NoteDatabase noteDatabase = new NoteDatabase(getApplicationContext());
        data = noteDatabase.getAllNotes(folderId);

        FragmentManager fragmentManager = getSupportFragmentManager();

        NoteAdapter noteAdapter = new NoteAdapter(this, data, fragmentManager);
        this.noteAdapter = noteAdapter;

        GridView gridView = viewBinding.notesGv;
        gridView.setAdapter(noteAdapter);

        // Setup Search Button
        viewBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewFolderActivity.this, SearchActivity.class);

                intent.putExtra("folderKey", folderId);

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
            createNoteDialogFragment.setFolderId(this.folderId);
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
                handleSortingOption();
            }
        });

        // Setup Folder Options Button
        viewBinding.folderOptionsBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            FolderOptionsFragment folderOptionsFragment = new FolderOptionsFragment();
            folderOptionsFragment.show(fm, "FolderOptionsDialog");
        });

    }

    /**
     * Refreshes main activity with updated db data
     */
    private void reloadNoteData() {
        NoteDatabase noteDatabase = new NoteDatabase(this);
        data = noteDatabase.getAllNotes(this.folderId);

        if (noteAdapter != null) {
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
    }


    /**
     * Handles sorting option in adapter.
     * @param sortingOption
     */
    private void handleSortingOption(int sortingOption) {
        // Sort notes based on the selected sorting option
        NoteDatabase noteDatabase = new NoteDatabase(getApplicationContext());
        data = noteDatabase.getAllNotes(folderId);
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
        data = noteDatabase.getAllNotes(folderId);
        noteAdapter.setData(data);

        noteAdapter.setSortOrder(isOrderAscending);

        noteAdapter.sortNotes(); // Add a method in your NoteAdapter to perform the sorting
        noteAdapter.notifyDataSetChanged();
    }

}
