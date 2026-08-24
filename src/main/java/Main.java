import ai.AIServiceInterface;
import ai.GeminiService;
import controller.ConsoleCardController;
import repition.Sm2Scheduler;
import repository.CardsJsonRepository;
import repository.CardsRepositoryInterface;
import service.CardService;
import view.ConsoleCardView;

public class Main {
    public static void main(String[] args) {
        CardsRepositoryInterface cardRepository = new CardsJsonRepository();
        AIServiceInterface aiService = new GeminiService();

        CardService service = new CardService(cardRepository, aiService, new Sm2Scheduler());
        ConsoleCardView view = new ConsoleCardView();

        ConsoleCardController consoleCardController = new ConsoleCardController(service, view);

        consoleCardController.start(); // 35 cards
    }
}
