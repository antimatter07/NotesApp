package com.mobdeve.xx22.memomate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mobdeve.xx22.memomate.model.CheckListNoteModel;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;
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
     * Given a note id, returns a cursor to go through every checklistitem given a specific note id.
     * @param noteId noteId used to filter out notes irrelevant to checklist note
     * @return Cursor for iterating through relevant checklistitems
     */
    public Cursor getChecklistItemsByNoteId(int noteId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                "ID",
                NoteDatabaseHandler.COLUMN_NOTE_ID,
                NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_TEXT,
                NoteDatabaseHandler.COLUMN_IS_CHECKED
        };

        // Define the selection criteria
        String selection = NoteDatabaseHandler.COLUMN_NOTE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(noteId) };

        // Query the database
        Cursor cursor = db.query(
                NoteDatabaseHandler.TABLE_CHECKLIST_ITEMS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Optionally, you can check if the cursor is not null and moveToFirst
        // to position the cursor to the first row.

        return cursor;
    }

    /**
     * Gets all notes in db to display in main activity
     * @return ArrayList<ParentNoteModel> of all notes in db
     */
    public ArrayList<ParentNoteModel> getAllNotes() {
        ArrayList<ParentNoteModel> notes = new ArrayList<>();

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor c = db.query(dbHandler.TABLE_NOTES, null, null, null, null, null, null);


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


                ArrayList<ChecklistItemModel> items = new ArrayList<>();

                //query checklist item data, only those with current id should be considered
                Cursor cursorCheckItem = getChecklistItemsByNoteId(id);

                //go through of every checklist item needed for this note

                if (cursorCheckItem != null && cursorCheckItem.moveToFirst()) {
                    do {
                        // Retrieve data from the cursor
                        int itemId = cursorCheckItem.getInt(cursorCheckItem.getColumnIndexOrThrow("ID"));
                        //int noteId = cursorCheckItem.getLong(cursorCheckItem.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_NOTE_ID));
                        String checklistItemText = cursorCheckItem.getString(cursorCheckItem.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_TEXT));

                        int isCheckedInt = cursorCheckItem.getInt(cursorCheckItem.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_IS_CHECKED));

                        boolean isChecked;
                        if(isCheckedInt == 1)
                            isChecked = true;
                        else
                            isChecked = false;


                        items.add(new ChecklistItemModel(itemId, id, isChecked, checklistItemText));



                    } while (cursorCheckItem.moveToNext());

                    cursorCheckItem.close(); // Close the cursor
                }


                CheckListNoteModel checkNote = new CheckListNoteModel(title, folderKey, items);

                ParentNoteModel note = (ParentNoteModel) checkNote;

                note.setNoteID(id);
                note.setLocked(isLocked);
                note.setDateCreated(dateCreated);
                note.setDateModified(dateModified);

                notes.add(note);




            } else if (noteType.equals("drawing")) {
                //TODO: Add logic for drawing notes

            }



        }

        return notes;
    }

    /**
     * Add a new note into the db after user clicks on new text note
     * @param note Note object with values to insert into db
     * @return int of row id inserted
     */
    public synchronized int addTextNote(TextNoteModel note) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHandler.COLUMN_TITLE, note.getTitle());
        values.put(NoteDatabaseHandler.COLUMN_FOLDER_KEY, note.getFolderKey());
        values.put(NoteDatabaseHandler.COLUMN_DATE_CREATED, note.getDateCreated());
        values.put(NoteDatabaseHandler.COLUMN_DATE_MODIFIED, note.getDateModified());

        boolean isLockedBoolean = note.getLocked();

        if(isLockedBoolean)
            values.put(NoteDatabaseHandler.COLUMN_IS_LOCKED, 1);
        else
            values.put(NoteDatabaseHandler.COLUMN_IS_LOCKED, 0);

        values.put(NoteDatabaseHandler.COLUMN_NOTE_TEXT, note.getText());
        values.put(NoteDatabaseHandler.COLUMN_NOTE_TYPE, note.getNoteType());

        Log.d("ADDING NEW NOTE!", "Entering (Text)" +note.getTitle() + " INTO DB");
        int row = (int) db.insert(NoteDatabaseHandler.TABLE_NOTES, null, values);

        return row;
    }
}
