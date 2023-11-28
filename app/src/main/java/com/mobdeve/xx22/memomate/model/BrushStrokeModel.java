package com.mobdeve.xx22.memomate.model;

import android.graphics.Path;

public class BrushStrokeModel {

    public int penColor;
    public int penWidth;
    public Path path;

    public BrushStrokeModel(int penColor, int penWidth, Path path){
    this.penColor = penColor;
    this.penWidth = penWidth;
    this.path = path;
    }

}
