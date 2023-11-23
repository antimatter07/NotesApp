package com.mobdeve.xx22.memomate.database;

import com.mobdeve.xx22.memomate.model.FolderModel;
import com.mobdeve.xx22.memomate.R;

import java.util.ArrayList;

public class FolderDataHelper {

    private static ArrayList<FolderModel> data = new ArrayList<>();

    public static ArrayList<FolderModel> generateFolderData() {

        data.add(new FolderModel(-1, "MemoMate", R.color.folderDefault));
        data.add(new FolderModel(0, "Dino Drafts", R.color.folderRed));
        data.add(new FolderModel(1));
        data.add(new FolderModel(2, "TODOs", R.color.folderCyan));
//        data.add(new FolderModel(3));
//        data.add(new FolderModel(4, "MOBDEVE Notes", R.color.folderGreen));

        return data;
    }

}
