package com.mobdeve.xx22.memomate.model;

public class TextNoteModel extends ParentNoteModel {
    private String text = "";

    public TextNoteModel (String title, String dateCreated, String text) {
        super(title, -1, dateCreated);
        this.text = text;
    }
    public TextNoteModel (String title, String dateCreated, int folderKey, String text) {
        super(title, folderKey, dateCreated);
        this.text = text;
    }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

}



