package com.mobdeve.xx22.memomate.model;



import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParentNoteModel {


    private String title;
    private int folderKey;
    private Boolean isLocked = false;

    private String dateCreated;
    private String dateModified;

    /**
     * Unique identifier for note in local db
     */
    private int noteID;

    /**
     * Type of note: drawing, checklist, text
     * Set accordingly in subclasses.
     */
    private String noteType;
    public static final int DEFAULT_NOTE_ID = -1;
    // if folderKey = -1, note doesn't belong to folder
    public static final int DEFAULT_FOLDER_KEY = -1;

    public ParentNoteModel(String title) {
        this.dateCreated = getCurrentDateTime();
        this.dateModified = getCurrentDateTime();
        this.title = title;
        this.folderKey = DEFAULT_FOLDER_KEY;
        this.noteID = DEFAULT_NOTE_ID;
    }


    public ParentNoteModel(String title, int folderKey) {
        this.dateCreated = getCurrentDateTime();
        this.dateModified = getCurrentDateTime();
        this.title = title;
        this.folderKey = folderKey;
        this.noteID = DEFAULT_NOTE_ID;

    }

    /**
     * Instantiates a Note object
     * @param noteID unique identifier for note in db
     * @param title title of note
     * @param folderKey folderkey of the folder where the note belongs to
     */
    public ParentNoteModel(int noteID, String title, int folderKey) {
        this.noteID = noteID;
        this.dateCreated = getCurrentDateTime();
        this.dateModified = getCurrentDateTime();
        this.title = title;
        this.folderKey = folderKey;

    }

    public void setNoteType(String type) {
        this.noteType = type;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;

    }

    public int getNoteID() {
        return this.noteID;
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
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }


    /**
     * Gets current date and time in proper format for sorting in SQLite
     * @return SimpleDateFormat when ParantNoteModel was instantiated.
     */
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

}
