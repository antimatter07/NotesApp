package com.mobdeve.xx22.memomate.model;

public class TextNoteModel extends ParentNoteModel {
    private String text = "";

    public TextNoteModel (String title, String text) {
        super(title, -1);
        this.text = text;
        super.setNoteType("text");
    }
    public TextNoteModel (String title, int folderKey, String text) {
        super(title, folderKey);
        this.text = text;
        super.setNoteType("text");
    }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

}



