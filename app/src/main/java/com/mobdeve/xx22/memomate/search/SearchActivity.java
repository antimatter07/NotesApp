package com.mobdeve.xx22.memomate.search;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.xx22.memomate.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(this.viewBinding.getRoot());

        // automatically open the keyboard for the search bar
        this.viewBinding.searchNoteSv.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.viewBinding.searchNoteSv, InputMethodManager.SHOW_IMPLICIT);

        this.viewBinding.searchNoteSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                imm.hideSoftInputFromWindow(viewBinding.searchNoteSv.getWindowToken(), 0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO: Fill the gridview of notes that match the query
                return false;
            }
        });


    }

}
