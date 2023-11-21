package com.mobdeve.xx22.memomate.note.note_text;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.model.TextNoteModel;
import com.mobdeve.xx22.memomate.partials.NoteOptionsFragment;
import com.mobdeve.xx22.memomate.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class TextNoteActivity extends AppCompatActivity {

     public static final String TEXT_KEY = "TEXT_KEY";
     public static final String TITLE_KEY = "TITLE_KEY";
     public static final String DATE_CREATED_KEY = "DATE_KEY";
     public static final String DATE_MODIFIED_KEY = "DATE_MODIFIED_KEY";
    private TextView noteTextView;
    private TextView noteTitleView;
    private String noteContent = "";
    private String titleContent = "";

    /**
     * Thread for db operations
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

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

        int noteID = getIntent().getIntExtra("noteID", -1);
        int folderKey = getIntent().getIntExtra("folderKey", 0);

        //if noteID retrieved is default value, creat new text note in db
        if(noteID == -1) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    NoteDatabase db = new NoteDatabase(getApplicationContext());
                    //make new text note and insert into db

                    TextNoteModel textNote = new TextNoteModel(getIntent().getStringExtra("titleContent"),
                            folderKey,
                            getIntent().getStringExtra("noteContent"));

                    db.addTextNote(textNote);

                }
            });
        }

        // Setup Note Options Button
        ImageButton noteOptionsBtn = findViewById(R.id.textNoteOptionsBtn);
        noteOptionsBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            NoteOptionsFragment noteOptionsFragment = new NoteOptionsFragment();
            noteOptionsFragment.show(fm, "NoteOptionsDialog");
        });
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

