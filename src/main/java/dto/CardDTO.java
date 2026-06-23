package dto;

import java.util.ArrayList;
import java.util.List;

public class CardDTO {
    public String word;
    public String description;

    public List<WeightedItemDTO> examples = new ArrayList<>();
    public List<String> mastery = new ArrayList<>();

    public int level;

    public int repetitions;
    public int interval;
    public double ef;
    public String nextReview;

    public double stability;
    public double difficulty;

    public CardDTO() {}

    public CardDTO(String word, String description,
                   List<WeightedItemDTO> examples,
                   List<String> mastery,
                   int level,
                   int repetitions,
                   int interval,
                   double ef,
                   String nextReview,
                   double stability,
                   double difficulty) {
        this.word = word;
        this.description = description;
        this.examples = examples;
        this.mastery = mastery;
        this.level = level;

        this.repetitions = repetitions;
        this.interval = interval;
        this.ef = ef;
        this.nextReview = nextReview;

        this.stability = stability;
        this.difficulty = difficulty;
    }
}