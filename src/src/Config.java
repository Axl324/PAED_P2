import java.util.ArrayList;
import java.util.List;

public class Config implements Comparable<Config>{
    List<Node> weeks;
    int numOfWeeks;
    int totalQuests;

    public Config () {
        weeks = new ArrayList<>();
        numOfWeeks = 1;
        totalQuests = 0;

        // So weeks is not NULL
        weeks.add(new Node());
    }

    public Config (Config that) {
        this.weeks = new ArrayList<>();
        for (Node node : that.weeks) {
            this.weeks.add(new Node(node));
        }

        this.numOfWeeks = that.numOfWeeks;
        this.totalQuests = that.totalQuests;
    }

    public List<Config> expand (Quest[] quests) {
        List<Config> children = new ArrayList<>();

        for (Quest quest : quests) {
            if (!this.containsQuest(quest)) {
                Config next = new Config(this);

                Node lastWeek = next.weeks.get(next.weeks.size() - 1);

                if (lastWeek.time + quest.getEstimatedTime() <= 1200) {
                    // Add to current week
                    updateWeek(lastWeek, quest);
                    next.totalQuests++;

                } else {
                    // Add to next week
                    next.numOfWeeks++;
                    next.totalQuests++;
                    Node newWeek = new Node();

                    updateWeek(newWeek, quest);
                    next.weeks.add(newWeek);
                }

                children.add(next);
            }
        }

        return children;
    }

    private void updateWeek (Node week, Quest quest) {
        week.quests.add(quest);
        week.updateTime(quest);
        week.updateQuestsCompleted(quest);
    }

    private boolean containsQuest(Quest quest) {
        for (Node node : weeks) {
            if (node.quests.contains(quest)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFull (Quest[] quests) { return totalQuests == quests.length; }

    @Override
    public int compareTo(Config that) {
        return this.numOfWeeks - that.numOfWeeks;
    }
}
