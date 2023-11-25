package com.mobdeve.xx22.memomate.model;

import com.mobdeve.xx22.memomate.R;

public class TextNoteModel extends ParentNoteModel {
    private String text = "";
    private int fontColor;

    public TextNoteModel (String title, String text) {
        super(title);
        this.text = text;
        super.setNoteType("text");
    }
    public TextNoteModel (String title, int folderKey, String text) {
        super(title, folderKey);
        this.text = text;
        this.fontColor = R.color.blackDefault;
        super.setNoteType("text");
    }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }
}



