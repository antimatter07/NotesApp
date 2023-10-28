package com.mobdeve.xx22.villarica.matthew.notes;

import java.util.ArrayList;

public class CheckListNoteModel extends ParentNoteModel{

    private ArrayList<ChecklistItemModel> checkItemData;


    public CheckListNoteModel(String title, String dateCreated, ArrayList<ChecklistItemModel> checkItemData) {
        super(title, dateCreated);
        this.checkItemData = checkItemData;

    }

    public CheckListNoteModel(String title, String dateCreated, int folderKey, ArrayList<ChecklistItemModel> checkItemData) {

        super(title, folderKey, dateCreated);
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

    public String getCheckItemDataStrings() {
        StringBuilder combinedItems = new StringBuilder();
        for (ChecklistItemModel item: checkItemData) {
            if (item.getIsChecked()){
                combinedItems.append("[✓] ");
            }
            else combinedItems.append("[   ] ");
            combinedItems.append(item.getText());
            combinedItems.append("\n");
        }

        return combinedItems.toString();
    }
}
