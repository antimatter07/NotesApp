package com.mobdeve.xx22.villarica.matthew.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.ChecklistActivityBinding;

import java.util.ArrayList;

public class TextNoteActivity extends AppCompatActivity {
    private TextView noteTextView;
    private TextView noteTitleView;
    private String noteContent = "";
    private String titleContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textnote_activity);

        noteTextView = findViewById(R.id.noteBodyText);
        noteTitleView = findViewById(R.id.noteTitleText);

        // Initially set the TextView to display the note content
        noteTextView.setText(noteContent);
        noteTitleView.setText(titleContent);

        // Retrieve note content if available from Intent extras
        if (getIntent().hasExtra("noteContent")) {
            noteContent = getIntent().getStringExtra("noteContent");
            noteTextView.setText(noteContent);
        }

        // Retrieve note content if available from Intent extras
        if (getIntent().hasExtra("titleContent")) {
            titleContent = getIntent().getStringExtra("titleContent");
            noteTitleView.setText(titleContent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save the note content when the activity is paused
        noteContent = noteTextView.getText().toString();
        titleContent = noteTitleView.getText().toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the TextView with the latest note content when the activity is resumed
        noteTextView.setText(noteContent);
        noteTitleView.setText(titleContent);
    }

}

