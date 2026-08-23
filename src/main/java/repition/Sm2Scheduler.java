package repition;

import model.Card;

import java.time.LocalDate;
import java.util.Random;

public class Sm2Scheduler implements SpacedRepetitionScheduler {
    private Random random = new Random();

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
        }
        card.setStability(stability);

        int interval = (int) Math.round((stability * card.getLevelMod()) / difficulty);
        double perc = random.nextDouble(-0.1, 0.1);
        interval = (int) (interval + (interval * perc));

        if (interval < 1) {
            interval = 1;
        } else {
            System.out.println(interval);
            int result = quality-2;
            if (result > 0) {
                repetitions += result;
                System.out.println(result);
            }
            card.setNextReview(LocalDate.now().plusDays(interval));
        }
        card.setRepetitions(repetitions);

        int level = card.getLevel();
        if (taskLevel < level && repetitions == 0) {
            card.levelDown();
            interval = 1;
        }
        if (repetitions >= 4*level && level < Card.MAXLEVEL) {
            card.levelUp();
            card.setRepetitions(0);
            card.setEf(Card.startEF);
            interval = 1;
        }
        card.setInterval(interval);
    }
}
