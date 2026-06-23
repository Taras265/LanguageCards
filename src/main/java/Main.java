import ai.GeminiService;
import app.ConsoleApp;
import models.Card;
import models.Task;
import repository.CardsJsonRepository;
import repository.CardsRepositoryInterface;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ConsoleApp app = new ConsoleApp();
        app.start(35);
    }
}