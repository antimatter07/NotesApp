package com.mobdeve.xx22.memomate.model;

import android.graphics.Path;
import java.util.ArrayList;
import java.util.List;
public class DrawingNoteModel extends ParentNoteModel {

    // placeholder default value for drawing drawable resource
    private List<Path> paths;

    public DrawingNoteModel(){
        super("test");
        paths = new ArrayList<>();
    }
    public DrawingNoteModel(String title) {
        super(title);
        paths = new ArrayList<>();
        this.setNoteType("drawing");
    }

    public DrawingNoteModel(String title, ArrayList paths){
        super(title);
        this.paths = paths;
        this.setNoteType("drawing");
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