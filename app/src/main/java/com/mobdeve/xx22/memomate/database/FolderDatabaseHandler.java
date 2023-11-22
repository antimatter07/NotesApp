package com.mobdeve.xx22.memomate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobdeve.xx22.memomate.model.FolderModel;

import java.util.ArrayList;

public class FolderDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FoldersDatabase";
    public static final String FOLDERS_TABLE = "folders";

    /**
     * Attributes for TABLE_FOLDERS
     */
    public static final String FOLDER_ID = "folderId";
    public static final String FOLDER_NAME = "name";
    public static final String FOLDER_COLOR = "colorResId";
    public static final String FOLDER_NOTE_COUNT = "noteCount";

    // SQL statement for creating the folders table
    private static final String CREATE_FOLDERS_TABLE = 
            "CREATE TABLE IF NOT EXISTS " + FOLDERS_TABLE + "(" +
            FOLDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FOLDER_NAME + " TEXT, " +
            FOLDER_COLOR +  " INTEGER, " +
            FOLDER_NOTE_COUNT +  " INTEGER) ";

    public FolderDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FOLDERS_TABLE);

        // insert dummy data into db
        ArrayList<FolderModel> folderData = new ArrayList<>();
        folderData.addAll(FolderDataHelper.generateFolderData());

        for (FolderModel folder: folderData) {
            ContentValues values = new ContentValues();
            values.put(FOLDER_ID, folder.getFolderId());
            values.put(FOLDER_NAME, folder.getName());
            values.put(FOLDER_COLOR, folder.getColorResId());
            values.put(FOLDER_NOTE_COUNT, folder.getNoteCount());
            db.insert(FOLDERS_TABLE, null, values);
            values.clear();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FOLDERS_TABLE);
        onCreate(db);
    }
}
