import java.util.ArrayList;

public class Node {
    ArrayList<Quest> quests;
    int time;
    int questsCompleted;

    public Node () {
        this.quests = new ArrayList<>();
        this.time = 0;
        this.questsCompleted = 0;
    }

    public Node(Node that) {
        this.quests = that.quests;
        this.questsCompleted = that.questsCompleted;
        this.time = that.time;
    }

    public void addToQuests (Quest quest) {
        this.quests.add(quest);
    }

    public void updateQuestsCompleted (Quest quest) {
        if(quest.getImportance().equals("#4FD945")) {
            this.questsCompleted++;
        } else if(quest.getImportance().equals("#CC00FF")) {
            this.questsCompleted += 2;
        } else if (quest.getImportance().equals("#FF8000")) {
            this.questsCompleted += 5;
        }
    }

    public void updateTime (Quest quest) { this.time += quest.getEstimatedTime(); }

    public void calculateTotalTime () {
        float discount = 0;
        for(Quest q : this.quests) {
            for (Quest q2 : this.quests) {
                if (q.getSubject().equals(q2.getSubject()) && q != q2)  {
                    discount += (float) (q.getEstimatedTime() * 0.1);
                    break;
                }
            }
        }

        System.out.println("\nDiscounted time for next one: " + discount);
        this.time -= Math.round(discount);
    }
}
