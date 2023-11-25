package com.mobdeve.xx22.memomate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mobdeve.xx22.memomate.model.FolderModel;

import java.util.ArrayList;

public class NoteFontColorDatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NoteFontColorDatabase";
    private static final int DATABASE_VERSION = 1;
    public static final String FONT_COLORS_TABLE = "notefontcolors";

    /**
     * Attributes for TABLE_FOLDERS
     */
    public static final String NOTE_ID = "noteId";
    public static final String FONT_COLOR = "fontColorId";
    public static final String START_POS = "startPos";
    public static final String END_POS = "endPos";

    // SQL statement for creating the table
    private static final String CREATE_FONT_COLOR_TABLE =
            "CREATE TABLE IF NOT EXISTS " + FONT_COLORS_TABLE + "(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTE_ID + " INTEGER, " +
                    FONT_COLOR + " INTEGER, " +
                    START_POS +  " INTEGER, " +
                    END_POS +  " INTEGER) ";

    public NoteFontColorDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FONT_COLOR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FONT_COLORS_TABLE);
        onCreate(db);
    }
}
