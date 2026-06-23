package models;

import ai.AIServiceInterface;
import ai.GeminiService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

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
    private LocalDate nextReview = LocalDate.now();
    private double stability = 1.0;   // "устойчивость памяти"
    private double difficulty = 1.0;  // сложность карточки

    public static final double startEF = 2.5;
    private final AIServiceInterface ai = GeminiService.getInstance();

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
        if (level < 3 && nextReview.isAfter(LocalDate.now())) {
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

    public Task getTask() {
        return switch (level) {
            case 1 -> firstLevelTask();
            case 2 -> secondLevelTask();
            default -> thirdLevelTask();
        };
    }

    public boolean isDue() {
        return !LocalDate.now().isBefore(nextReview);
    }

    public void completeTask(int quality, int taskLevel) {
        applySm2(quality);
        if (taskLevel < level && repetitions == 0) {
            levelDown();
        }
        if (repetitions >= 6*level && level < 3) {
            levelUp();
            repetitions = 0;
            ef = startEF;
        }
    }

    private void applySm2(int quality) {
        // обновляем ef
        ef = ef + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
        ef = Math.max(1.3, ef);

        // обновляем difficulty
        difficulty += (5 - quality) * 0.1;
        difficulty = Math.max(0.5, Math.min(difficulty, 3.0));

        // обновляем stability
        if (quality < 3) {
            stability *= 0.5; // стабильность резко падает
            repetitions = 0;
        } else {
            stability *= ef;  // рост экспоненциальный у стабильности
            repetitions++;
        }

        interval = (int) Math.round((stability * getLevelMod()) / difficulty);

        if (interval < 1) {
            interval = 1;
        } else {
            System.out.println(interval);
            nextReview = LocalDate.now().plusDays(interval);
        }
    }

    private Task firstLevelTask() {
        ArrayList<String> taskArguments = new ArrayList<>();
        taskArguments.add(word);
        taskArguments.add(description);
        Collections.shuffle(taskArguments);
        return new Task(taskArguments.getFirst(), taskArguments.getLast(), 1);
    }

    private Task secondLevelTask() {
        String example = examples.getRandom();

        while (example == null) {
            addNewExample();
            example = examples.getRandom();
        }

        example = example.replaceAll(word, "---");
        example = example + "\n\n" + description;
        return new Task(example, word, 2);
    }

    private Task thirdLevelTask() {
        String m = mastery.getFirst();
        mastery.removeFirst();

        if (mastery.isEmpty()) {
            addNewMastery();
        }

        String task = "Create sentence with using word \"" + word + "\"\n" + m;
        return new Task(task, "", 3);
    }

    private void addNewExample() {
        ArrayList<String> s = ai.createExamples(word);
        for (String e: s) {
            examples.add(e);
        }
    }

    private void addNewMastery() {
        ArrayList<String> s = ai.createMasteries(word);
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

    private double getLevelMod() {
        if (level == 1) {
            return 1;
        } else if (level == 2) {
            return 0.2;
        } else {
            return 1;
        }
    }
}
