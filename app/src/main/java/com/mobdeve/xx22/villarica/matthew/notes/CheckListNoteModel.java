package com.mobdeve.xx22.villarica.matthew.notes;

import java.util.ArrayList;

public class CheckListNoteModel {

    private ArrayList<ChecklistItemModel> checkItemData;


    private String title;

    public CheckListNoteModel(ArrayList<ChecklistItemModel> checkItemData, String title) {
        this.checkItemData = checkItemData;
        this.title = title;
    }
}
