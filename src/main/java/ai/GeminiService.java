package ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import java.util.ArrayList;
import java.util.HashSet;
import model.Card;

public class GeminiService implements AIServiceInterface {
    private HashSet<String> words = new HashSet<>();
    private final Client client = Client.builder()
            .apiKey(System.getenv("GEMINI_API_KEY")).build();
    // private final String model = "gemini-2.5-flash";
    private final String model = "gemini-2.5-flash-lite";

    private static final String createCardsPrompt =
            "Я буду кидать тебе слово или несколько слова на английском. " +
                    "Ты будешь кидать же сразу следующий ответ с новой строчки:\n" +
                    "word: что за слово (с маленькой буквы)\n" +
                    "meaning: объяснение слова на английском (максимально простым английским)\n" +
                    "example1: пример использования слова в предложении\n" +
                    "example2: пример использования слова в предложении\n" +
                    "example3: пример использования слова в предложении\n" +
                    "master1: пример задания, которое предлагает пользователю " +
                    "составить мини-рассказ с этим словом на определенную " +
                    "тему для закрепления материала. \n" +
                    "master2: пример задания, которое предлагает пользователю " +
                    "составить мини-рассказ с этим словом на определенную " +
                    "тему для закрепления материала. \n" +
                    "master3: пример задания, которое предлагает пользователю " +
                    "составить мини-рассказ с этим словом на определенную " +
                    "тему для закрепления материала. \n" +
                    "В конце три слеша (///) (разделитель карточек)\n" +
                    "Вот тебе список слов:\n";
    private static final String createExamplesPrompt =
            "Я буду кидать тебе слово на английском. " +
                    "Ты будешь кидать же сразу следующий ответ с новой строчки:\n" +
                    "example1: пример использования слова в предложении\n" +
                    "example2: пример использования слова в предложении\n" +
                    "example3: пример использования слова в предложении\n" +
                    "Не пиши название полей" +
                    "Вот тебе твое первое слово - ";
    private static final String createMasteriesPrompt =
            "Я буду кидать тебе слово на английском. " +
                    "Ты будешь кидать же сразу следующий ответ с новой строчки:\n" +
                    "master1: пример задания, которое предлагает пользователю " +
                    "составить мини-рассказ с этим словом на определенную " +
                    "тему для закрепления материала. \n" +
                    "master2: пример задания, которое предлагает пользователю " +
                    "составить мини-рассказ с этим словом на определенную " +
                    "тему для закрепления материала. \n" +
                    "master3: пример задания, которое предлагает пользователю " +
                    "составить мини-рассказ с этим словом на определенную " +
                    "тему для закрепления материала. \n" +
                    "Не пиши название полей." +
                    "Вот тебе пример для слова \"continue\": " +
                    "You are working on an important project " +
                    "when an unexpected problem arises. " +
                    "Describe the problem and how you and your team decide " +
                    "to continue working towards a solution despite the setback.\n" +
                    "Помни что тебе в итоге надо сделать 3 заданий, а не 1\n" +
                    "Вот тебе твое первое слово - ";

    public GeminiService() {}

    @Override
    public void addWord(String word) {
        words.add(word);
    }

    public boolean haveWords() {
        return !words.isEmpty();
    }

    @Override
    public ArrayList<Card> createCards() {
        StringBuilder prompt = new StringBuilder(createCardsPrompt);
        for (String word : words) {
            prompt.append(word).append("\n");
        }

        GenerateContentResponse response =
                client.models.generateContent(
                        model,
                        prompt.toString(),
                        null
                );
        String answer = response.text();

        words.clear();

        return stringToCards(answer);
    }

    @Override
    public ArrayList<String> createExamples(String word) {
        GenerateContentResponse response =
                client.models.generateContent(
                        model,
                        createExamplesPrompt + word,
                        null
                );
        String answer = response.text();
        System.out.println(answer);
        return stringToArray(answer);
    }

    @Override
    public ArrayList<String> createMasteries(String word) {
        GenerateContentResponse response =
                client.models.generateContent(
                        model,
                        createMasteriesPrompt + word,
                        null
                );
        String answer = response.text();
        return stringToArray(answer);
    }

    private ArrayList<Card> stringToCards(String str) {
        ArrayList<Card> cards = new ArrayList<>();
        String[] blocks = str.split("\\n///\\n");
        for (String block : blocks) {

            cards.add(stringToCard(block));
        }
        return cards;
    }

    private Card stringToCard(String str) {
        String word="", meaning="";
        ArrayList<String> examples = new ArrayList<>();
        ArrayList<String> mastery = new ArrayList<>();

        String[] lines = str.split("\\n");

        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("word:")) {
                word = line.substring("word:".length()).trim();
            }
            else if (line.startsWith("meaning:")) {
                meaning = line.substring("meaning:".length()).trim();
            }
            else if (line.startsWith("example1:")
                    || line.startsWith("example2:")
                    ||  line.startsWith("example3:")) {
                examples.add(line.substring("example1:".length()).trim());
            }
            else if (line.startsWith("master1:")
                    || line.startsWith("master2:")
                    ||  line.startsWith("master3:")) {
                mastery.add(line.substring("master1:".length()).trim());
            }
        }
        return new Card(word, meaning, examples, mastery);
    }

    private ArrayList<String> stringToArray(String str) {
        ArrayList<String> data = new ArrayList<>();

        String[] lines = str.split("\\n");

        for (String line : lines) {
            data.add(line.trim());
        }
        return data;
    }
}
