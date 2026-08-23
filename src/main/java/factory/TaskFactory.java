package factory;

import model.Card;
import model.Task;

import java.util.ArrayList;
import java.util.Collections;

public class TaskFactory {
    public static TaskFactoryResult getTask(Card card) {
        return switch (card.getLevel()) {
            case 1 -> new TaskFactoryResult.Success(createFirstLevelTask(card));
            case 2 -> createSecondLevelTask(card);
            case 3 -> createThirdLevelTask(card);
            default -> createFourthLevelTask(card);
        };
    }

    public static Task createFallbackTask(Card card) {
        return createFirstLevelTask(card);
    }

    private static Task createFirstLevelTask(Card card) {
        ArrayList<String> taskArguments = new ArrayList<>();
        taskArguments.add(card.getDescription());
        taskArguments.add(card.getWord());
        Collections.shuffle(taskArguments);
        return new Task(taskArguments.getFirst(), taskArguments.getLast(), 1);
    }

    private static TaskFactoryResult createSecondLevelTask(Card card) {
        String example = card.getExamples().getRandom();

        if (example == null) {
            return new TaskFactoryResult.NeedsExamples();
        }

        example = example + "\n\n" + "What does the word \"" +
                card.getWord() +
                "\" mean here?";
        return new TaskFactoryResult.Success(
                new Task(example, card.getDescription(), 2)
        );
    }

    private static TaskFactoryResult createThirdLevelTask(Card card) {
        String example = card.getExamples().getRandom();

        if (example == null) {
            return new TaskFactoryResult.NeedsExamples();
        }

        String word = card.getWord();

        example = example.replaceAll(word, "---");
        example = example + "\n\n" + card.getDescription();
        return new TaskFactoryResult.Success(
                new Task(example, card.getDescription(), 3)
        );
    }

    private static TaskFactoryResult createFourthLevelTask(Card card) {
        ArrayList<String> mastery = card.getMastery();
        if (mastery.isEmpty()) {
            return new TaskFactoryResult.NeedsMastery();
        }

        String m = mastery.getFirst();
        mastery.removeFirst();

        String task = "Create sentence with using word \"" + card.getWord() + "\"\n" + m;
        return new TaskFactoryResult.Success(
                new Task(task, "", 4)
        );
    }
}
