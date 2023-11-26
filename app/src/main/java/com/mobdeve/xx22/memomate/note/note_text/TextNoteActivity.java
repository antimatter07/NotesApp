package com.mobdeve.xx22.memomate.note.note_text;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
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

import com.google.android.material.resources.TextAppearance;
import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.model.TextNoteModel;
import com.mobdeve.xx22.memomate.partials.NoteOptionsFragment;
import com.mobdeve.xx22.memomate.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class TextNoteActivity extends AppCompatActivity {

     public static final String TEXT_KEY = "TEXT_KEY";
     public static final String TITLE_KEY = "TITLE_KEY";
     public static final String DATE_CREATED_KEY = "DATE_KEY";
     public static final String DATE_MODIFIED_KEY = "DATE_MODIFIED_KEY";

     private ConstraintLayout noteBar;
     private LinearLayout fontOptionsBar;
     private EditText noteTextView;
    private TextView noteTitleView;
    private ImageButton fontSizeBtn;
    private ImageButton fontColorBtn;

    private PopupWindow fontColorPopup;
    private PopupWindow fontSizePopup;
    private String noteContent = "";
    private String titleContent = "";
    private int noteColor;
    private int noteSize;

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
        noteSize = getIntent().getIntExtra("noteFontSize", 18);

        // Initially set the TextView to display the note content
        noteTextView.setText(noteContent);
        noteTitleView.setText(titleContent);

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

        setFontColor(getIntent().getIntExtra("noteFontColor", R.color.blackDefault));
        setFontSize(noteSize);

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
                    if (fontColorPopup != null)
                        fontColorPopup.dismiss();
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
                showFontSizePopup(v);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

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
     * Changes the font size of the text based on the color id
     */
    private void setFontSize(int size) {
        noteTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * Changes the font color of the text based on the color id
     */
    private void setFontColor(int color) {
        int fontColor = ContextCompat.getColor(this, color);
        noteTextView.setTextColor(fontColor);
        fontColorBtn.setColorFilter(fontColor);
    }

    /**
     * Setups popups for font styles
     */
    private void setUpPopupWindows() {
        // Font Color
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_choose_font_color, null);

        fontColorPopup = new PopupWindow(
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

        // If a color is selected, close popup and set font color
        for (Map.Entry<ImageButton, Integer> colorBtn : colorBtns.entrySet()) {
            ImageButton btn = colorBtn.getKey();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fontColorPopup.dismiss();
                    setFontColor(colorBtn.getValue());

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            NoteDatabase db = new NoteDatabase(getApplicationContext());
                            db.updateTextNoteColor(currentNoteID, colorBtn.getValue());
                        }
                    });
                }
            });
        }

        fontColorPopup.setOutsideTouchable(true);
        fontColorPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);


        // Font Size
        View popupView2 = LayoutInflater.from(this).inflate(R.layout.popup_choose_font_size, null);

        fontSizePopup = new PopupWindow(
                popupView2,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // setup buttons for popup
        Button smallBtn = popupView2.findViewById(R.id.fontSmall);
        Button mediumBtn = popupView2.findViewById(R.id.fontMedium);
        Button largeBtn = popupView2.findViewById(R.id.fontLarge);

        // If a size is selected, close popup and set font size
        View.OnClickListener sizeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fontSizePopup.dismiss();
                String fontSize = ((Button) v).getText().toString();
                int sizeToNum = 0;
                switch(fontSize) {
                    case "Small":
                        sizeToNum = 14;
                        break;
                    case "Medium":
                        sizeToNum = 18;
                        break;
                    case "Large":
                        sizeToNum = 22;
                        break;
                    default:
                        break;
                }
                setFontSize(sizeToNum);
                int finalSizeToNum = sizeToNum;
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        NoteDatabase db = new NoteDatabase(getApplicationContext());
                        db.updateTextNoteSize(currentNoteID, finalSizeToNum);
                    }
                });
            }
        };

        smallBtn.setOnClickListener(sizeListener);
        mediumBtn.setOnClickListener(sizeListener);
        largeBtn.setOnClickListener(sizeListener);

        fontSizePopup.setOutsideTouchable(true);
        fontSizePopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

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
        int y = location[1] - fontColorPopup.getHeight() - 325;
        fontColorPopup.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);


    }

    /**
     * Handles the font size settings menu
     * @param anchorView the button view it will be attached to
     */
    private void showFontSizePopup(View anchorView) {

        // find the location of the button
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1] - fontSizePopup.getHeight() - 500;
        fontSizePopup.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);


    }
}

