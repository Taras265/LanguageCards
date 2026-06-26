package repository;

import model.Card;

import java.time.LocalDate;
import java.util.ArrayList;

public interface CardsRepositoryInterface {
    void restoreFromDate(LocalDate date);

    ArrayList<Card> getCards();
    void addCard(Card card);
    void addCards(ArrayList<Card> cards);
    void updateCard(Card card);
    void deleteCard(Card card);
}
