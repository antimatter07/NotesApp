package com.mobdeve.xx22.memomate.note.note_checklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.xx22.memomate.model.ChecklistItemModel;
import com.mobdeve.xx22.memomate.databinding.ChecklistTestActivityBinding;
import com.mobdeve.xx22.memomate.model.CheckListNoteModel;

import java.util.ArrayList;

public class ChecklistTestActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChecklistTestActivityBinding viewBinding = ChecklistTestActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());




        // Intent is made and launched in onclicklistener of viewholder in Adapter of MainActivity
        ArrayList<ChecklistItemModel> data = new ArrayList<ChecklistItemModel>();
        data.add(new ChecklistItemModel(true, "kanin"));
        data.add(new ChecklistItemModel(false, "dog food"));
        data.add(new ChecklistItemModel(true, "EYO THIS IS CHECKED"));
        data.add(new ChecklistItemModel(false, "potato"));
        data.add(new ChecklistItemModel(true, "carrot"));


        CheckListNoteModel checklistNote = new CheckListNoteModel("Grocery List",data);

        // create Intent and put checklist data, launch
        Intent intent = new Intent(ChecklistTestActivity.this, ChecklistActivity.class);
        intent.putParcelableArrayListExtra(ChecklistActivity.ITEMLIST_KEY, checklistNote.getCheckItemData());
        intent.putExtra(ChecklistActivity.TITLE_KEY, checklistNote.getTitle());
        intent.putExtra(ChecklistActivity.DATE_CREATED_KEY, checklistNote.getDateCreated());
        intent.putExtra(ChecklistActivity.DATE_MODIFIED_KEY, checklistNote.getDateModified());

        viewBinding.launchListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });








    }
}
