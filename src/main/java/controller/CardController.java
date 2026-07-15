package controller;

import ai.AIServiceInterface;
import com.google.genai.errors.ServerException;
import factory.TaskFactory;
import factory.TaskFactoryResult;
import model.Card;
import model.Task;
import repition.Sm2Scheduler;
import repository.CardsRepositoryInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CardController {
    private final CardsRepositoryInterface cardRepository;
    private final AIServiceInterface aiService;
    private static final Sm2Scheduler cardScheduler = new Sm2Scheduler();

    public CardController(CardsRepositoryInterface cRep, AIServiceInterface aiServ) {
        cardRepository = cRep;
        aiService = aiServ;
    }

    public ArrayList<Card> getTodayCards(int cardsNum) {
        int newCards = (int) Math.ceil(cardsNum*0.2);
        int lvl1Cards = (int) Math.ceil(cardsNum*0.2);
        int lvl2Cards = (int) Math.ceil(cardsNum*0.2);
        int lvl3Cards = (int) Math.ceil(cardsNum*0.3);
        int lvl4Cards = (int) Math.ceil(cardsNum*0.1);

        List<Card> cards = cardRepository.getCards().stream()
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
        List<Card> lvl4Array = cards.stream()
                .filter(c -> c.getLevel() >= 4)
                .limit(lvl4Cards)
                .toList();
        ArrayList<Card> result = new ArrayList<>();
        result.addAll(newArray);
        result.addAll(lvl1Array);
        result.addAll(lvl2Array);
        result.addAll(lvl3Array);
        result.addAll(lvl4Array);
        return result;
    }

    public Task getTaskForCard(Card card) {
        while (true) {
            TaskFactoryResult taskResult;
            try {
                taskResult = TaskFactory.getTask(card);
            } catch (ServerException e) {
                return TaskFactory.createFallbackTask(card);
            }
            switch (taskResult) {
                case TaskFactoryResult.Success success -> {
                    return success.task();
                }
                case TaskFactoryResult.NeedsExamples ignored -> {
                    ArrayList<String> examples = aiService.createExamples(card.getWord());
                    // если что то произошло и в итоге не вернули данные - даем другую карточку
                    if (examples.isEmpty()) return TaskFactory.createFallbackTask(card);

                    card.addExamples(examples);
                    cardRepository.updateCard(card);
                }
                case TaskFactoryResult.NeedsMastery ignored -> {
                    ArrayList<String> mastery;
                    try {
                        mastery = aiService.createMasteries(card.getWord());
                    } catch (ServerException e) {
                        return TaskFactory.createFallbackTask(card);
                    }

                    card.addMastery(mastery);
                    cardRepository.updateCard(card);
                }
            }
        }
    }

    public void createCards(ArrayList<String> c) {
        c.forEach(aiService::addWord);
        createCards();
    }
    public void createCards() {
        if (aiService.haveWords()) {
            cardRepository.addCards(aiService.createCards());
        }
    }

    public void updateCard(Card c) {
        cardRepository.updateCard(c);
    }

    public void addCard(String card) {
        aiService.addWord(card);
    }

    public void reviewCard(Card card, int choice, int level) {
        cardScheduler.reviewCard(card, choice, level);
        cardRepository.updateCard(card);
    }
}
