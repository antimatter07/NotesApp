package com.mobdeve.xx22.villarica.matthew.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.ChecklistActivityBinding;
import com.mobdeve.xx22.villarica.matthew.notes.databinding.TextnoteActivityBinding;

import java.util.ArrayList;

public class TextNoteActivity extends AppCompatActivity {
    public static final String TEXT_KEY = "TEXT_KEY";
    public static final String TITLE_KEY = "TITLE_KEY";
    public static final String DATE_CREATED_KEY = "DATE_KEY";
    public static final String DATE_MODIFIED_KEY = "DATE_MODIFIED_KEY";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextnoteActivityBinding viewBinding = TextnoteActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        Intent intent = getIntent();
        String titleString = intent.getStringExtra(TITLE_KEY);
        String textString = intent.getStringExtra(TEXT_KEY);

        //set up views and adapter with received data
        viewBinding.noteTitleText.setText(titleString);
        viewBinding.noteBodyText.setText(textString);
        // TextNoteAdapter adapter = null;

    }


}