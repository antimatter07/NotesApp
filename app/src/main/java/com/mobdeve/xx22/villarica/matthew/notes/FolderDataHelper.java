package com.mobdeve.xx22.villarica.matthew.notes;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class FolderDataHelper {

    public static ArrayList<FolderModel> generateFolderData() {
        ArrayList<FolderModel> data = new ArrayList<>();

        data.add(new FolderModel(0, "Drafts", Color.RED));
        data.add(new FolderModel(1));
        data.add(new FolderModel(2, "TODOs", Color.CYAN));
        data.add(new FolderModel(3));
        data.add(new FolderModel(4, "MOBDEVE Notes", Color.GREEN));

        return data;
    }
}
