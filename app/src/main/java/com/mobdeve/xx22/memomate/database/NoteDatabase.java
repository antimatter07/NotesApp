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

                        Log.d("IN NOTEDATABASE", "item id of check item model: " + itemId);
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
        db.close();

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
        db.close();

        return row;
    }

    /**
     *Add checklistNote item to db
     * @param note note to add to db
     * @return row where note was inserted
     */
    public synchronized int addCheckListNote(CheckListNoteModel note) {

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

        values.put(NoteDatabaseHandler.COLUMN_NOTE_TYPE, note.getNoteType());

        Log.d("ADDING NEW NOTE!", "Entering (Checklist)" +note.getTitle() + " INTO DB");
        int row_id = (int) db.insert(NoteDatabaseHandler.TABLE_NOTES, null, values);

        ArrayList<ChecklistItemModel> checklistItems = note.getCheckItemData();

        //for every checklist item, insert into db with associated note id
        for (ChecklistItemModel item: checklistItems) {
            ContentValues checklistValues = new ContentValues();

            checklistValues.put(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_TEXT, item.getText());
            checklistValues.put(NoteDatabaseHandler.COLUMN_NOTE_ID, row_id);
            checklistValues.put(NoteDatabaseHandler.COLUMN_IS_CHECKED, item.getIsChecked());
            Log.d("CHECKLIST ITEM MODEL", "BEING INSERTED INTO DB:" + item.getText());
            int row = (int) db.insert(NoteDatabaseHandler.TABLE_CHECKLIST_ITEMS, null, checklistValues);
            Log.d("CHECKLIST ITEM INSERTED AT: ", String.valueOf(row));

        }

        db.close();

        return row_id;

    }

    /**
     * Updates a text note with new text content
     * @param currentNoteID id of edited note
     * @param updatedText new text for text note
     * @param updatedTime new date modified
     */
    public synchronized void updateTextNoteContent(int currentNoteID, String updatedText, String updatedTime) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHandler.COLUMN_NOTE_TEXT, updatedText);
        values.put(NoteDatabaseHandler.COLUMN_DATE_MODIFIED, updatedTime);

        String selection = NoteDatabaseHandler.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(currentNoteID)};

        db.update(NoteDatabaseHandler.TABLE_NOTES, values, selection, selectionArgs);

        db.close();


    }


    /**
     * Updates title of note.
     * @param currentNoteID ID of note to change title
     * @param updatedTitle new title of note
     * @param updatedTime new date modified
     */
    public synchronized void updateNoteTitle(int currentNoteID, String updatedTitle, String updatedTime) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHandler.COLUMN_TITLE, updatedTitle);
        values.put(NoteDatabaseHandler.COLUMN_DATE_MODIFIED, updatedTime);

        String selection = NoteDatabaseHandler.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(currentNoteID)};

        db.update(NoteDatabaseHandler.TABLE_NOTES, values, selection, selectionArgs);

        db.close();


    }

    /**
     * Delete a note given its ID in the db
     * @param noteID note to delete
     */
    public synchronized void deleteNote(int noteID) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();



        String selection = NoteDatabaseHandler.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(noteID)};

        db.delete(NoteDatabaseHandler.TABLE_NOTES, selection, selectionArgs);

        db.close();


    }

    public synchronized void updateChecklistItemText(int item_id, String updatedText) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_TEXT, updatedText);
        //values.put(NoteDatabaseHandler.COLUMN_DATE_MODIFIED, updatedTime);

        String selection = "ID = ?";
        String[] selectionArgs = {String.valueOf(item_id)};
        Log.d("IN UPDATE CHECK ITEM", "item id: " + String.valueOf(item_id) + "new text: " + updatedText);

        db.update(NoteDatabaseHandler.TABLE_CHECKLIST_ITEMS, values, selection, selectionArgs);

        db.close();


    }

}
