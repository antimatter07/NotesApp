package com.mobdeve.xx22.villarica.matthew.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.mobdeve.xx22.villarica.matthew.notes.databinding.ChecklistActivityBinding;


import java.util.ArrayList;

public class ChecklistActivity extends AppCompatActivity {
    public static final String ITEMLIST_KEY = "ITEMLIST_KEY";
    public static final String TITLE_KEY = "TITLE_KEY";

    public static final String DATE_CREATED_KEY = "DATE_KEY";

    public static final String DATE_MODIFIED_KEY = "DATE_MODIFIED_KEY";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChecklistActivityBinding viewBinding = ChecklistActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        //retrieve data from intent
        Intent intent = getIntent();
        String titleString = intent.getStringExtra(TITLE_KEY);
        ArrayList<ChecklistItemModel> listData = intent.getParcelableArrayListExtra(ITEMLIST_KEY);




        //set up views and adapter with received data
        viewBinding.noteTitle.setText(titleString);
        ChecklistAdapter adapter = new ChecklistAdapter(listData);
        RecyclerView recyclerview = viewBinding.recyclerView;

        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        //add notes with button
        viewBinding.addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.add(new ChecklistItemModel(false, ""));
                adapter.notifyItemInserted(listData.size() - 1);
            }
        });


    }
}
