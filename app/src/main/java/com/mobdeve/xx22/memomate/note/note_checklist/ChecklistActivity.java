package com.mobdeve.xx22.memomate.note.note_checklist;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.databinding.ChecklistItemBinding;
import com.mobdeve.xx22.memomate.partials.NoteOptionsFragment;
import com.mobdeve.xx22.memomate.databinding.ChecklistActivityBinding;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;


import java.util.ArrayList;

public class ChecklistActivity extends AppCompatActivity {
    public static final String ITEMLIST_KEY = "ITEMLIST_KEY";
    public static final String TITLE_KEY = "TITLE_KEY";
    public static final String FOLDER_KEY = "FOLDER_KEY";
    public static final String DATE_CREATED_KEY = "DATE_KEY";
    public static final String DATE_MODIFIED_KEY = "DATE_MODIFIED_KEY";

    private int noteColor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChecklistActivityBinding viewBinding = ChecklistActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        //retrieve data from intent
        Intent intent = getIntent();
        String titleString = intent.getStringExtra(TITLE_KEY);
        ArrayList<ChecklistItemModel> listData = intent.getParcelableArrayListExtra(ITEMLIST_KEY);

        // set colors of note bar and checkboxes
        noteColor = ContextCompat.getColor(this, getIntent().getIntExtra("noteColor", R.color.folderDefault));
        viewBinding.noteBarCl.setBackgroundColor(noteColor);


        //set up views and adapter with received data
        viewBinding.noteTitle.setText(titleString);
        ChecklistAdapter adapter = new ChecklistAdapter(listData, this, noteColor);
        RecyclerView recyclerview = viewBinding.recyclerView;

        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        // create a new item by default when creating new checklist
        if (listData.size() == 0) {
            listData.add(new ChecklistItemModel(false, ""));
            adapter.notifyItemInserted(listData.size() - 1);
        }

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


    }
}
