package com.mobdeve.xx22.memomate.note.note_checklist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.model.CheckListNoteModel;
import com.mobdeve.xx22.memomate.model.TextNoteModel;
import com.mobdeve.xx22.memomate.partials.NoteOptionsFragment;
import com.mobdeve.xx22.memomate.databinding.ChecklistActivityBinding;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;


import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChecklistActivity extends AppCompatActivity {
    public static final String ITEMLIST_KEY = "ITEMLIST_KEY";
    public static final String TITLE_KEY = "TITLE_KEY";
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChecklistActivityBinding viewBinding = ChecklistActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        noteDatabase = new NoteDatabase(getApplicationContext());

        //retrieve data from intent
        Intent intent = getIntent();
        String titleString = intent.getStringExtra(TITLE_KEY);
        ArrayList<ChecklistItemModel> listData = intent.getParcelableArrayListExtra(ITEMLIST_KEY);



        //set up views and adapter with received data
        viewBinding.noteTitle.setText(titleString);
        RecyclerView recyclerview = viewBinding.recyclerView;

        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        // create a new item by default when creating new checklist
        if (listData.size() == 0) {
            listData.add(new ChecklistItemModel(false, ""));
            //adapter.notifyItemInserted(listData.size() - 1);
        }

        ChecklistAdapter adapter = new ChecklistAdapter(getApplicationContext(), listData, noteDatabase);


        recyclerview.setAdapter(adapter);

        if(listData.size() == 0)
            adapter.notifyItemInserted(listData.size() - 1);

        //add notes with button
        viewBinding.addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.add(new ChecklistItemModel(false, ""));
                adapter.notifyItemInserted(listData.size() - 1);
            }
        });

        // Setup Note Options Button
        viewBinding.noteOptionsBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            NoteOptionsFragment noteOptionsFragment = new NoteOptionsFragment();
            noteOptionsFragment.show(fm, "NoteOptionsDialog");
        });

        currentNoteID = getIntent().getIntExtra("noteID", -1);
        int folderKey = getIntent().getIntExtra("folderKey", 0);

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


    }
}
