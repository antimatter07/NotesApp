package com.mobdeve.xx22.villarica.matthew.notes;

public class ParentNoteModel {

    private String title;
    // if folderKey = 0, note doesn't belong to folder
    private int folderKey = 0;
    private Boolean isLocked = false;

    private final String dateCreated;
    private String dateModified;



    public ParentNoteModel(String title, String dateCreated) {
        this.dateCreated = dateCreated;
        this.dateModified = dateCreated;
        this.title = title;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFolderKey() {
        return folderKey;
    }

    public void setFolderKey(int folderKey) {
        this.folderKey = folderKey;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
}
