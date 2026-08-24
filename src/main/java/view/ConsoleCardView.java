package view;

import model.Task;

import java.util.Scanner;

public class ConsoleCardView {

    private final Scanner input = new Scanner(System.in);

    public void showWelcome() {
        System.out.println("Приветствую в приложении \"Language Card\"!");
    }

    public void showMenu() {
        System.out.println("Что бы вы хотели сделать?");
        System.out.println("\t 1) Получить сегодняшние карточки >.<");
        System.out.println("\t 2) Добавить новые карточки >.<");
        System.out.println("\t 3) Добавить новую карточку >.<");
        System.out.println("\t 4) Выйти :(");
    }

    public String readInput() {
        return input.nextLine();
    }

    public void showLoading(String message) {
        System.out.println(message);
    }

    public void showTask(Task task) {
        System.out.println(task.getTask());
    }

    public void showAnswer(Task task) {
        System.out.println("Правильный ответ: " + task.getAnswer());
    }

    public void showRatingOptions() {
        System.out.println("Оцените правильность ответа: ");
        System.out.println("\t 5 - Идеально");
        System.out.println("\t 4 - Хорошо");
        System.out.println("\t 3 - Сложно, но вспомнил");
        System.out.println("\t 2 - Ошибся");
        System.out.println("\t 1 - Почти не помню");
        System.out.println("\t 0 - Не помню");
    }

    public void showAudioLink(String word) {
        System.out.println(
                "Услышать слово можно перейдя по следующей ссылке: "
        );
        System.out.println("https://clip.cafe/s/" + word);
    }

    public void showRemainingCards(int count) {
        System.out.println(
                "Количество карточек осталось: " + count
        );
    }

    public void showNextReview(String nextReview) {
        System.out.println("Следующий повтор: " + nextReview);
    }

    public void showCardsFinished() {
        System.out.println("Карточки закончились >.<");
    }

    public void showError() {
        System.out.println(
                "Похоже, произошла какая то ошибка " +
                        "или вы ввели неверное значение >.<"
        );
    }

    public void showAddCardMessage() {
        System.out.println(
                "Введите слово на английском, " +
                        "которое вы хотите запомнить."
        );
    }

    public void showAddCardsMessage() {
        System.out.println(
                "Введите слово на английском, " +
                        "которое вы хотите запомнить."
        );
        System.out.println(
                "Что бы прекратить ввод, введите \"-1\" :)"
        );
    }
}