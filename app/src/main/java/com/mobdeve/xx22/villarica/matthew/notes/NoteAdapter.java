package com.mobdeve.xx22.villarica.matthew.notes;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.NoteItemBinding;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ParentNoteModel> data;
    private FragmentManager fragmentManager;

    public NoteAdapter(Context context, ArrayList<ParentNoteModel> data, FragmentManager fragmentManager) {
        this.context = context;
        this.data = data;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        }

        // Bind data to the views within your layout
        ParentNoteModel noteData = data.get(position);
        TextView dateModTv = convertView.findViewById(R.id.dateModTv);
        TextView noteTitleTv = convertView.findViewById(R.id.noteTitleTv);
        TextView notePreviewTv = convertView.findViewById(R.id.notePreviewTv);
        dateModTv.setText(noteData.getDateModified());
        noteTitleTv.setText(noteData.getTitle());

        // set note icon and preview
        String noteType = getNoteType(noteData);
        ImageView noteTypeIv = convertView.findViewById(R.id.noteTypeIv);

        switch(noteType) {
            case "TextNoteModel":
                notePreviewTv.setText(((TextNoteModel) noteData).getText());
                noteTypeIv.setImageResource(R.drawable.ic_note_text);
                break;
            case "CheckListNoteModel":
                String items = ((CheckListNoteModel) noteData).getCheckItemDataStrings();
                notePreviewTv.setText(items);
                noteTypeIv.setImageResource(R.drawable.ic_note_checklist);
                break;
            case "DrawingNoteModel":
                noteTypeIv.setImageResource(R.drawable.ic_note_drawing);
                break;
        }

        // TODO: set note color based on folder
        ConstraintLayout noteCl = convertView.findViewById(R.id.noteItemCl);
        int colorId = FolderDataHelper.getFolderColor(noteData.getFolderKey());
        int folderColor =  ContextCompat.getColor(noteCl.getContext(), colorId);
        noteCl.setBackgroundColor(folderColor);
        noteTypeIv.setColorFilter(folderColor);
        notePreviewTv.setBackgroundColor(getColorAccent(colorId));

        ImageButton optionsButton = convertView.findViewById(R.id.noteOptionsBtn);

        //listener for options btn
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open note options modal
                NoteOptionsFragment noteOptionsFragment = new NoteOptionsFragment();
                noteOptionsFragment.show(fragmentManager, "NoteOptionsDialog");

            }
        });

        // click listener for each note
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                Log.d("GridView", noteType);
                switch(noteType) {
                    case "TextNoteModel":
                        intent = new Intent(context, TextNoteActivity.class);
                        intent.putExtra("titleContent", noteData.getTitle());
                        intent.putExtra("noteContent", ((TextNoteModel) noteData).getText());
                        context.startActivity(intent);
                        break;
                    case "CheckListNoteModel":
                        intent = new Intent(context, ChecklistActivity.class);
                        intent.putExtra(ChecklistActivity.TITLE_KEY, noteData.getTitle());
                        intent.putExtra(ChecklistActivity.ITEMLIST_KEY, ((CheckListNoteModel) noteData).getCheckItemData());
                        context.startActivity(intent);
                        break;
                    case "DrawingNoteModel":
                        break;
                }

            }
        });


        return convertView;
    }


    private String getNoteType(ParentNoteModel note) {
        String fullString = note.getClass().getCanonicalName();
        return fullString.substring(fullString.lastIndexOf(".") + 1);
    }

    private int getColorAccent (int folderColor) {
        int accentId = R.color.whiteAccent;

        if (folderColor == R.color.folderDefault) accentId = R.color.whiteAccent;
        else if (folderColor == R.color.folderRed) accentId = R.color.redAccent;
        else if (folderColor == R.color.folderOrange) accentId = R.color.orangeAccent;
        else if (folderColor == R.color.folderYellow) accentId = R.color.yellowAccent;
        else if (folderColor == R.color.folderGreen) accentId = R.color.greenAccent;
        else if (folderColor == R.color.folderCyan) accentId = R.color.cyanAccent;
        else if (folderColor == R.color.folderBlue) accentId = R.color.blueAccent;
        else if (folderColor == R.color.folderPurple) accentId = R.color.purpleAccent;

       return ContextCompat.getColor(context, accentId);
    }
}