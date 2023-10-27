package com.mobdeve.xx22.villarica.matthew.notes;

import java.util.ArrayList;

public class CheckListNoteModel extends ParentNoteModel{

    private ArrayList<ChecklistItemModel> checkItemData;




    public CheckListNoteModel(String title, String dateCreated, ArrayList<ChecklistItemModel> checkItemData) {

        super(title, dateCreated);
        this.checkItemData = checkItemData;


    }

    public void addToCheckList(ChecklistItemModel checklistItem) {
        checkItemData.add(checklistItem);
    }

    public void removeFromCheckList(int adapterPosition) {
        checkItemData.remove(adapterPosition);

    }

    public ArrayList<ChecklistItemModel> getCheckItemData() {
        return checkItemData;
    }
}
