package com.example.ricardomartins.lallaapp.Statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Note> ITEMS = new ArrayList<Note>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    //public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();


    public static void addItem(Note item) {
        ITEMS.add(item);
        //ITEM_MAP.put(item.id, item);
    }

    public void Clear(){
        ITEMS.clear();
        //ITEM_MAP.clear();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Note {
        public final String date;
        public final String content;

        public Note(String date, String content) {
            this.content = content;
            this.date = date;
        }
    }
}
