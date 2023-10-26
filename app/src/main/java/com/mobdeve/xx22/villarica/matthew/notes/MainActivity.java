package com.mobdeve.xx22.villarica.matthew.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobdeve.xx22.villarica.matthew.notes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.viewBinding.getRoot());


        //uncomment code below to test checklist

        //Intent intent = new Intent(MainActivity.this, ChecklistTestActivity.class);
        //startActivity(intent);

    }
}