package com.mobdeve.xx22.memomate.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobdeve.xx22.memomate.model.ParentNoteModel;
import com.mobdeve.xx22.memomate.model.TextNoteModel;

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;

public class NoteDatabase {

    private NoteDatabaseHandler dbHandler;

    /**
     * Initialize handler using context
     * @param context context provided usually main activity
     */
    public NoteDatabase(Context context) {
        this.dbHandler = new NoteDatabaseHandler(context);
    }

    /**
     * Gets all notes in db to display in main activity
     * @return ArrayList<ParentNoteModel> of all notes in db
     */
    public ArrayList<ParentNoteModel> getAllNotes() {
        ArrayList<ParentNoteModel> notes = new ArrayList<>();

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor c = db.query(dbHandler.TABLE_NOTES, null, null, null, null, null, null);

        /*
          private static final String CREATE_TABLE_NOTES = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_FOLDER_KEY + " INTEGER,"
            + COLUMN_IS_LOCKED + " INTEGER,"
            + COLUMN_DATE_CREATED + " TEXT,"
            + COLUMN_DATE_MODIFIED + " TEXT,"
            + COLUMN_NOTE_TYPE + " TEXT, "
            + COLUMN_NOTE_TEXT + " TEXT)";
         */
        while(c.moveToNext()) {

            int id = c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_ID));
            String title = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_TITLE));
            int folderKey = c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_FOLDER_KEY));
            int isLockedInt = c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_IS_LOCKED));

            boolean isLocked;
            if(isLockedInt == 1)
                isLocked = true;
            else
                isLocked = false;

            String dateCreated = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_DATE_CREATED));
            String dateModified = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_DATE_MODIFIED));

            String noteType = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_NOTE_TYPE));

            //create note object based on type of note
            if(noteType.equals("text")) {
                String noteText = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_NOTE_TEXT));

                TextNoteModel textNote = new TextNoteModel(title, folderKey, noteText);

                ParentNoteModel note = (ParentNoteModel) textNote;

                note.setNoteID(id);
                note.setLocked(isLocked);
                note.setDateCreated(dateCreated);
                note.setDateModified(dateModified);

                notes.add(note);

            } else if (noteType.equals("checklist")) {
                //TODO: Add logic for checklist notes

            } else if (noteType.equals("drawing")) {
                //TODO: Add logic for drawing notes

            }



        }

        return notes;
    }
}
