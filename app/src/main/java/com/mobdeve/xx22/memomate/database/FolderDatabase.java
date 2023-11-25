package com.mobdeve.xx22.memomate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.model.FolderModel;

import java.util.ArrayList;

public class FolderDatabase {

    private FolderDatabaseHandler folderHandler;

    // Initializes the handlers instance using the context provided.
    public FolderDatabase(Context context) {
        this.folderHandler = new FolderDatabaseHandler(context);
    }

    /**
     * Gets all folders in db to display in main activity
     * @return ArrayList<FolderModel> of all folders in db
     */
    public ArrayList<FolderModel> getAllFolders () {
        ArrayList<FolderModel> result = new ArrayList<FolderModel>();

        SQLiteDatabase fdb = folderHandler.getReadableDatabase();

        if (fdb != null) {
            Cursor fc = fdb.query(
                    folderHandler.FOLDERS_TABLE,
                    null, null, null, null, null,
                    folderHandler.FOLDER_ID + " ASC", null
            );

            while(fc.moveToNext()){
                FolderModel folder = new FolderModel(
                        fc.getInt(fc.getColumnIndexOrThrow(folderHandler.FOLDER_ID)),
                        fc.getString(fc.getColumnIndexOrThrow(folderHandler.FOLDER_NAME)),
                        fc.getInt(fc.getColumnIndexOrThrow(folderHandler.FOLDER_COLOR))
                );
                result.add(folder);
            }

            fc.close();

        }

        fdb.close();

        return result;
    }

    // Inserts a provided folder item into the database. Returns the id provided by the DB.
    public int addFolder(FolderModel folder) {
        SQLiteDatabase db = folderHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(folderHandler.FOLDER_ID, folder.getFolderId());
        values.put(folderHandler.FOLDER_NAME, folder.getName());
        values.put(folderHandler.FOLDER_COLOR, folder.getColorResId());
        values.put(folderHandler.FOLDER_NOTE_COUNT, folder.getNoteCount());

        int _id = (int) db.insert(folderHandler.FOLDERS_TABLE, null, values);

        db.close();

        return _id;
    }

    // updates a folder item with new content
    public void updateFolder(FolderModel folder) {
        SQLiteDatabase db = folderHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(folderHandler.FOLDER_ID, folder.getFolderId());
        values.put(folderHandler.FOLDER_NAME, folder.getName());
        values.put(folderHandler.FOLDER_COLOR, folder.getColorResId());
        values.put(folderHandler.FOLDER_NOTE_COUNT, folder.getNoteCount());

        db.update(folderHandler.FOLDERS_TABLE, values,
                folderHandler.FOLDER_ID + " = ?", new String[]{String.valueOf(folder.getFolderId())});

        db.close();

    }

    // updates a folder item's name
    public void updateFolderName(int folderId, String folderName) {
        SQLiteDatabase db = folderHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(folderHandler.FOLDER_NAME, folderName);

        db.update(folderHandler.FOLDERS_TABLE, values,
                folderHandler.FOLDER_ID + " = ?", new String[]{String.valueOf(folderId)});

        db.close();

    }

    // updates a folder item's color
    public void updateFolderColor(int folderId, int folderColor) {
        SQLiteDatabase db = folderHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(folderHandler.FOLDER_COLOR, folderColor);

        db.update(folderHandler.FOLDERS_TABLE, values,
                folderHandler.FOLDER_ID + " = ?", new String[]{String.valueOf(folderId)});

        db.close();

    }

    // deletes a folder item along with the notes within it
    public void deleteFolder(int folderID) {
        SQLiteDatabase db = folderHandler.getWritableDatabase();

        db.delete(
                folderHandler.FOLDERS_TABLE,
                folderHandler.FOLDER_ID + " = ?",
                new String[]{String.valueOf(folderID)});
        db.close();

    }

    public int getLastId() {
        int lastId = -1;

        SQLiteDatabase db = folderHandler.getReadableDatabase();
        String lastIdQuery = "SELECT MAX(" + FolderDatabaseHandler.FOLDER_ID + ") FROM " + FolderDatabaseHandler.FOLDERS_TABLE;
        Cursor c = db.rawQuery(lastIdQuery, null);

        if (c != null && c.moveToFirst()) {
            lastId = c.getInt(0);
            c.close();
        }

        return lastId;
    }

    public int getFolderColor(int folderId) {
        int folderColor = R.color.folderDefault;
        SQLiteDatabase db = folderHandler.getReadableDatabase();

        String query = "SELECT "+ FolderDatabaseHandler.FOLDER_COLOR + " FROM " +
                        FolderDatabaseHandler.FOLDERS_TABLE + " WHERE " +
                        FolderDatabaseHandler.FOLDER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(folderId)});

        if (cursor != null && cursor.moveToFirst()) {
            folderColor = cursor.getInt(cursor.getColumnIndexOrThrow(FolderDatabaseHandler.FOLDER_COLOR));
            cursor.close();
        }

        db.close();

        return folderColor;
    }


    public void printFolderDB(SQLiteDatabase db) {

        if (db != null) {
            Cursor c = db.query(
                    folderHandler.FOLDERS_TABLE,
                    null, null, null, null, null,
                    folderHandler.FOLDER_ID + " ASC", null
            );

            while(c.moveToNext()){
                Log.d("JVC", "Folder: " + c.getInt(c.getColumnIndexOrThrow(folderHandler.FOLDER_ID)) +
                        " " + c.getString(c.getColumnIndexOrThrow(folderHandler.FOLDER_NAME)) +
                        " " + c.getInt(c.getColumnIndexOrThrow(folderHandler.FOLDER_COLOR))
                );
            }

            c.close();
        }

    }

}
