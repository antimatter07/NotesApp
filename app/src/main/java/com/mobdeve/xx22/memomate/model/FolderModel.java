package com.mobdeve.xx22.memomate.model;

import com.mobdeve.xx22.memomate.R;

public class FolderModel {
    private static final int DEFAULT_FOLDER_ID = -1;

    private int folderId;
    private String name;
    private int colorResId;

    private int noteCount;

    public FolderModel() {
        this.folderId = DEFAULT_FOLDER_ID;
        this.name = "Folder" + this.folderId;
        this.colorResId = R.color.folderDefault;
        this.noteCount = 0;
    }

    public FolderModel(int folderId) {
        this.folderId = folderId;
        this.name = "Folder" + this.folderId;
        this.colorResId = R.color.folderDefault;
        this.noteCount = 0;
    }

    public FolderModel(String name, int colorResId) {
        this.folderId = DEFAULT_FOLDER_ID;
        this.name = name;
        this.colorResId = colorResId;
        this.noteCount = 0;
    }

    public FolderModel(int folderId, String name, int colorResId) {
        this.folderId = folderId;
        this.name = name;
        this.colorResId = colorResId;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getColorResId() { return colorResId; }

    public void setColorResId(int colorResId) { this.colorResId = colorResId; }

    public int getFolderId() { return folderId; }

    public void setFolderId(int folderId) { this.folderId = folderId; }

    public int getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(int count) { this.noteCount = count; }
}
