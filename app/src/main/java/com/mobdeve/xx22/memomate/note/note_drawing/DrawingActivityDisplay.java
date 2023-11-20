package com.mobdeve.xx22.memomate.note.note_drawing;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
public class DrawingActivityDisplay extends View{
    public static ArrayList<Path> pathList = new ArrayList<>();
    public static ArrayList<Integer> colorList = new ArrayList<>();
    public ViewGroup.LayoutParams params;
    public static int current_brush = Color.BLACK;

    public DrawingActivityDisplay(Context context) {
        super(context);
    }

    public DrawingActivityDisplay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawingActivityDisplay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
