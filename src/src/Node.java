import java.util.ArrayList;

public class Node {
    ArrayList<Quest> quests;
    int time;
    int questsCompleted;

    public Node(ArrayList<Quest> quests, int questsCompleted, int time) {
        this.quests = quests;
        this.questsCompleted = questsCompleted;
        this.time = time;
    }

    public void addToQuests (Quest quest) {
        this.quests.add(quest);
    }

    public void updateQuestsCompleted (int numQuestCompleted) {
        this.questsCompleted += numQuestCompleted;
    }

    public void updateTime (int timeToBeAdd) {
        this.time += timeToBeAdd;
    }
}
