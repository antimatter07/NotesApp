package com.mobdeve.xx22.memomate.model;

import android.graphics.Path;
import java.util.ArrayList;
import java.util.List;
public class DrawingNoteModel extends ParentNoteModel {

    // placeholder default value for drawing drawable resource
    private List<Path> paths;

    public DrawingNoteModel(){
        super("test", "10/28/23");
        paths = new ArrayList<>();
    }
    public DrawingNoteModel(String title, String dateCreated) {
        super(title, dateCreated);
        paths = new ArrayList<>();
    }

    public DrawingNoteModel(String title, String dateCreated, ArrayList paths){
        super(title, dateCreated);
        this.paths = paths;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public void addPath(Path path){
        paths.add(path);
    }

    public void clearPaths(){
        paths.clear();
    }
}