package com.mobdeve.xx22.memomate.model;

import java.util.ArrayList;
public class DrawingNoteModel extends ParentNoteModel {

    private ArrayList<BrushStrokeModel> paths;

    public DrawingNoteModel(String title){
        super(title);
    }

    public DrawingNoteModel (String title, int folderKey) {
        super(title, folderKey);
    }

    public ArrayList<BrushStrokeModel> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<BrushStrokeModel> paths) {
        this.paths = paths;
    }
}




