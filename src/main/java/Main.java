import ai.AIServiceInterface;
import ai.GeminiService;
import controller.ConsoleCardController;
import repository.CardsJsonRepository;
import repository.CardsRepositoryInterface;

public class Main {
    public static void main(String[] args) {
        CardsRepositoryInterface cardRepository = new CardsJsonRepository();
        AIServiceInterface aiService = new GeminiService();

        ConsoleCardController consoleCardController = new ConsoleCardController(cardRepository, aiService);

        consoleCardController.start(35);
    }
}