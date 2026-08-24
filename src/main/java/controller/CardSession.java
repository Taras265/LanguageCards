package controller;

import model.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardSession {

    private final List<Card> cards;
    private int currentIndex = 0;

    public CardSession(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        Collections.shuffle(this.cards);
    }

    public boolean hasCards() {
        return !cards.isEmpty();
    }

    public Card nextCard() {
        if (!hasCards()) {
            return null;
        }

        if (!cards.get(currentIndex).isDue()) {
            cards.remove(currentIndex);

            if (!hasCards()) {
                return null;
            }
        } else {
            currentIndex++;
        }

        if (currentIndex >= cards.size()) {
            currentIndex = 0;
            Collections.shuffle(this.cards);
        }

        return getCurrentCard();
    }

    public Card getCurrentCard() {
        if (!hasCards()) {
            return null;
        }

        return cards.get(currentIndex);
    }

    public int remainingCardsAmount() {
        return cards.size();
    }
}
