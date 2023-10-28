package com.mobdeve.xx22.villarica.matthew.notes;

public class TextNoteModel extends ParentNoteModel {
    private String text = "";

    public TextNoteModel (String title, String dateCreated, String text) {
        super(title, dateCreated);
        this.text = text;
    }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }
}