package models;

public class Task {
    private final String task;
    private final String answer;
    private final int level;

    public Task(String task, String answer, int level) {
        this.task = task;
        this.answer = answer;
        this.level = level;
    }

    public String getTask() {
        return task;
    }

    public String getAnswer() {
        return answer;
    }

    public String toString() {
        return task + " : " + answer;
    }

    public int getLevel() {
        return level;
    }
}
