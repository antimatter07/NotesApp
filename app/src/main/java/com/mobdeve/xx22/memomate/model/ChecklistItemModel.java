package com.mobdeve.xx22.memomate.model;

import android.os.Parcel;
import android.os.Parcelable;

//implement parcelable so that we can pass arraylist of ChecklistItemModel to an intent
public class ChecklistItemModel implements Parcelable {
    private boolean isChecked = false;
    private String text;

    public ChecklistItemModel(boolean isChecked, String text) {
        this.isChecked = isChecked;
        this.text = text;
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
    }
}
