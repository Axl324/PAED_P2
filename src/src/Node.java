import java.time.LocalDate;
import java.util.ArrayList;

public class Node {
    ArrayList<Quest> quests;
    int time;
    int timeWhitDiscount;
    int questsCompleted;

    public Node () {
        this.quests = new ArrayList<>();
        this.time = 0;
        this.timeWhitDiscount = 0;
        this.questsCompleted = 0;
    }

    public Node(Node that) {
        this.quests = that.quests;
        this.questsCompleted = that.questsCompleted;
        this.timeWhitDiscount = that.timeWhitDiscount;
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
        int subjectTime;
        int count;

        ArrayList<String> discountedSubjects = new ArrayList<>();
        this.timeWhitDiscount = this.time;
        for(Quest q : this.quests) {

            if (discountedSubjects.contains(q.getSubject())) {
                continue;
            }

            subjectTime = 0;
            count = 0;

            for (Quest q2 : this.quests) {
                if (q.getSubject().equals(q2.getSubject()))  {
                    subjectTime += q2.getEstimatedTime();
                    count++;
                }
            }

            if (count > 1) {
                discount += subjectTime * 0.1f;
                discountedSubjects.add(q.getSubject());
            }
        }

        //System.out.println("\nDiscounted time for next one: " + discount);
        this.timeWhitDiscount -= Math.round(discount);
    }

    public boolean validTimeSameDay() {
        int timeSameDay = 0;
        ArrayList<LocalDate> checkedDeadlines = new ArrayList<>();

        for (Quest q : this.quests) {
            timeSameDay = 0;

            if (checkedDeadlines.contains(q.getDeadline())) {
                continue;
            }

            for (Quest q2 : this.quests) {
                if (q.getDeadline().equals(q2.getDeadline())) {
                    timeSameDay+= q2.getEstimatedTime();
                }
            }

            if (timeSameDay > 480) {
                return false;
            }
            checkedDeadlines.add(q.getDeadline());
        }
        return true;
    }
}
