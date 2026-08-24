package repition;

import model.Card;

import java.time.LocalDate;
import java.util.Random;

public class Sm2Scheduler implements SpacedRepetitionScheduler {
    private final Random random = new Random();

    public void reviewCard(Card card, int quality) {
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

        int interval = calculateInterval(
                card.getLevel(),
                stability,
                difficulty
        );

        if (interval < 1) {
            card.setInterval(1);
            card.setRepetitions(repetitions);
            return;
        }

        // Учитываем только успешные повторения,
        // после которых карточка переносится на другой день.
        int result = quality - 2;
        if (result > 0) {
            repetitions += result;
        }


        int level = card.getLevel();
        if (repetitions >= 4*level && level < Card.MAXLEVEL) {
            card.levelUp();
            repetitions = 0;
            card.setEf(Card.startEF);
            interval = 1;
        }
        card.setInterval(interval);
        card.setRepetitions(repetitions);
        card.setNextReview(LocalDate.now().plusDays(interval));
    }

    private int calculateInterval(
            int level,
            double stability,
            double difficulty
    ) {
        int interval = (int) Math.round(
                stability * getLevelMod(level) / difficulty
        );

        return addRandomVariation(interval);
    }


    private double getLevelMod(int level) {
        if (level == 1) {
            return 1;
        } else
        if (level == 2) {
            return 1;
        } else if (level == 3) {
            return 0.2;
        } else {
            return 1;
        }
    }

    private int addRandomVariation(int interval) {
        double perc = random.nextDouble(-0.1, 0.1);
        return (int) (interval + interval * perc);
    }
}
