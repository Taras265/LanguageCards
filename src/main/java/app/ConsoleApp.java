package app;

import controller.CardController;
import factory.TaskFactory;
import model.Card;
import model.Task;
import repition.Sm2Scheduler;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleApp {
    CardController cardController;

    public ConsoleApp(CardController cardController) {
        this.cardController = cardController;
    }

    public void start(int maxCards) {
        System.out.println("Приветствую в приложении \"Language Card\"!");
        askToDo();
        Scanner input = new Scanner(System.in);
        String choice;
        while (!(choice = input.nextLine()).equals("4")) {
            if (choice.equals("1")) {
                getTodayCards(input, maxCards);
            } else if (choice.equals("2")) {
                System.out.println("Введите слово на английском, которое вы хотите запомнить.");
                System.out.println("Что бы прекратить ввод, введите \"-1\" :)");
                while (!(choice = input.nextLine()).equals("-1")) {
                    cardController.addCard(choice);
                }
                System.out.println("Подождите, карточки создаются. Извините за ожидение >.<");
                cardController.createCards();
                askToDo();
            } else if (choice.equals("3")) {
                System.out.println("Введите слово на английском, которое вы хотите запомнить: ");
                cardController.addCard(input.nextLine());
                askToDo();
                System.out.println("Создаем карточки... Извините, если это займет какое то время >.<");
            } else {
                System.out.println("Похоже, произошла какая то ошибка или вы ввели неверное значение >.<");
                askToDo();
            }
        }
    }

    private void askToDo() {
        System.out.println("Что бы вы хотели сделать?");
        System.out.println("\t 1) Получить сегодняшние карточки >.<");
        System.out.println("\t 2) Добавить новые карточки >.<");
        System.out.println("\t 3) Добавить новую карточку >.<");
        System.out.println("\t 4) Выйти :(");
    }

    private void getTodayCards(Scanner input, int maxCards) {
        String choice;

        System.out.println("Грузим карточки... Извините, если это займет какое то время >.<");
        cardController.createCards();
        ArrayList<Card> cards = cardController.getTodayCards(maxCards);
        while (!cards.isEmpty()) {
            System.out.println("Количество карточек осталось: " + cards.size());
            for (Card card : cards) {
                Task task = cardController.getTaskForCard(card);

                System.out.println(task.getTask());
                input.nextLine();
                System.out.println("Правильный ответ: " + task.getAnswer());
                System.out.println("Оцените правильность ответа: ");
                System.out.println("\t 5 - Идеально");
                System.out.println("\t 4 - Хорошо");
                System.out.println("\t 3 - Сложно, но вспомнил");
                System.out.println("\t 2 - Ошибся");
                System.out.println("\t 1 - Почти не помню");
                System.out.println("\t 0 - Не помню");

                System.out.println("Услышать слово можно перейдя по следующей ссылке: ");
                System.out.println("https://clip.cafe/s/" + card.getWord());

                do {
                    choice = input.nextLine();
                } while (!choice.equals("0") &&
                        !choice.equals("1") &&
                        !choice.equals("2") &&
                        !choice.equals("3") &&
                        !choice.equals("4") &&
                        !choice.equals("5"));

                cardController.reviewCard(card, Integer.parseInt(choice), task.getLevel());
                System.out.println("Следующий повтор: " + card.getNextReview());
            }
            cards.removeIf(c -> !c.isDue());
        }
        System.out.println("Карточки закончились >.<");
        askToDo();
    }
}
