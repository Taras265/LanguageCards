package factory;

import model.Task;

public sealed interface TaskFactoryResult {
    record Success(Task task) implements TaskFactoryResult {}
    record NeedsExamples() implements TaskFactoryResult {}
    record NeedsMastery() implements TaskFactoryResult {}
}
