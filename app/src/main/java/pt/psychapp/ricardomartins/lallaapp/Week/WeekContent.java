package pt.psychapp.ricardomartins.lallaapp.Week;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class WeekContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Day> ITEMS = new ArrayList<Day>();

    /**
     * A map of sample (dummy) items, by ID.
     */
//    public static final Map<String, Day> ITEM_MAP = new HashMap<String, Day>();


    public static void addItem(Day item) {
        ITEMS.add(item);
       // ITEM_MAP.put(item.id, item);
    }

    public void ChangeValue(Day item, int period){
        //Log.i("OK..", "changing index " + ITEMS.indexOf(item));
        Day Day_2BChanged = ITEMS.get(ITEMS.indexOf(item));
        switch (period){
            case 1:
                Day_2BChanged.Bmorning= !Day_2BChanged.Bmorning;
                break;
            case 2:
                Day_2BChanged.Bafternoon= !Day_2BChanged.Bafternoon;
                break;
            case 3:
                Day_2BChanged.Bnight= !Day_2BChanged.Bnight;
                break;
        }
        //ITEM_MAP.remove(item.id);
        //ITEM_MAP.put(Day_2BChanged.id, Day_2BChanged);
    }

    public void Clear(){
        ITEMS.clear();
        //ITEM_MAP.clear();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Day {
        public final String id;
        public final int TrainingDayNb;
        public final int day;
        public int month;
        public int year;
        public final String weekday;
        public boolean Bmorning;
        public boolean Bafternoon;
        public boolean Bnight;
        public boolean Active;



        public Day(String id, int TrainingDayNb, int day, int month, int year, String weekday, boolean morning, boolean afternoon, boolean night, boolean Active) {
            this.id = id;
            this.TrainingDayNb = TrainingDayNb;
            this.day = day;
            this.month = month;
            this.year = year;
            this.weekday = weekday;
            this.Bmorning = morning;
            this.Bafternoon = afternoon;
            this.Bnight = night;
            this.Active = Active;
        }
    }
}
