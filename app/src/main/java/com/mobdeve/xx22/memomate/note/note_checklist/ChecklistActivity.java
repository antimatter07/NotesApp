package com.mobdeve.xx22.memomate.note.note_checklist;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.textfield.TextInputLayout;
import com.mobdeve.xx22.memomate.R;
import com.mobdeve.xx22.memomate.databinding.ChecklistItemBinding;
import com.mobdeve.xx22.memomate.database.NoteDatabase;
import com.mobdeve.xx22.memomate.model.CheckListNoteModel;
import com.mobdeve.xx22.memomate.model.TextNoteModel;
import com.mobdeve.xx22.memomate.partials.NoteOptionsFragment;
import com.mobdeve.xx22.memomate.databinding.ChecklistActivityBinding;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChecklistActivity extends AppCompatActivity {
    public static final String ITEMLIST_KEY = "ITEMLIST_KEY";
    public static final String TITLE_KEY = "TITLE_KEY";
    public static final String FOLDER_KEY = "FOLDER_KEY";
    public static final String DATE_CREATED_KEY = "DATE_KEY";
    public static final String DATE_MODIFIED_KEY = "DATE_MODIFIED_KEY";

    private PopupWindow fontSizePopup;
    private PopupWindow fontColorPopup;
    private EditText activeChecklistItem;
    private int activeItemID;

    /**
     * note id of current checklist note
     */
    private int currentNoteID;

    /**
     * Thread for db operations
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private NoteDatabase noteDatabase;

    private int noteColor;
    // Handler associated with the main (UI) thread
    private Handler mainHandler;

    private boolean isTitleContentChanged = false;

    private String currentDateTime;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChecklistActivityBinding viewBinding = ChecklistActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        noteDatabase = new NoteDatabase(getApplicationContext());

        //make handler for ui updates
        mainHandler = new Handler(getMainLooper());

        //retrieve data from intent
        Intent intent = getIntent();
        String titleString = intent.getStringExtra(TITLE_KEY);
        ArrayList<ChecklistItemModel> listData = intent.getParcelableArrayListExtra(ITEMLIST_KEY);

        // set colors of note bar and checkboxes
        noteColor = ContextCompat.getColor(this, getIntent().getIntExtra("noteColor", R.color.folderDefault));
        viewBinding.noteBarCl.setBackgroundColor(noteColor);

        //set up views and adapter with received data
        viewBinding.noteTitle.setText(titleString);
        ChecklistAdapter adapter = new ChecklistAdapter(getApplicationContext(), this, listData, noteDatabase, noteColor);

        RecyclerView recyclerview = viewBinding.recyclerView;

        // set up popups
        setUpPopupWindows();

        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        // create a new item by default when creating new checklist

        recyclerview.setAdapter(adapter);

        if(listData.size() == 0)
            adapter.notifyItemInserted(listData.size() - 1);



        // Setup Note Options Button
        viewBinding.noteOptionsBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            NoteOptionsFragment noteOptionsFragment = new NoteOptionsFragment();
            noteOptionsFragment.show(fm, "NoteOptionsDialog");
        });

        currentNoteID = getIntent().getIntExtra("noteID", -1);
        int folderKey = getIntent().getIntExtra(ChecklistActivity.FOLDER_KEY, -1);


        //if noteID retrieved is default value, create a new checklist note in db
        if(currentNoteID == -1) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    //make new text note and insert into db

                    CheckListNoteModel checklistNote = new CheckListNoteModel(titleString,
                            folderKey,
                            listData);

                    currentNoteID = noteDatabase.addCheckListNote(checklistNote);

                }
            });
        }

        if (listData.size() == 0) {

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    int newCheckListItemId = noteDatabase.addCheckListItem(currentNoteID);
                    ChecklistItemModel newItem = new ChecklistItemModel(false, "");
                    newItem.setItemId(newCheckListItemId);

                    // Update the UI on the main thread
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listData.add(newItem);
                            adapter.notifyItemInserted(listData.size() - 1);
                        }
                    });
                }
            });

        }

        //add notes with button
        viewBinding.addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.add(new ChecklistItemModel(false, ""));


                executorService.execute(new Runnable() {
                    @Override
                    public void run() {

                        int newCheckListItemId = noteDatabase.addCheckListItem(currentNoteID);
                        //UI updates after adding new check list item
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listData.get(listData.size() - 1).setItemId(newCheckListItemId);
                                adapter.notifyItemInserted(listData.size() - 1);


                            }
                        });

                    }
                });
                ;
            }
        });

        viewBinding.noteTitle.addTextChangedListener(new TextWatcher() {
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
                            String updatedTitle = viewBinding.noteTitle.getText().toString();

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
        viewBinding.fontOptionsLl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                viewBinding.fontOptionsLl.getWindowVisibleDisplayFrame(r);
                int screenHeight = viewBinding.fontOptionsLl.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > (screenHeight * 0.15))
                    viewBinding.fontOptionsLl.setVisibility(View.VISIBLE);
                else  {
                    if (fontColorPopup != null)
                        fontColorPopup.dismiss();
                    viewBinding.fontOptionsLl.setVisibility(View.GONE);
                }
            }
        });

        viewBinding.fontSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFontSizePopup(v);
            }
        });

        viewBinding.fontColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFontColorPopup(v);
            }
        });


    }


    /**
     * Gets current date and time in proper format for sorting in SQLite
     * @return SimpleDateFormat when ParentNoteModel was instantiated.
     */
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
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
                    if (activeChecklistItem != null) {
                        fontColorPopup.dismiss();
                        activeChecklistItem.setTextColor(
                                ContextCompat.getColor(ChecklistActivity.this, colorBtn.getValue()));

                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                NoteDatabase db = new NoteDatabase(getApplicationContext());
                                db.updateChecklistItemColor(activeItemID, colorBtn.getValue());
                            }
                        });
                    }

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
                if (activeChecklistItem != null) {
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
                    activeChecklistItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeToNum);
                    int finalSizeToNum = sizeToNum;

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            NoteDatabase db = new NoteDatabase(getApplicationContext());
                            db.updateChecklistItemSize(activeItemID, finalSizeToNum);
                        }
                    });
                }

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

    public void setActiveChecklistItem(EditText activeChecklistItem, int itemId) {
        this.activeChecklistItem = activeChecklistItem;
        this.activeItemID = itemId;
    }
}
