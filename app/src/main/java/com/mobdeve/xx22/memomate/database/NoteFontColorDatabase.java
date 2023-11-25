package com.mobdeve.xx22.memomate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobdeve.xx22.memomate.model.FolderModel;

import java.util.ArrayList;

public class NoteFontColorDatabase {
    private NoteFontColorDatabaseHandler handler;
    
    public NoteFontColorDatabase(Context context) {
        this.handler = new NoteFontColorDatabaseHandler(context);
    }

    /**
     * Gets all font color that has the given note id
     */
    public ArrayList<int[]> getNoteFontColors (int noteID) {
        ArrayList<int[]> result = new ArrayList<>();

        SQLiteDatabase db = handler.getReadableDatabase();

        if (db != null) {
            Cursor c = db.query(
                    handler.FONT_COLORS_TABLE, null,
                    handler.NOTE_ID + " = ?",
                    new String[]{String.valueOf(noteID)},
                    null, null, null
            );

            while(c.moveToNext()){
                int color = c.getInt(c.getColumnIndexOrThrow(handler.FONT_COLOR));
                int startPos = c.getInt(c.getColumnIndexOrThrow(handler.START_POS));
                int endPos = c.getInt(c.getColumnIndexOrThrow(handler.END_POS));
                result.add(new int[]{color, startPos, endPos});
            }

            c.close();

        }

        db.close();

        return result;
    }

    /**
     * Adds a font color configuration to the db
     */
    public int addFontColor(int noteID, int[] fontColorConfig) {
        SQLiteDatabase db = handler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(handler.NOTE_ID, noteID);
        values.put(handler.FONT_COLOR, fontColorConfig[0]);
        values.put(handler.START_POS, fontColorConfig[1]);
        values.put(handler.END_POS, fontColorConfig[2]);

        int _id = (int) db.insert(handler.FONT_COLORS_TABLE, null, values);

        db.close();

        return _id;
    }

    /**
     * Deletes all font color instances that has the given note id
     */
    public void deleteNoteFontColors(int noteID) {
        SQLiteDatabase db = handler.getWritableDatabase();

        db.delete(
                handler.FONT_COLORS_TABLE,
                handler.NOTE_ID + " = ?",
                new String[]{String.valueOf(noteID)});
        db.close();

    }
}
