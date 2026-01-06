import java.time.LocalDate;

public class Quest {
    private String name;
    private String subject;
    private LocalDate deadline;
    private int estimatedTime;
    private int difficulty;
    private int progress;
    private String importance;
    private String location;

    // Class of quest.
    public Quest(String name, String subject, LocalDate deadline, int estimatedTime, int difficulty, int progress, String importance, String location) {
        this.name = name;
        this.subject = subject;
        this.deadline = deadline;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
        this.progress = progress;
        this.importance = importance;
        this.location = location;
    }

    // Functions to get information from the class.
    public String getName() {return name;}
    public String getSubject() {return subject;}
    public LocalDate getDeadline() {return deadline;}
    public int getEstimatedTime() {return estimatedTime;}
    public int getDifficulty() {return difficulty;}
    public int getProgress() {return progress;}
    public String getImportance() {return importance;}
    public int rarityWeight() {
        switch (importance) {
            case "#FF8000":
                return 5;
            case "#CC00FF":
                return 2;
            default:
                return 1;
        }
    }

}
