package com.mobdeve.xx22.memomate.search;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.model.ParentNoteModel;
import com.mobdeve.xx22.memomate.databinding.ActivitySearchBinding;
import com.mobdeve.xx22.memomate.note.NoteAdapter;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding viewBinding;
    private NoteAdapter searchNoteAdapter;
    private ArrayList<ParentNoteModel> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(this.viewBinding.getRoot());

        // automatically open the keyboard for the search bar
        this.viewBinding.searchNoteSv.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.viewBinding.searchNoteSv, InputMethodManager.SHOW_IMPLICIT);

        // Initialize the adapter
        this.searchNoteAdapter = new NoteAdapter(this, searchResults, getSupportFragmentManager());

        // Set the adapter to the GridView
        this.viewBinding.searchResultsGv.setAdapter(searchNoteAdapter);

        // Set the query listener for the SearchView
        this.viewBinding.searchNoteSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                imm.hideSoftInputFromWindow(viewBinding.searchNoteSv.getWindowToken(), 0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Update the search results based on the entered query
                searchNotes(newText);
                return false;
            }
        });
    }

    private void searchNotes(String query) {
        // query needed notes in db
        NoteDatabase noteDatabase = new NoteDatabase(SearchActivity.this);
        ArrayList<ParentNoteModel> matchingNotes = noteDatabase.searchNotes(query);


        //update the adapter with the new search results
        searchNoteAdapter.setData(matchingNotes);
        searchNoteAdapter.notifyDataSetChanged();
    }

    /**
     * Refreshes search results with updated db data
     */
    private void reloadNoteData() {
        NoteDatabase noteDatabase = new NoteDatabase(getApplicationContext());
        searchResults = noteDatabase.getAllNotes(-1);


        searchNoteAdapter.setData(searchResults);
        searchNoteAdapter.notifyDataSetChanged();


    }

    /**
     * On resume of search act, refresh screen with new note data, in case changes hav been made
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the data when the activity is resumed, assuming changes are made to notes in db
        reloadNoteData();
    }
}