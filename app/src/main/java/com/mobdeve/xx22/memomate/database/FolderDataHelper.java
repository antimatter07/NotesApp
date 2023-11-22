package com.mobdeve.xx22.memomate.database;

import com.mobdeve.xx22.memomate.model.FolderModel;
import com.mobdeve.xx22.memomate.R;

import java.util.ArrayList;

public class FolderDataHelper {

    private static ArrayList<FolderModel> data = new ArrayList<>();
    private static int folderCount = 5;    // serves as the temporary key tracker

    public static ArrayList<FolderModel> generateFolderData() {

        data.add(new FolderModel(0, "Drafts", R.color.folderRed));
        data.add(new FolderModel(1));
        data.add(new FolderModel(2, "TODOs", R.color.folderCyan));
        data.add(new FolderModel(3));
        data.add(new FolderModel(4, "MOBDEVE Notes", R.color.folderGreen));

        return data;
    }

//    public static void addFolder(String name, int color) {
//        data.add(new FolderModel(folderCount, name, color));
//        folderCount++;
//    }

    public static int getFolderColor(int key) {
        for (FolderModel folder : data) {
            if (folder.getFolderId() == key) {
                return folder.getColorResId();
            }
        }
        return R.color.folderDefault;
    }

}
