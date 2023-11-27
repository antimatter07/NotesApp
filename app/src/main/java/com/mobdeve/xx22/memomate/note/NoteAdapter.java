package com.mobdeve.xx22.memomate.note;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.mobdeve.xx22.memomate.MainActivity;
import com.mobdeve.xx22.memomate.database.FolderDatabase;
import com.mobdeve.xx22.memomate.partials.NoteOptionsFragment;
import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.note.note_checklist.ChecklistActivity;
import com.mobdeve.xx22.memomate.database.FolderDataHelper;
import com.mobdeve.xx22.memomate.model.CheckListNoteModel;
import com.mobdeve.xx22.memomate.model.ParentNoteModel;
import com.mobdeve.xx22.memomate.model.TextNoteModel;
import com.mobdeve.xx22.memomate.note.note_text.TextNoteActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NoteAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ParentNoteModel> data;
    private FragmentManager fragmentManager;

    private int currentSortingOption = R.id.nameSortingRb;

    private boolean isOrderAscending = true;

    public NoteAdapter(Context context, ArrayList<ParentNoteModel> data, FragmentManager fragmentManager) {
        this.context = context;
        this.data = data;
        this.fragmentManager = fragmentManager;
    }

    /**
     * Replace note data to new list
     * @param newData new arraylsit of parent notes
     */
    public void setData(ArrayList<ParentNoteModel> newData) {
        this.data = newData;
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

        // sets note color based on folder
        FolderDatabase folderDatabase = new FolderDatabase(parent.getContext());
        ConstraintLayout noteCl = convertView.findViewById(R.id.noteItemCl);
        int colorId = folderDatabase.getFolderColor(noteData.getFolderKey());
        int folderColor =  ContextCompat.getColor(noteCl.getContext(), colorId);
        noteCl.setBackgroundColor(folderColor);
        noteTypeIv.setColorFilter(folderColor);
        notePreviewTv.setBackgroundColor(getColorAccent(colorId));

        ImageButton optionsButton = convertView.findViewById(R.id.noteOptionsBtn);



        //listener for options btn
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNoteOptions(noteData);
            }
        });

        // long press
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openNoteOptions(noteData);
                return true;
            }
        });

        // click listener for each note
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;

                switch(noteType) {
                    case "TextNoteModel":
                        intent = new Intent(context, TextNoteActivity.class);

                        intent.putExtra("titleContent", noteData.getTitle());
                        intent.putExtra("noteContent", ((TextNoteModel) noteData).getText());
                        intent.putExtra("noteID", noteData.getNoteID());
                        intent.putExtra("folderKey", noteData.getFolderKey());
                        intent.putExtra("noteColor", folderDatabase.getFolderColor(noteData.getFolderKey()));
                        intent.putExtra("noteFontColor", ((TextNoteModel) noteData).getFontColor());
                        intent.putExtra("noteFontSize", ((TextNoteModel) noteData).getFontSize());

                        context.startActivity(intent);
                        break;
                    case "CheckListNoteModel":
                        intent = new Intent(context, ChecklistActivity.class);
                        intent.putExtra(ChecklistActivity.TITLE_KEY, noteData.getTitle());
                        intent.putExtra(ChecklistActivity.ITEMLIST_KEY, ((CheckListNoteModel) noteData).getCheckItemData());

                        Log.d("In note adapter intent passing", "check item id of first" + ((CheckListNoteModel) noteData).getCheckItemData().get(0).getNoteId());


                        intent.putExtra("noteID", noteData.getNoteID());
                        intent.putExtra("folderKey", noteData.getFolderKey());

                        intent.putExtra("noteColor", folderDatabase.getFolderColor(noteData.getFolderKey()));

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

    public void setSortingOption(int sortingOption) {
        currentSortingOption = sortingOption;
    }

    public void setSortOrder(boolean b) {
        this.isOrderAscending = b;

    }

    public void sortNotes() {
        Comparator<ParentNoteModel> comparator;

        if (currentSortingOption == R.id.nameSortingRb) {
            // Sort by title
            comparator = Comparator.comparing(ParentNoteModel::getTitle, String.CASE_INSENSITIVE_ORDER);
        } else if (currentSortingOption == R.id.createdDateSortingRb) {
            // Sort by date created (assuming date is stored as a string)
            comparator = Comparator.comparing(ParentNoteModel::getDateCreated, String.CASE_INSENSITIVE_ORDER);
        } else if (currentSortingOption == R.id.modifiedDateSortingRb) {
            // Sort by date modified (assuming date is stored as a string)
            comparator = Comparator.comparing(ParentNoteModel::getDateModified, String.CASE_INSENSITIVE_ORDER);
        } else {
            // Default sorting by title
            comparator = Comparator.comparing(ParentNoteModel::getTitle, String.CASE_INSENSITIVE_ORDER);
        }

        if (!isOrderAscending) {
            // Reverse the comparator if it's descending order
            comparator = comparator.reversed();
        }

        Collections.sort(data, comparator);
    }

    private void openNoteOptions(ParentNoteModel noteData) {
        // Open note options modal
        NoteOptionsFragment noteOptionsFragment = new NoteOptionsFragment();

        //pass noteID for deletions
        noteOptionsFragment.setNoteID(noteData.getNoteID());
        noteOptionsFragment.setFolderID(noteData.getFolderKey());

        noteOptionsFragment.show(fragmentManager, "NoteOptionsDialog");
    }


}