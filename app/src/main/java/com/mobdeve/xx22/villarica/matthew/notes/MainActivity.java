package com.mobdeve.xx22.villarica.matthew.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<FolderModel> folders;
    private MainActivityAdapter mainAdapter;
    private ActivityMainBinding viewBinding;

//    TEMP data
    private boolean isOrderAscending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.viewBinding.getRoot());

        this.folders = FolderDataHelper.generateFolderData();

        setupRecyclerView();

        // TODO: Sort Order functionality
        ToggleButton orderBtn = this.viewBinding.orderBtn;

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOrderAscending = !isOrderAscending;
                if(isOrderAscending) {
                    orderBtn.setBackgroundResource(R.drawable.ic_ascend);
                }
                else {
                    orderBtn.setBackgroundResource(R.drawable.ic_descend);
                }
            }
        });

        viewBinding.sortBtn.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            SortingOptionsDialogFragment sortingOptionsFragment = new SortingOptionsDialogFragment();
            sortingOptionsFragment.show(fm, "SettingsDialog");

        });
    }

    private void setupRecyclerView() {  // TODO: add ActivityResultLauncher
        mainAdapter = new MainActivityAdapter(this.folders);
        viewBinding.folderRv.setAdapter(mainAdapter);
        viewBinding.folderRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

}