package com.mobdeve.xx22.memomate.note.note_text;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.model.TextNoteModel;
import com.mobdeve.xx22.memomate.partials.NoteOptionsFragment;
import com.mobdeve.xx22.memomate.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class TextNoteActivity extends AppCompatActivity {

     public static final String TEXT_KEY = "TEXT_KEY";
     public static final String TITLE_KEY = "TITLE_KEY";
     public static final String DATE_CREATED_KEY = "DATE_KEY";
     public static final String DATE_MODIFIED_KEY = "DATE_MODIFIED_KEY";

     private ConstraintLayout noteBar;
     private TextView noteTextView;
    private TextView noteTitleView;
    private String noteContent = "";
    private String titleContent = "";
    private int noteColor;

    private boolean isNoteContentChanged = false;
    private boolean isTitleContentChanged = false;
    private int currentNoteID = -1;

    private String currentDateTime = getCurrentDateTime();

    /**
     * Thread for db operations
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    //note db
    private NoteDatabase noteDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textnote_activity);

        noteDatabase = new NoteDatabase(getApplicationContext());

        noteTextView = findViewById(R.id.noteBodyText);
        noteTitleView = findViewById(R.id.noteTitleText);
        noteBar = findViewById(R.id.noteBarCl);

        noteColor = ContextCompat.getColor(this, getIntent().getIntExtra("noteColor", R.color.folderDefault));
        noteBar.setBackgroundColor(noteColor);

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


        currentNoteID = getIntent().getIntExtra("noteID", -1);
        int folderKey = getIntent().getIntExtra("folderKey", -1);

        //if noteID retrieved is default value, create new text note in db
        if(currentNoteID == -1) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    NoteDatabase db = new NoteDatabase(getApplicationContext());
                    //make new text note and insert into db

                    TextNoteModel textNote = new TextNoteModel(getIntent().getStringExtra("titleContent"),
                            folderKey,
                            getIntent().getStringExtra("noteContent"));

                    currentNoteID = db.addTextNote(textNote);

                }
            });
        }

        // Setup Note Options Button
        ImageButton noteOptionsBtn = findViewById(R.id.textNoteOptionsBtn);
        noteOptionsBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            NoteOptionsFragment noteOptionsFragment = new NoteOptionsFragment();

            //set current noteID so that its visible inside fragment for deletion, locking, etc
            noteOptionsFragment.setNoteID(currentNoteID);

            noteOptionsFragment.show(fm, "NoteOptionsDialog");
        });

        noteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                isNoteContentChanged = true;
                currentDateTime =getCurrentDateTime();

                if(isNoteContentChanged) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {



                            String updatedNoteContent = noteTextView.getText().toString();


                            if (isNoteContentChanged) {
                                // Update note content in the database
                                noteDatabase.updateTextNoteContent(currentNoteID, updatedNoteContent, currentDateTime);
                                isNoteContentChanged = false; // Reset flag
                            }
                        }
                    });

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        noteTitleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                isTitleContentChanged = true;
                currentDateTime = getCurrentDateTime();

                if(isTitleContentChanged) {

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {


                            // Get the updated content
                            String updatedTitle = noteTitleView.getText().toString();

                            // Update title in the database
                            noteDatabase.updateNoteTitle(currentNoteID, updatedTitle, currentDateTime);
                            isTitleContentChanged = false; // Reset flag


                        }
                    });

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();



        // Save the note content when the activity is paused
        /*if (isNoteContentChanged || isTitleContentChanged) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    NoteDatabase db = new NoteDatabase(getApplicationContext());

                    // Get the updated content
                    String updatedTitle = noteTitleView.getText().toString();
                    String updatedNoteContent = noteTextView.getText().toString();

                    // Update the database only if the content has changed
                    if (isTitleContentChanged) {
                        // Update title in the database
                        db.updateNoteTitle(currentNoteID, updatedTitle, currentDateTime);
                        isTitleContentChanged = false; // Reset flag
                    }

                    if (isNoteContentChanged) {
                        // Update note content in the database
                        db.updateTextNoteContent(currentNoteID, updatedNoteContent, currentDateTime);
                        isNoteContentChanged = false; // Reset flag
                    }
                }
            });
        }
        */

    }


    @Override
    protected void onResume() {
        super.onResume();
        // Update the TextView with the latest note content when the activity is resumed
        //noteTextView.setText(noteContent);
        //noteTitleView.setText(titleContent);


    }

    /**
     * Gets current date and time in proper format for sorting in SQLite
     * @return SimpleDateFormat when ParantNoteModel was instantiated.
     */
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

}

