package com.mobdeve.xx22.villarica.matthew.notes;

//generate data

import java.util.ArrayList;
public class DataGenerator {
    public static ArrayList<TextNoteModel> loadTextNote(){
        ArrayList<TextNoteModel> data = new ArrayList<>();

        data.add(new TextNoteModel(
                "Brownisaur", "January 1", 0,
                "Sunlight is absorbed by its whipped cream and used to make the cherry on its back grow larger in size."
        ));
        data.add(new TextNoteModel(
                "Chocosaur", "February 14", 0,
                "The cherry on its back is known to be quite delicious but it is also heavy making it a useful blunt attack."
        ));
        data.add(new TextNoteModel(
                "Fudgasaur", "December 31","It grows many cherries. It can trap prey inside its whipped cream but it’s also a fun ride."
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

        //todo data
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
                "Grocery List", "January 1",
                item
        ));
        data.add(new CheckListNoteModel(
                "Task To Do", "February 14", 2, todo
        ));

        return data;
    }

}

