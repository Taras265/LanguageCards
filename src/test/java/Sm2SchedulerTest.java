import model.Card;
import org.junit.jupiter.api.Test;
import repition.Sm2Scheduler;

import static org.junit.jupiter.api.Assertions.*;

class Sm2SchedulerTest {

    private final Sm2Scheduler scheduler = new Sm2Scheduler();

    @Test
    void newCardShouldHaveInitialValues() {
        Card card = new Card();

        assertEquals(1, card.getLevel());
        assertEquals(0, card.getRepetitions());
        assertEquals(0, card.getInterval());
        assertEquals(Card.startEF, card.getEf());
        assertEquals(1.0, card.getStability());
        assertEquals(1.0, card.getDifficulty());
    }

    @Test
    void successfulReviewShouldScheduleCard() {
        Card card = new Card();

        scheduler.reviewCard(card, 5);

        assertTrue(card.getInterval() >= 1);
        assertTrue(card.getNextReview().isAfter(java.time.LocalDate.now()));
        assertEquals(3, card.getRepetitions());
    }

    @Test
    void failedReviewShouldResetRepetitions() {
        Card card = new Card();

        card.setRepetitions(5);

        scheduler.reviewCard(card, 0);

        assertEquals(0, card.getRepetitions());
    }

    @Test
    void badAnswerShouldDecreaseEf() {
        Card card = new Card();

        double initialEf = card.getEf();

        scheduler.reviewCard(card, 0);

        assertTrue(card.getEf() < initialEf);
    }

    @Test
    void goodAnswerShouldIncreaseEf() {
        Card card = new Card();

        double initialEf = card.getEf();

        scheduler.reviewCard(card, 5);

        assertTrue(card.getEf() > initialEf);
    }

    @Test
    void efShouldNeverBeBelowMinimum() {
        Card card = new Card();

        for (int i = 0; i < 20; i++) {
            scheduler.reviewCard(card, 0);
        }

        assertTrue(card.getEf() >= 1.3);
    }

    @Test
    void cardShouldLevelUpAfterEnoughSuccessfulReviews() {
        Card card = new Card();

        for (int i = 0; i < 4; i++) {
            scheduler.reviewCard(card, 5);
        }

        assertEquals(2, card.getLevel());
    }
}