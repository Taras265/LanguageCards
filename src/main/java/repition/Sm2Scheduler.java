package repition;

import model.Card;

import java.time.LocalDate;

public class Sm2Scheduler implements SpacedRepetitionScheduler {

    public void reviewCard(Card card, int quality, int taskLevel) {
        // обновляем ef
        double ef = card.getEf() + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
        ef = Math.max(1.3, ef);
        card.setEf(ef);

        // обновляем difficulty
        double difficulty = card.getDifficulty();
        difficulty += (5 - quality) * 0.1;
        difficulty = Math.max(0.5, Math.min(difficulty, 3.0));
        card.setDifficulty(difficulty);

        // обновляем stability
        double stability = card.getStability();
        int repetitions = card.getRepetitions();
        if (quality < 3) {
            stability *= 0.5; // стабильность резко падает
            repetitions = 0;
        } else {
            stability *= ef;  // рост экспоненциальный у стабильности
            repetitions++;
        }
        card.setStability(stability);
        card.setRepetitions(repetitions);

        int interval = (int) Math.round((stability * card.getLevelMod()) / difficulty);

        if (interval < 1) {
            interval = 1;
        } else {
            System.out.println(interval);
            card.setNextReview(LocalDate.now().plusDays(interval));
        }

        card.setInterval(interval);

        int level = card.getLevel();
        if (taskLevel < level && repetitions == 0) {
            card.levelDown();
        }
        if (repetitions >= 6*level && level < 3) {
            card.levelUp();
            card.setRepetitions(0);
            card.setEf(Card.startEF);
        }
    }
}
