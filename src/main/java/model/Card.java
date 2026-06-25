package model;

import ai.AIServiceInterface;
import ai.GeminiService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.ArrayList;

public class Card {
    private String word;
    private String description;
    private WeightedArrayList<String> examples;
    private ArrayList<String> mastery;
    private int level = 1;

    // SM2
    private int repetitions = 0;
    private int interval = 0;
    private double ef = startEF; // ease factor

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate nextReview = LocalDate.now();
    private double stability = 1.0;   // "устойчивость памяти"
    private double difficulty = 1.0;  // сложность карточки

    public static final double startEF = 2.5;
    public static final int MAXLEVEL = 3;

    public Card() {
        this.examples = new WeightedArrayList<>();
        this.mastery = new ArrayList<>();
    }
    
    public Card(String word, String description, ArrayList<String> examples,
                ArrayList<String> mastery) {
        this.word = word;
        this.description = description;

        this.examples = new WeightedArrayList<>();
        this.mastery = new ArrayList<>();

        this.examples.addFromList(examples);
        this.examples.add(null);
        this.examples.setSameWeights();

        this.mastery.addAll(mastery);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WeightedArrayList<String> getExamples() {
        return examples;
    }

    public ArrayList<String> getMastery() {
        return mastery;
    }

    public int getLevel() {
        return level;
    }

    public void levelUp() {
        if (level < MAXLEVEL && nextReview.isAfter(LocalDate.now())) {
            level++;
        }
    }

    public void levelDown() {
        if (level > 1) {
            level--;
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @JsonIgnore
    public boolean isDue() {
        return !LocalDate.now().isBefore(nextReview);
    }

    public void addExamples(ArrayList<String> e) {
        examples.addFromList(e);
    }

    public void addMastery(ArrayList<String> s) {
        mastery.addAll(s);
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public double getEf() {
        return ef;
    }

    public void setEf(double ef) {
        this.ef = ef;
    }

    public LocalDate getNextReview() {
        return nextReview;
    }

    public void setNextReview(LocalDate nextReview) {
        this.nextReview = nextReview;
    }

    public double getStability() {
        return stability;
    }

    public void setStability(double stability) {
        this.stability = stability;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    @JsonIgnore
    public double getLevelMod() {
        if (level == 1) {
            return 1;
        } else if (level == 2) {
            return 0.2;
        } else {
            return 1;
        }
    }
}
