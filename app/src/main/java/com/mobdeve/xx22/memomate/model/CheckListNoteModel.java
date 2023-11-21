package com.mobdeve.xx22.memomate.model;

import java.util.ArrayList;

public class CheckListNoteModel extends ParentNoteModel{

    private ArrayList<ChecklistItemModel> checkItemData;


    public CheckListNoteModel(String title, ArrayList<ChecklistItemModel> checkItemData) {
        super(title);
        this.checkItemData = checkItemData;
        super.setNoteType("checklist");

    }

    public CheckListNoteModel(String title, int folderKey, ArrayList<ChecklistItemModel> checkItemData) {

        super(title, folderKey);
        this.checkItemData = checkItemData;
        super.setNoteType("checklist");

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

    public void setCheckItemData(ArrayList<ChecklistItemModel> checkItemData) {
        this.checkItemData = checkItemData;
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
