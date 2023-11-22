package com.mobdeve.xx22.memomate.database;

//generate data

import com.mobdeve.xx22.memomate.model.CheckListNoteModel;
import com.mobdeve.xx22.memomate.model.ChecklistItemModel;
import com.mobdeve.xx22.memomate.model.TextNoteModel;

import java.util.ArrayList;
public class NoteDataHelper {
    public static ArrayList<TextNoteModel> loadTextNote(){
        ArrayList<TextNoteModel> data = new ArrayList<>();

        data.add(new TextNoteModel(
                "Brownisaur", -1,
                "Sunlight is absorbed by its whipped cream and used to make the cherry on its back grow larger in size."
        ));
        data.add(new TextNoteModel(
                "Chocosaur", 0,
                "The cherry on its back is known to be quite delicious but it is also heavy making it a useful blunt attack."
        ));
        data.add(new TextNoteModel(
                "Fudgasaur", 0,"It grows many cherries. It can trap prey inside its whipped cream but it’s also a fun ride."
        ));

        return data;
    }

    public static ArrayList<CheckListNoteModel> loadCheckListNote(){
        ArrayList<CheckListNoteModel> data = new ArrayList<>();
        ArrayList<ChecklistItemModel> item = new ArrayList<>();
        ArrayList<ChecklistItemModel> todo = new ArrayList<>();

        //item data
        item.add(new ChecklistItemModel(
                true, "eggs"
        ));
        item.add(new ChecklistItemModel(
                false, "chicken"
        ));
        item.add(new ChecklistItemModel(
                true, "beef"
        ));
        item.add(new ChecklistItemModel(
                true, "water"
        ));

        //todolist data
        todo.add(new ChecklistItemModel(
                true, "STINTSY Notebook 6"
        ));
        todo.add(new ChecklistItemModel(
                false, "MOBDEVE MCO2"
        ));
        todo.add(new ChecklistItemModel(
                true, "CSOPESY Defense"
        ));


        data.add(new CheckListNoteModel(
                "Grocery List",
                item
        ));
        data.add(new CheckListNoteModel(
                "Task To Do", 2, todo
        ));

        return data;
    }

}

