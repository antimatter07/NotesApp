package com.mobdeve.xx22.villarica.matthew.notes;

public class DrawingNoteModel extends ParentNoteModel {

    // placeholder default value for drawing drawable resource
    private int drawingResId = R.drawable.ic_launcher_foreground;

    public DrawingNoteModel(String title, String dateCreated) {
        super(title, dateCreated);
    }

    public int getDrawingResId() { return drawingResId; }

    public void setDrawingResId(int drawingResId) { this.drawingResId = drawingResId; }
}