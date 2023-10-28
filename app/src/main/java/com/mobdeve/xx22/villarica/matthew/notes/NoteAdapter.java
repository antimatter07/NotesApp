package com.mobdeve.xx22.villarica.matthew.notes;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.NoteItemBinding;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
    private ArrayList<ParentNoteModel> data;
    private FragmentManager fragmentManager;

    public NoteAdapter(ArrayList<ParentNoteModel> data, FragmentManager fragmentManager) {
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
        dateModTv.setText(noteData.getDateModified());
        noteTitleTv.setText(noteData.getTitle());

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







        return convertView;
    }
}