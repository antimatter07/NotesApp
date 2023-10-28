package com.mobdeve.xx22.villarica.matthew.notes;

import android.content.Context;

public class FolderModel {

    private final int key;
    private String name;
    private int colorResId;

    public FolderModel(int key) {
        this.key = key;
        this.name = "Folder" + key;
        this.colorResId = R.color.folderDefault;
    }

    public FolderModel(int key, String name, int colorResId) {
        this.key = key;
        this.name = name;
        this.colorResId = colorResId;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getColorResId() { return colorResId; }

    public void setColorResId(int colorResId) { this.colorResId = colorResId; }

    public int getKey() { return key; }
}
