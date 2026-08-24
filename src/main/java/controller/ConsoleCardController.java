package controller;

import model.Card;
import model.Task;
import service.CardService;
import view.ConsoleCardView;

public class ConsoleCardController {

    private final CardService cardService;
    private final ConsoleCardView cardView;

    public ConsoleCardController(
            CardService service,
            ConsoleCardView view
    ) {
        cardService = service;
        cardView = view;
    }

    public void start() {

        cardView.showWelcome();
        cardView.showMenu();

        String choice;

        while (!(choice = cardView.readInput()).equals("4")) {

            switch (choice) {
                case "1" -> getTodayCards();
                case "2" -> addCards();
                case "3" -> addCard();
                default -> {

                    cardView.showError();
                    cardView.showMenu();
                }
            }
        }
    }

    private void addCards() {

        cardView.showAddCardsMessage();

        String choice;

        while (!(choice = cardView.readInput()).equals("-1")) {
            cardService.addCard(choice);
        }

        cardView.showLoading(
                "Подождите, карточки создаются. " +
                        "Извините за ожидание >.<"
        );

        cardService.createCards();

        cardView.showMenu();
    }

    private void addCard() {

        cardView.showAddCardMessage();

        cardService.addCard(cardView.readInput());

        cardView.showMenu();

        cardView.showLoading(
                "Создаем карточки... " +
                        "Извините, если это займет какое то время >.<"
        );
    }

    private void getTodayCards() {

        int newCards = 0;
        int lvl1Cards = 15;
        int lvl2Cards = 20;
        int lvl3Cards = 20;
        int lvl4Cards = 2;

        cardView.showLoading(
                "Грузим карточки... " +
                        "Извините, если это займет какое то время >.<"
        );

        cardService.createCards();

        CardSession session = new CardSession(
                cardService.getTodayCards(
                        newCards,
                        lvl1Cards,
                        lvl2Cards,
                        lvl3Cards,
                        lvl4Cards
                )
        );

        while (session.hasCards()) {
            cardView.showRemainingCards(session.remainingCardsAmount());

            Card card = session.getCurrentCard();
            Task task = cardService.getTaskForCard(card);

            cardView.showTask(task);
            cardView.readInput();
            cardView.showAnswer(task);
            cardView.showRatingOptions();
            cardView.showAudioLink(card.getWord());

            String choice;
            do {
                choice = cardView.readInput();
            } while (
                    !choice.equals("0") &&
                            !choice.equals("1") &&
                            !choice.equals("2") &&
                            !choice.equals("3") &&
                            !choice.equals("4") &&
                            !choice.equals("5")
            );
            cardService.reviewCard(
                    card,
                    Integer.parseInt(choice)
            );
            cardView.showNextReview(
                    card.getNextReview().toString()
            );
            session.nextCard();
        }

        cardView.showCardsFinished();
        cardView.showMenu();
    }
}