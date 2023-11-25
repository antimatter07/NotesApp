package com.mobdeve.xx22.memomate.note.note_text;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.database.NoteFontColorDatabase;
import com.mobdeve.xx22.memomate.model.TextNoteModel;
import com.mobdeve.xx22.memomate.partials.ChangeFolderFragment;
import com.mobdeve.xx22.memomate.partials.NoteOptionsFragment;
import com.mobdeve.xx22.memomate.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class TextNoteActivity extends AppCompatActivity
                                implements PopupMenu.OnMenuItemClickListener {

     public static final String TEXT_KEY = "TEXT_KEY";
     public static final String TITLE_KEY = "TITLE_KEY";
     public static final String DATE_CREATED_KEY = "DATE_KEY";
     public static final String DATE_MODIFIED_KEY = "DATE_MODIFIED_KEY";

     private ConstraintLayout noteBar;
     private LinearLayout fontOptionsBar;
     private EditText noteTextView;
    private TextView noteTitleView;
    private Button fontSizeBtn;
    private ImageButton fontColorBtn;

    private PopupWindow popupWindow;
    private String noteContent = "";
    private String titleContent = "";
    private int noteColor;
    private int selectedFontColor;

    private boolean isNoteContentChanged = false;
    private boolean isTitleContentChanged = false;
    private int currentNoteID = -1;

    private String currentDateTime = getCurrentDateTime();

    /**
     * Thread for db operations
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    //note db
    private NoteDatabase noteDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textnote_activity);

        noteDatabase = new NoteDatabase(getApplicationContext());

        fontOptionsBar = findViewById(R.id.fontOptionsLl);

        noteTextView = findViewById(R.id.noteBodyText);
        noteTitleView = findViewById(R.id.noteTitleText);
        noteBar = findViewById(R.id.noteBarCl);
        fontSizeBtn = findViewById(R.id.fontSizeBtn);
        fontColorBtn = findViewById(R.id.fontColorBtn);

        noteColor = ContextCompat.getColor(this, getIntent().getIntExtra("noteColor", R.color.folderDefault));
        noteBar.setBackgroundColor(noteColor);

        // Initially set the TextView to display the note content
        noteTextView.setText(noteContent);
        noteTitleView.setText(titleContent);

        // Setup Font Style Popup Windows
        setUpPopupWindows();

        // Retrieve note content if available from Intent extras
        if (getIntent().hasExtra("noteContent")) {
            noteContent = getIntent().getStringExtra("noteContent");
            noteTextView.setText(noteContent);
        }

        // Retrieve note content if available from Intent extras
        if (getIntent().hasExtra("titleContent")) {
            titleContent = getIntent().getStringExtra("titleContent");
            noteTitleView.setText(titleContent);
        }

        currentNoteID = getIntent().getIntExtra("noteID", -1);
        int folderKey = getIntent().getIntExtra("folderKey", -1);

        selectedFontColor = ContextCompat.getColor(this, R.color.blackDefault);

        //if noteID retrieved is default value, create new text note in db
        if(currentNoteID == -1) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    NoteDatabase db = new NoteDatabase(getApplicationContext());
                    //make new text note and insert into db

                    TextNoteModel textNote = new TextNoteModel(getIntent().getStringExtra("titleContent"),
                            folderKey,
                            getIntent().getStringExtra("noteContent"));

                    currentNoteID = db.addTextNote(textNote);

                }
            });
        }
        // Setup the font styles for noteTextView
        else setupNoteFontStyle();




        // Setup Note Options Button
        ImageButton noteOptionsBtn = findViewById(R.id.textNoteOptionsBtn);
        noteOptionsBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            NoteOptionsFragment noteOptionsFragment = new NoteOptionsFragment();

            //set current noteID so that its visible inside fragment for deletion, locking, etc
            noteOptionsFragment.setNoteID(currentNoteID);
            noteOptionsFragment.show(fm, "NoteOptionsDialog");
        });

        noteTextView.addTextChangedListener(new TextWatcher() {
            int textLength = noteTextView.getText().toString().length();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                isNoteContentChanged = true;
                currentDateTime = getCurrentDateTime();

                if(isNoteContentChanged) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {

                            String updatedNoteContent = noteTextView.getText().toString();


                            if (isNoteContentChanged) {
                                // Update note content in the database
                                noteDatabase.updateTextNoteContent(currentNoteID, updatedNoteContent, currentDateTime);
                                isNoteContentChanged = false; // Reset flag
                            }
                        }
                    });

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // update character font color based on selected font color
                if (textLength < editable.length())
                    updateFontColor(editable);

                textLength = editable.length();
            }
        });

        noteTitleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                isTitleContentChanged = true;
                currentDateTime = getCurrentDateTime();

                if(isTitleContentChanged) {

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            // Get the updated content
                            String updatedTitle = noteTitleView.getText().toString();

                            // Update title in the database
                            noteDatabase.updateNoteTitle(currentNoteID, updatedTitle, currentDateTime);
                            isTitleContentChanged = false; // Reset flag


                        }
                    });

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Only show the note options when the keyboard is open
        // Source: https://stackoverflow.com/questions/4745988/how-do-i-detect-if-software-keyboard-is-visible-on-android-device-or-not
        fontOptionsBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                fontOptionsBar.getWindowVisibleDisplayFrame(r);
                int screenHeight = fontOptionsBar.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > (screenHeight * 0.15))
                    fontOptionsBar.setVisibility(View.VISIBLE);
                else  {
                    popupWindow.dismiss();
                    fontOptionsBar.setVisibility(View.GONE);
                }
            }
        });


        // Font Color
        fontColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFontColorPopup(v);
            }
        });

        fontSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                PopupMenu popup = new PopupMenu(TextNoteActivity.this, v);
                popup.setOnMenuItemClickListener(TextNoteActivity.this);
                popup.inflate(R.menu.popup_font_size);
                popup.show();
            }
        });

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();



        // Save the note content when the activity is paused
        /*if (isNoteContentChanged || isTitleContentChanged) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    NoteDatabase db = new NoteDatabase(getApplicationContext());

                    // Get the updated content
                    String updatedTitle = noteTitleView.getText().toString();
                    String updatedNoteContent = noteTextView.getText().toString();

                    // Update the database only if the content has changed
                    if (isTitleContentChanged) {
                        // Update title in the database
                        db.updateNoteTitle(currentNoteID, updatedTitle, currentDateTime);
                        isTitleContentChanged = false; // Reset flag
                    }

                    if (isNoteContentChanged) {
                        // Update note content in the database
                        db.updateTextNoteContent(currentNoteID, updatedNoteContent, currentDateTime);
                        isNoteContentChanged = false; // Reset flag
                    }
                }
            });
        }
        */

    }


    @Override
    protected void onResume() {
        super.onResume();
        // Update the TextView with the latest note content when the activity is resumed
        //noteTextView.setText(noteContent);
        //noteTitleView.setText(titleContent);

    }

    /**
     * Gets current date and time in proper format for sorting in SQLite
     * @return SimpleDateFormat when ParantNoteModel was instantiated.
     */
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * Changes the font color of the text based on the color id
     */
    private void setFontColor(int color, int startPos, int endPos) {
        if (startPos == endPos) {
            selectedFontColor = color;
            fontColorBtn.setColorFilter(color);
        }
        else {
            Editable editable = noteTextView.getText();
            editable.setSpan(new ForegroundColorSpan(color), startPos,
                    endPos, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            noteTextView.setSelection(endPos);
        }
    }

    /**
     * Changes the font color of the last added character in noteTextView
     */
    private void updateFontColor(Editable editable) {
        editable.setSpan(new ForegroundColorSpan(selectedFontColor), noteTextView.getSelectionEnd() - 1,
                noteTextView.getSelectionEnd(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        saveFontConfig();
    }

    private void setupNoteFontStyle() {
        NoteFontColorDatabase db = new NoteFontColorDatabase(getApplicationContext());

         ArrayList<int[]> fontConfigs = db.getNoteFontColors(currentNoteID);

         Log.d("JVC", "SIZE: " + fontConfigs.size());

         if (fontConfigs.size() > 0) {
             for(int i = 0; i < fontConfigs.size(); i++) {
                Log.d("JVC", "color " + fontConfigs.get(i)[0]);
                Log.d("JVC", "start " + fontConfigs.get(i)[1]);
                Log.d("JVC", "end " + fontConfigs.get(i)[2]);
                setFontColor(fontConfigs.get(i)[0], fontConfigs.get(i)[1], fontConfigs.get(i)[2]);

             }
        }

    }

    /**
     * Handles the font color settings menu
     * @param anchorView the button view it will be attached to
     */
    private void showFontColorPopup(View anchorView) {
        // find the location of the button
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int x = location[0] - 100;
        int y = location[1] - popupWindow.getHeight() - 325;
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);

    }

    private void setUpPopupWindows() {
        // Font Color
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_choose_font_color, null);

        popupWindow = new PopupWindow(
                popupView,
                450,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // setup buttons for popup
        Map<ImageButton, Integer> colorBtns = new HashMap<>();
        colorBtns.put(popupView.findViewById(R.id.colorDefaultBtn), R.color.blackDefault);
        colorBtns.put(popupView.findViewById(R.id.colorRedBtn), R.color.fontRed);
        colorBtns.put(popupView.findViewById(R.id.colorOrangeBtn), R.color.fontOrange);
        colorBtns.put(popupView.findViewById(R.id.colorYellowBtn), R.color.fontYellow);
        colorBtns.put(popupView.findViewById(R.id.colorGreenBtn), R.color.fontGreen);
        colorBtns.put(popupView.findViewById(R.id.colorCyanBtn), R.color.fontCyan);
        colorBtns.put(popupView.findViewById(R.id.colorBlueBtn), R.color.fontBlue);
        colorBtns.put(popupView.findViewById(R.id.colorPurpleBtn), R.color.fontPurple);

        // If a color is selected, remove the checks from the other colors
        // and set a check on this color
        for (Map.Entry<ImageButton, Integer> colorBtn : colorBtns.entrySet()) {
            ImageButton btn = colorBtn.getKey();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    int color = ContextCompat.getColor(TextNoteActivity.this, colorBtn.getValue());
                    setFontColor(color, noteTextView.getSelectionStart(), noteTextView.getSelectionEnd());

                    saveFontConfig();
                }
            });
        }

        popupWindow.setOutsideTouchable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

    }

    public void saveFontConfig() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // save font style config
                Spannable spannable = noteTextView.getText();
                ForegroundColorSpan[] colorSpans = spannable.getSpans(0, spannable.length(),
                        ForegroundColorSpan.class);
                NoteFontColorDatabase db = new NoteFontColorDatabase(getApplicationContext());
                Log.d("JVC", "before delete. " + db.getNoteFontColors(currentNoteID).size());
                db.getNoteFontColors(currentNoteID);
                db.deleteNoteFontColors(currentNoteID);
                Log.d("JVC", "Tried to delete. " + db.getNoteFontColors(currentNoteID).size());
                for (ForegroundColorSpan colorSpan : colorSpans) {
                    int[] fontColorConfig = {
                            colorSpan.getForegroundColor(),
                            spannable.getSpanStart(colorSpan),
                            spannable.getSpanEnd(colorSpan)
                    };

                    db.addFontColor(currentNoteID, fontColorConfig);
                }
            }
        });
    }
}

