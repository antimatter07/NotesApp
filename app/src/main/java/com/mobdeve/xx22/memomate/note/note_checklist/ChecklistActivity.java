package com.mobdeve.xx22.memomate.note.note_checklist;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;




import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.databinding.ChecklistItemBinding;
import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.model.CheckListNoteModel;
import com.mobdeve.xx22.memomate.model.TextNoteModel;
import com.mobdeve.xx22.memomate.partials.NoteOptionsFragment;
import com.mobdeve.xx22.memomate.databinding.ChecklistActivityBinding;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChecklistActivity extends AppCompatActivity {
    public static final String ITEMLIST_KEY = "ITEMLIST_KEY";
    public static final String TITLE_KEY = "TITLE_KEY";
    public static final String FOLDER_KEY = "FOLDER_KEY";
    public static final String DATE_CREATED_KEY = "DATE_KEY";
    public static final String DATE_MODIFIED_KEY = "DATE_MODIFIED_KEY";

    /**
     * note id of current checklist note
     */
    private int currentNoteID;

    /**
     * Thread for db operations
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private NoteDatabase noteDatabase;

    private int noteColor;
    // Handler associated with the main (UI) thread
    private Handler mainHandler;

    private boolean isTitleContentChanged = false;

    private String currentDateTime;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChecklistActivityBinding viewBinding = ChecklistActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        noteDatabase = new NoteDatabase(getApplicationContext());

        //make handler for ui updates
        mainHandler = new Handler(getMainLooper());

        //retrieve data from intent
        Intent intent = getIntent();
        String titleString = intent.getStringExtra(TITLE_KEY);
        ArrayList<ChecklistItemModel> listData = intent.getParcelableArrayListExtra(ITEMLIST_KEY);

        // set colors of note bar and checkboxes
        noteColor = ContextCompat.getColor(this, getIntent().getIntExtra("noteColor", R.color.folderDefault));
        viewBinding.noteBarCl.setBackgroundColor(noteColor);



        //set up views and adapter with received data
        viewBinding.noteTitle.setText(titleString);
        ChecklistAdapter adapter = new ChecklistAdapter(getApplicationContext(), listData, noteDatabase, noteColor);
        RecyclerView recyclerview = viewBinding.recyclerView;

        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        // create a new item by default when creating new checklist




        recyclerview.setAdapter(adapter);

        if(listData.size() == 0)
            adapter.notifyItemInserted(listData.size() - 1);



        // Setup Note Options Button
        viewBinding.noteOptionsBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            NoteOptionsFragment noteOptionsFragment = new NoteOptionsFragment();
            noteOptionsFragment.show(fm, "NoteOptionsDialog");
        });

        currentNoteID = getIntent().getIntExtra("noteID", -1);
        int folderKey = getIntent().getIntExtra(ChecklistActivity.FOLDER_KEY, -1);

        Log.d("IN CHECKLIST ACT", "note id: " + currentNoteID);
        Log.d("IN CHECKLIST ACT", "folder key: " + folderKey);

        //if noteID retrieved is default value, create a new checklist note in db
        if(currentNoteID == -1) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    //make new text note and insert into db

                    CheckListNoteModel checklistNote = new CheckListNoteModel(titleString,
                            folderKey,
                            listData);

                    currentNoteID = noteDatabase.addCheckListNote(checklistNote);

                }
            });
        }

        if (listData.size() == 0) {

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    int newCheckListItemId = noteDatabase.addCheckListItem(currentNoteID);
                    ChecklistItemModel newItem = new ChecklistItemModel(false, "");
                    newItem.setItemId(newCheckListItemId);

                    // Update the UI on the main thread
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listData.add(newItem);
                            adapter.notifyItemInserted(listData.size() - 1);
                        }
                    });
                }
            });

        }

        //add notes with button
        viewBinding.addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.add(new ChecklistItemModel(false, ""));


                executorService.execute(new Runnable() {
                    @Override
                    public void run() {

                        int newCheckListItemId = noteDatabase.addCheckListItem(currentNoteID);
                        //UI updates after adding new check list item
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listData.get(listData.size() - 1).setItemId(newCheckListItemId);
                                adapter.notifyItemInserted(listData.size() - 1);


                            }
                        });

                    }
                });
                ;
            }
        });

        viewBinding.noteTitle.addTextChangedListener(new TextWatcher() {
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
                            String updatedTitle = viewBinding.noteTitle.getText().toString();

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



    /**
     * Gets current date and time in proper format for sorting in SQLite
     * @return SimpleDateFormat when ParantNoteModel was instantiated.
     */
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

}
