import ai.AIServiceInterface;
import ai.GeminiService;
import app.ConsoleApp;
import controller.CardController;
import repository.CardsJsonRepository;
import repository.CardsRepositoryInterface;

public class Main {
    public static void main(String[] args) {
        CardsRepositoryInterface cardRepository = new CardsJsonRepository();
        AIServiceInterface aiService = new GeminiService();

        CardController cardController = new CardController(cardRepository, aiService);

        ConsoleApp app = new ConsoleApp(cardController);
        app.start(35);
    }
}