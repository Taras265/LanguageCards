package ai;

import java.util.ArrayList;
import models.Card;

public interface AIServiceInterface {
    void addWord(String word);
    ArrayList<Card> createCards();
    boolean haveWords();
    ArrayList<String> createExamples(String word);
    ArrayList<String> createMasteries(String word);
}
