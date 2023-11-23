package com.mobdeve.xx22.memomate.model;

import android.os.Parcel;
import android.os.Parcelable;

//implement parcelable so that we can pass arraylist of ChecklistItemModel to an intent
public class ChecklistItemModel implements Parcelable {
    private boolean isChecked = false;
    private String text;

    /**
     * the note ID this checklist item belongs to.
     */
    private int noteId;

    /**
     * Unique identifier for the item itself
     */
    private int itemId;

    public ChecklistItemModel(int itemId, int noteId, boolean isChecked, String text) {
        this.isChecked = isChecked;
        this.text = text;
        this.noteId = noteId;
        this.itemId = itemId;
    }
    public ChecklistItemModel(int noteId, boolean isChecked, String text) {
        this.isChecked = isChecked;
        this.text = text;
        this.noteId = noteId;
    }

    public ChecklistItemModel(boolean isChecked, String text) {
        this.isChecked = isChecked;
        this.text = text;
        this.noteId = ParentNoteModel.DEFAULT_NOTE_ID;

    }

    public void setItemId(int itemId) { this.itemId = itemId;}
    public int getItemId()  { return this.itemId; }
    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getText() {
        return text;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setChecked(boolean b) {
        this.isChecked = b;
    }
    public int getNoteId() {
        return noteId;
    }
    // Parcelable implementation

    // Describe the kinds of special objects contained in this Parcelable instance
    @Override
    public int describeContents() {
        return 0;
    }

    // Write the object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isChecked ? 1 : 0)); // Write boolean as a byte
        dest.writeString(text);
        dest.writeInt(noteId);
        dest.writeInt(itemId);
    }



    // Creator constant for generating instances of your Parcelable class
    public static final Parcelable.Creator<ChecklistItemModel> CREATOR = new Parcelable.Creator<ChecklistItemModel>() {
        public ChecklistItemModel createFromParcel(Parcel in) {
            return new ChecklistItemModel(in);
        }

        public ChecklistItemModel[] newArray(int size) {
            return new ChecklistItemModel[size];
        }
    };

    // Constructor that takes a Parcel and unmarshals the object
    private ChecklistItemModel(Parcel in) {
        isChecked = in.readByte() != 0; // Read byte as a boolean
        text = in.readString();
        noteId = in.readInt();
        itemId = in.readInt();
    }
}
