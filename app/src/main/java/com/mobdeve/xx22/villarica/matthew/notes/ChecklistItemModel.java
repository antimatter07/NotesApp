package com.mobdeve.xx22.villarica.matthew.notes;

public class ChecklistItemModel {
    private boolean isChecked = false;
    private String text;

    public ChecklistItemModel (boolean isChecked, String text) {
        this.isChecked = isChecked;

    }

    public String getText() {
        return text;
    }

    public boolean getIsChecked() {
        return isChecked;
    }
}
