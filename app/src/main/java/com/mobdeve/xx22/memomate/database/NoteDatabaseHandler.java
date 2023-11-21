package com.mobdeve.xx22.memomate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mobdeve.xx22.memomate.model.CheckListNoteModel;
import com.mobdeve.xx22.memomate.model.ParentNoteModel;
import com.mobdeve.xx22.memomate.model.TextNoteModel;

import java.util.ArrayList;

public class NoteDatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotesDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table names and column names for the notes table
    public static final String TABLE_NOTES = "notes";

    public static final String TABLE_CHECKLIST_ITEMS = "checklist_items";

    /**
     * Attributes for TABLE_NOTES
     */
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_FOLDER_KEY = "folder_key";
    public static final String COLUMN_IS_LOCKED = "is_locked";
    public static final String COLUMN_DATE_CREATED = "date_created";
    public static final String COLUMN_DATE_MODIFIED = "date_modified";
    public static final String COLUMN_NOTE_TYPE = "note_type"; // Added to distinguish between Text and Checklist notes
    public static final String COLUMN_NOTE_TEXT = "note_text"; // For Text notes

    /**
     * Attributes for TABLE_CHECKLIST_ITEMS
     */

    private static final String COLUMN_NOTE_ID = "note_id";
    private static final String COLUMN_IS_CHECKED = "is_checked";

    private static final String COLUMN_CHECKLIST_ITEM_TEXT = "checklist_item_text";


    // SQL statement to create the notes table
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_FOLDER_KEY + " INTEGER,"
            + COLUMN_IS_LOCKED + " INTEGER,"
            + COLUMN_DATE_CREATED + " TEXT,"
            + COLUMN_DATE_MODIFIED + " TEXT,"
            + COLUMN_NOTE_TYPE + " TEXT, "
            + COLUMN_NOTE_TEXT + " TEXT)";

    private static final String CREATE_TABLE_CHECKLIST_ITEMS = "CREATE TABLE IF NOT EXISTS " + TABLE_CHECKLIST_ITEMS + "("
            + COLUMN_NOTE_ID + " INTEGER, "
            + COLUMN_CHECKLIST_ITEM_TEXT + " TEXT,"
            + COLUMN_IS_CHECKED + " INTEGER)";




    public NoteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_CHECKLIST_ITEMS);
        ArrayList<ParentNoteModel> noteData = new ArrayList<>();

        //insert dummy data into db
        //noteData.addAll(NoteDataHelper.loadCheckListNote());

        noteData.addAll(NoteDataHelper.loadTextNote());

        for (ParentNoteModel note: noteData) {

            if(note.getNoteType().equals("text") && note instanceof TextNoteModel) {
                TextNoteModel textNote = (TextNoteModel) note;


                ContentValues values = new ContentValues();
                values.put(COLUMN_TITLE, note.getTitle());
                values.put(COLUMN_FOLDER_KEY, note.getFolderKey());
                values.put(COLUMN_DATE_CREATED, note.getDateCreated());
                values.put(COLUMN_DATE_MODIFIED, note.getDateModified());

                boolean isLockedBoolean = note.getLocked();

                if(isLockedBoolean)
                    values.put(COLUMN_IS_LOCKED, 1);
                else
                    values.put(COLUMN_IS_LOCKED, 0);

                values.put(COLUMN_NOTE_TEXT, textNote.getText());
                values.put(COLUMN_NOTE_TYPE, textNote.getNoteType());

                Log.d("DUMMY DATA", "Entering " +note.getTitle() + " INTO DB");
                db.insert(TABLE_NOTES, null, values);

            }


        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it exists and create a new one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKLIST_ITEMS);
        onCreate(db);
    }

    // Method to add a note to the database

}