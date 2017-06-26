package pt.psychapp.ricardomartins.lallaapp.Quizz;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class QuestionContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Question> ITEMS = new ArrayList<Question>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    //public static final Map<String, Question> ITEM_MAP = new HashMap<String, Question>();


    public static void addItem(Question item) {
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
    public static class Question {
        public final int number;
        public final int answer1;
        public final int answer2;

        public Question( int number, int a1, int a2) {
            this.number = number;
            this.answer1 = a1;
            this.answer2 = a2;
        }
    }
}
