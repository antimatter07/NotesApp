package com.mobdeve.xx22.memomate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.model.BrushStrokeModel;
import com.mobdeve.xx22.memomate.model.CheckListNoteModel;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;
import com.mobdeve.xx22.memomate.model.DrawingNoteModel;
import com.mobdeve.xx22.memomate.model.ParentNoteModel;
import com.mobdeve.xx22.memomate.model.TextNoteModel;

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.logging.Handler;

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

        // Define the columns to retrieve
        String[] projection = {
                "ID",
                NoteDatabaseHandler.COLUMN_NOTE_ID,
                NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_TEXT,
                NoteDatabaseHandler.COLUMN_IS_CHECKED,
                NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_SIZE,
                NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_COLOR
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
    public ArrayList<ParentNoteModel> getAllNotes(int folderId) {
        ArrayList<ParentNoteModel> notes = new ArrayList<>();

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor c = db.query(dbHandler.TABLE_NOTES, null,
                    NoteDatabaseHandler.COLUMN_FOLDER_KEY + " = ?",
                            new String[]{String.valueOf(folderId)},
                    null, null, null);


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
                textNote.setFontColor(c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_NOTE_COLOR)));
                textNote.setFontSize(c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_NOTE_SIZE)));

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

                        int itemSize = cursorCheckItem.getInt(cursorCheckItem.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_SIZE));
                        int itemColor = cursorCheckItem.getInt(cursorCheckItem.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_COLOR));

                        Log.d("IN NOTEDATABASE", "item id of check item model: " + itemId);
                        ChecklistItemModel item = new ChecklistItemModel(itemId, id, isChecked, checklistItemText);
                        item.setItemSize(itemSize);
                        item.setItemColor(itemColor);
                        items.add(item);


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
     * Gets the note title of the given id
     * @param noteID id of  note
     */
    public synchronized String getNoteTitle(int noteID) {
        String noteTitle = "";
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        String query = "SELECT " + NoteDatabaseHandler.COLUMN_TITLE + " FROM " +
                        NoteDatabaseHandler.TABLE_NOTES + " WHERE " +
                        NoteDatabaseHandler.COLUMN_ID + " = ?";
        Cursor c = db.rawQuery(query, new String[] {String.valueOf(noteID)});

        if (c != null && c.moveToFirst()) {
            noteTitle = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_TITLE));
            c.close();
        }
        db.close();
        return noteTitle;
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
        values.put(NoteDatabaseHandler.COLUMN_NOTE_SIZE, note.getFontSize());
        values.put(NoteDatabaseHandler.COLUMN_NOTE_COLOR, note.getFontColor());

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
            checklistValues.put(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_SIZE, item.getItemSize());
            checklistValues.put(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_COLOR, item.getItemColor());
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
     * Updates folder of note.
     * @param currentNoteID ID of note to change title
     * @param folderKey ID of new folder
     */
    public synchronized void updateNoteFolder(int currentNoteID, int folderKey) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHandler.COLUMN_FOLDER_KEY, folderKey);

        String selection = NoteDatabaseHandler.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(currentNoteID)};

        db.update(NoteDatabaseHandler.TABLE_NOTES, values, selection, selectionArgs);

        db.close();

    }

    /**
     * Updates font color note.
     * @param currentNoteID ID of note to change title
     * @param fontColor resID of font color
     */
    public synchronized void updateTextNoteColor(int currentNoteID, int fontColor) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHandler.COLUMN_NOTE_COLOR, fontColor);

        String selection = NoteDatabaseHandler.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(currentNoteID)};

        db.update(NoteDatabaseHandler.TABLE_NOTES, values, selection, selectionArgs);

        db.close();

    }

    /**
     * Updates font size of the note.
     * @param currentNoteID ID of note to change title
     * @param fontSize __sp of font size
     */
    public synchronized void updateTextNoteSize(int currentNoteID, int fontSize) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHandler.COLUMN_NOTE_SIZE, fontSize);

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

    /**
     * Delete the notes that has the given folderID
     * @param folderID folder to delete
     */
    public synchronized void deleteNotesInFolder(int folderID) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        String selection = NoteDatabaseHandler.COLUMN_FOLDER_KEY + " = ?";
        String[] selectionArgs = {String.valueOf(folderID)};

        db.delete(NoteDatabaseHandler.TABLE_NOTES, selection, selectionArgs);

        db.close();

    }

    /**
     * Updates content of an item in a checklist
     * @param item_id id of checklist item
     * @param updatedText new text
     */
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

    /**
     * Updates font size of the checklist item
     * @param item_id id of checklist item
     * @param fontSize new font size
     */
    public synchronized void updateChecklistItemSize(int item_id, int fontSize) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_SIZE, fontSize);

        String selection = "ID = ?";
        String[] selectionArgs = {String.valueOf(item_id)};

        db.update(NoteDatabaseHandler.TABLE_CHECKLIST_ITEMS, values, selection, selectionArgs);

        db.close();


    }

    /**
     * Updates font color of the checklist item
     * @param item_id id of checklist item
     * @param fontColor new font size
     */
    public synchronized void updateChecklistItemColor(int item_id, int fontColor) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_COLOR, fontColor);

        String selection = "ID = ?";
        String[] selectionArgs = {String.valueOf(item_id)};

        db.update(NoteDatabaseHandler.TABLE_CHECKLIST_ITEMS, values, selection, selectionArgs);

        db.close();


    }

    /**
     * Add a new checklist item when user presses add button in a checklist activity
     * @param currentNoteID current checklist note of user
     * @return int of added checklist item
     */
    public synchronized int addCheckListItem(int currentNoteID) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues checklistValues = new ContentValues();

        checklistValues.put(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_TEXT, "");
        checklistValues.put(NoteDatabaseHandler.COLUMN_NOTE_ID, currentNoteID);
        checklistValues.put(NoteDatabaseHandler.COLUMN_IS_CHECKED, false);
        checklistValues.put(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_SIZE, 18);
        checklistValues.put(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_COLOR, R.color.blackDefault);

        Log.d("CHECKLIST ITEM MODEL", "BEING INSERTED INTO DB AT NOTE:" + currentNoteID);
        int row = (int) db.insert(NoteDatabaseHandler.TABLE_CHECKLIST_ITEMS, null, checklistValues);
        Log.d("CHECKLIST ITEM INSERTED AT: ", String.valueOf(row));



        db.close();

        return row;

    }

    /**
     * Updates a checklist item box's boolean value (check or not checked)
     * @param currentItemID id of check list item
     * @param isChecked updated boolean
     */
    public synchronized void updateChecklistItemChecked(int currentItemID, boolean isChecked) {

        int isCheckedInt;

        if(isChecked)
            isCheckedInt = 1;
        else
            isCheckedInt = 0;

        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NoteDatabaseHandler.COLUMN_IS_CHECKED, isCheckedInt);
        //values.put(NoteDatabaseHandler.COLUMN_DATE_MODIFIED, updatedTime);

        String selection = "ID = ?";
        String[] selectionArgs = {String.valueOf(currentItemID)};
        Log.d("IN UPDATE CHECK ITEM", "item id: " + String.valueOf(currentItemID) + "new chech boolean: " + isChecked);

        db.update(NoteDatabaseHandler.TABLE_CHECKLIST_ITEMS, values, selection, selectionArgs);

        db.close();


    }

    /**
     * Deletes checklist item of indicated id.
     * @param currentItemID
     */
    public synchronized void removeChecklistItem(int currentItemID) {

        SQLiteDatabase db = dbHandler.getWritableDatabase();



        String selection = "ID = ?";
        String[] selectionArgs = {String.valueOf(currentItemID)};

        db.delete(NoteDatabaseHandler.TABLE_CHECKLIST_ITEMS, selection, selectionArgs);

        db.close();


    }


    /**
     * Looks for notes with folder key filter.
     * @param query String to query for title
     * @param folderId folder used as a filter
     * @return list of notes (ParentNoteModel) to display in search
     */
    public ArrayList<ParentNoteModel> searchNotesWithFolderKey(String query, int folderId) {
        ArrayList<ParentNoteModel> notes = new ArrayList<>();

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        String selection = NoteDatabaseHandler.COLUMN_FOLDER_KEY + " = ? AND " +
                NoteDatabaseHandler.COLUMN_TITLE + " LIKE ?";

        String[] selectionArgs = {String.valueOf(folderId), "%" + query + "%"};

        Cursor c = db.query(
                NoteDatabaseHandler.TABLE_NOTES,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (c.moveToNext()) {

            int id = c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_ID));
            String title = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_TITLE));
            int folderKey = c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_FOLDER_KEY));
            int isLockedInt = c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_IS_LOCKED));


            boolean isLocked;
            if (isLockedInt == 1)
                isLocked = true;
            else
                isLocked = false;

            String dateCreated = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_DATE_CREATED));
            String dateModified = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_DATE_MODIFIED));

            String noteType = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_NOTE_TYPE));

            //create note object based on type of note
            if (noteType.equals("text")) {

                String noteText = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_NOTE_TEXT));

                TextNoteModel textNote = new TextNoteModel(title, folderKey, noteText);
                textNote.setFontColor(c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_NOTE_COLOR)));
                textNote.setFontSize(c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_NOTE_SIZE)));

                ParentNoteModel note = (ParentNoteModel) textNote;

                note.setNoteID(id);
                note.setLocked(isLocked);
                note.setDateCreated(dateCreated);
                note.setDateModified(dateModified);


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
                        if (isCheckedInt == 1)
                            isChecked = true;
                        else
                            isChecked = false;

                        int itemSize = cursorCheckItem.getInt(cursorCheckItem.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_SIZE));
                        int itemColor = cursorCheckItem.getInt(cursorCheckItem.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_COLOR));

                        Log.d("IN NOTEDATABASE", "item id of check item model: " + itemId);
                        ChecklistItemModel item = new ChecklistItemModel(itemId, id, isChecked, checklistItemText);
                        item.setItemSize(itemSize);
                        item.setItemColor(itemColor);
                        items.add(item);

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
        // Close the cursor and database
        c.close();
        db.close();

        return notes;
    }

    /**
     * Retrieve all notes from all folders that match search query for search activity
     * @param query query in search bar
     * @return ParentNote model arraylist used to update adapter
     */
    public ArrayList<ParentNoteModel> searchNotes(String query) {
        ArrayList<ParentNoteModel> notes = new ArrayList<>();

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        String selection = NoteDatabaseHandler.COLUMN_TITLE + " LIKE ?";

        String[] selectionArgs = {"%" + query + "%"};

        Cursor c = db.query(
                NoteDatabaseHandler.TABLE_NOTES,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (c.moveToNext()) {

            int id = c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_ID));
            String title = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_TITLE));
            int folderKey = c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_FOLDER_KEY));
            int isLockedInt = c.getInt(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_IS_LOCKED));


            boolean isLocked;
            if (isLockedInt == 1)
                isLocked = true;
            else
                isLocked = false;

            String dateCreated = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_DATE_CREATED));
            String dateModified = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_DATE_MODIFIED));

            String noteType = c.getString(c.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_NOTE_TYPE));

            //create note object based on type of note
            if (noteType.equals("text")) {
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
                        if (isCheckedInt == 1)
                            isChecked = true;
                        else
                            isChecked = false;

                        int itemSize = cursorCheckItem.getInt(cursorCheckItem.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_SIZE));
                        int itemColor = cursorCheckItem.getInt(cursorCheckItem.getColumnIndexOrThrow(NoteDatabaseHandler.COLUMN_CHECKLIST_ITEM_COLOR));

                        Log.d("IN NOTEDATABASE", "item id of check item model: " + itemId);
                        ChecklistItemModel item = new ChecklistItemModel(itemId, id, isChecked, checklistItemText);
                        item.setItemSize(itemSize);
                        item.setItemColor(itemColor);
                        items.add(item);


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

                DrawingNoteModel textNote = new DrawingNoteModel(title, folderKey);

                ParentNoteModel note = (ParentNoteModel) textNote;

                note.setNoteID(id);
                note.setLocked(isLocked);
                note.setDateCreated(dateCreated);
                note.setDateModified(dateModified);

                notes.add(note);

            }


        }
        // Close the cursor and database
        c.close();
        db.close();

        return notes;
    }





}
