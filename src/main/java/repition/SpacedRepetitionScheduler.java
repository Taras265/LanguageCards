package repition;

import model.Card;

public interface SpacedRepetitionScheduler {
    void reviewCard(Card card, int quality, int taskLevel);
}
