package com.mobdeve.xx22.memomate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Path;
import android.util.Log;

import com.mobdeve.xx22.memomate.model.BrushStrokeModel;
import com.mobdeve.xx22.memomate.model.CheckListNoteModel;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;
import com.mobdeve.xx22.memomate.model.ParentNoteModel;
import com.mobdeve.xx22.memomate.model.TextNoteModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NoteDatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotesDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table names and column names for the notes table
    public static final String TABLE_NOTES = "notes";

    public static final String TABLE_CHECKLIST_ITEMS = "checklist_items";

    public static final String TABLE_DRAWING = "drawing";

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
    public static final String COLUMN_NOTE_COLOR = "note_color"; // font color for text notes
    public static final String COLUMN_NOTE_SIZE = "note_size"; // font size for text notes

    /**
     * Attributes for TABLE_CHECKLIST_ITEMS
     */

    public static final String COLUMN_NOTE_ID = "note_id";
    public static final String COLUMN_IS_CHECKED = "is_checked";

    public static final String COLUMN_CHECKLIST_ITEM_TEXT = "checklist_item_text";
    public static final String COLUMN_CHECKLIST_ITEM_COLOR = "checklist_item_color";
    public static final String COLUMN_CHECKLIST_ITEM_SIZE = "checklist_item_size";

    /**
     * Attributes for TABLE_BRUSHSTROKES
     */
    public static final int COLUMN_PEN_COLOR = Color.BLACK;
    public static final int COLUMN_PEN_WIDTH = 18;
    public static final Path COLUMN_PATH = new Path();

    /**
     * Attributes for TABLE_DRAWING
     */
    public static final ArrayList<BrushStrokeModel> COLUMN_PATHS =  new ArrayList<>();



    // SQL statement to create the notes table
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_FOLDER_KEY + " INTEGER,"
            + COLUMN_IS_LOCKED + " INTEGER,"
            + COLUMN_DATE_CREATED + " TEXT,"
            + COLUMN_DATE_MODIFIED + " TEXT,"
            + COLUMN_NOTE_TYPE + " TEXT, "
            + COLUMN_NOTE_TEXT + " TEXT,"
            + COLUMN_NOTE_COLOR + " INTEGER,"
            + COLUMN_NOTE_SIZE + " INTEGER)";


    private static final String CREATE_TABLE_CHECKLIST_ITEMS = "CREATE TABLE IF NOT EXISTS " + TABLE_CHECKLIST_ITEMS + "("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOTE_ID + " INTEGER, "
            + COLUMN_CHECKLIST_ITEM_TEXT + " TEXT,"
            + COLUMN_IS_CHECKED + " INTEGER,"
            + COLUMN_CHECKLIST_ITEM_SIZE + " INTEGER,"
            + COLUMN_CHECKLIST_ITEM_COLOR + " INTEGER,"
            + "FOREIGN KEY (" + COLUMN_NOTE_ID + ") REFERENCES " + TABLE_NOTES + "(" + COLUMN_ID + "))";




    public NoteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_CHECKLIST_ITEMS);
        ArrayList<ParentNoteModel> noteData = new ArrayList<>();

        //insert dummy data into db

        noteData.addAll(NoteDataHelper.loadTextNote());
        noteData.addAll(NoteDataHelper.loadCheckListNote());

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
                values.put(COLUMN_NOTE_COLOR, textNote.getFontColor());
                values.put(COLUMN_NOTE_SIZE, textNote.getFontSize());

                Log.d("DUMMY DATA", "Entering (Text)" +note.getTitle() + " INTO DB");
                db.insert(TABLE_NOTES, null, values);

            } else if (note.getNoteType().equals("checklist") && note instanceof CheckListNoteModel) {

                CheckListNoteModel checkListNote = (CheckListNoteModel) note;


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

                //values.put(COLUMN_NOTE_TEXT, textNote.getText());
                values.put(COLUMN_NOTE_TYPE, note.getNoteType());

                Log.d("DUMMY DATA", "Entering (Checklist) " +note.getTitle() + " INTO DB");

                int row_id = (int) db.insert(TABLE_NOTES, null, values);

                ArrayList<ChecklistItemModel> checklistItems = checkListNote.getCheckItemData();

                //for every checklist item, insert into db with associated note id
                for (ChecklistItemModel item: checklistItems) {
                    ContentValues checklistValues = new ContentValues();

                    checklistValues.put(COLUMN_CHECKLIST_ITEM_TEXT, item.getText());
                    checklistValues.put(COLUMN_NOTE_ID, row_id);
                    checklistValues.put(COLUMN_IS_CHECKED, item.getIsChecked());
                    checklistValues.put(COLUMN_CHECKLIST_ITEM_SIZE, item.getItemSize());
                    checklistValues.put(COLUMN_CHECKLIST_ITEM_COLOR, item.getItemColor());


                    Log.d("CHECKLIST ITEM MODEL", "BEING INSERTED INTO DB:" + item.getText());
                    int row = (int) db.insert(TABLE_CHECKLIST_ITEMS, null, checklistValues);
                    Log.d("CHECKLIST ITEM INSERTED AT: ", String.valueOf(row));

                }

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



}