package com.mobdeve.xx22.villarica.matthew.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<FolderModel> folders;
    private MainActivityAdapter mainAdapter;
    private ActivityMainBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.viewBinding.getRoot());

        this.folders = FolderDataHelper.generateFolderData();

        setupRecyclerView();
    }

    private void setupRecyclerView() {  // TODO: add ActivityResultLauncher
        mainAdapter = new MainActivityAdapter(this.folders);
        viewBinding.folderRv.setAdapter(mainAdapter);
        viewBinding.folderRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

}