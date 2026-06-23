package controller;

import ai.GeminiService;
import models.Card;
import repository.CardsJsonRepository;
import repository.CardsRepositoryInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CardController {
    private static final CardController instance = new CardController();
    private final CardsRepositoryInterface rep = CardsJsonRepository.getInstance();
    private final GeminiService geminiService = GeminiService.getInstance();

    private CardController() {}

    public static CardController getInstance() {
        return instance;
    }

    public ArrayList<Card> getTodayCards(int newCards,
                                         int lvl1Cards,
                                         int lvl2Cards,
                                         int lvl3Cards) {
        List<Card> cards = rep.getCards().stream()
                .filter(Card::isDue).sorted(Comparator.comparing(Card::getNextReview)).toList();
        List<Card> newArray = cards.stream()
                .filter(c -> (c.getLevel() == 1 && c.getEf() == Card.startEF))
                .limit(newCards)
                .toList();
        List<Card> lvl1Array = cards.stream()
                .filter(c -> (c.getLevel() == 1 && c.getEf() != Card.startEF))
                .limit(lvl1Cards)
                .toList();
        List<Card> lvl2Array = cards.stream()
                .filter(c -> c.getLevel() == 2)
                .limit(lvl2Cards)
                .toList();
        List<Card> lvl3Array = cards.stream()
                .filter(c -> c.getLevel() == 3)
                .limit(lvl3Cards)
                .toList();
        ArrayList<Card> result = new ArrayList<>();
        result.addAll(newArray);
        result.addAll(lvl1Array);
        result.addAll(lvl2Array);
        result.addAll(lvl3Array);
        return result;
    }

    public void createCards(ArrayList<String> c) {
        c.forEach(geminiService::addWord);
        createCards();
    }
    public void createCards() {
        if (geminiService.haveWords()) {
            rep.addCards(geminiService.createCards());
        }
    }

    public void updateCard(Card c) {
        rep.updateCard(c);
    }

    public void addCard(String card) {
        geminiService.addWord(card);
    }
}
