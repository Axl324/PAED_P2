import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class TheFestivalOfQuests {
    List<Node> bestComb;            // Best combination of quests per week
    int bestWeekNum;
    int maxTime = 1200;

    public void start () throws FileNotFoundException {
        Data data = new Data();
        Quest[] quests = data.creatListQuests();

        bestComb = new ArrayList<>();
        bestComb.add(new Node());
        bestWeekNum = 1;
        //branchAndBound(quests);

        List<Quest> highImportance = new ArrayList<>();
        List<Quest> lowImportance = new ArrayList<>();

        for (Quest q : quests) {
            if (q.rarityWeight() == 2 || q.rarityWeight() == 5) {
                highImportance.add(q);
            } else {
                lowImportance.add(q);
            }
        }

        Quest[] high = highImportance.toArray(new Quest[0]);
        Quest[] low = lowImportance.toArray(new Quest[0]);

        greedy(high);
        greedy(low);

        printSolution();
    }

    private void greedy(Quest[] quests) {
        preparation(quests);

        for (Quest quest : quests) {
            Node bestWeek = findBestWeek(quest);

            if (bestWeek != null) {
                bestWeek.addToQuests(quest);
                bestWeek.updateTime(quest);
                bestWeek.updateQuestsCompleted(quest);
            } else {
                Node newWeek = new Node();
                newWeek.addToQuests(quest);
                newWeek.updateTime(quest);
                newWeek.updateQuestsCompleted(quest);

                bestComb.add(newWeek);
                bestWeekNum++;
            }

        }
    }

    private void preparation(Quest[] quests) {
        // Insertion Sort
        for (int i = 1; i < quests.length; i++) {
            Quest temp = quests[i];
            int j = i - 1;

            double tempEfficiency = (double) temp.rarityWeight() / temp.getEstimatedTime();

            while (j >= 0 && ((double) quests[j].rarityWeight() / quests[j].getEstimatedTime() < tempEfficiency)) {
                quests[j + 1] = quests[j];
                j--;
            }

            quests[j + 1] = temp;
        }
    }

    private Node findBestWeek(Quest quest) {
        Node bestWeek = null;
        int minRemainingTime = Integer.MAX_VALUE;

        for (Node week : bestComb) {
            if (quest.rarityWeight() == 1 && totalCommonQuest(week) >= 6) {
                continue;
            }

            int remainingTime = maxTime - week.time;

            if (quest.getEstimatedTime() <= remainingTime) {
                int timeAfter = remainingTime - quest.getEstimatedTime();

                if (timeAfter < minRemainingTime) {
                    minRemainingTime = timeAfter;
                    bestWeek = week;
                }
            }
        }

        return bestWeek;
    }

    private int totalCommonQuest (Node week) {
        int total = 0;

        for (Quest q : week.quests) {
            if (q.rarityWeight() == 1) {
                total++;
            }
        }

        return total;
    }
/*
    private void  branchAndBound(Quest[] quests) {
       PriorityQueue<Config> queue = new PriorityQueue<>(
               Comparator.comparing(
                       config -> config.numOfWeeks));

       Config first = new Config();
       queue.offer(first);

        while (!queue.isEmpty()){
            Config current = queue.poll();

            List<Config> children = current.expand(quests);

            for (Config child : children) {
                if (child.isFull(quests)) {
                    // Check for the number of weeks in total
                    if (checkPriority(child)) {
                        bestComb = child.weeks;
                        bestWeekNum = child.numOfWeeks;

                        //System.out.println("Best comb with " + bestWeekNum + " weeks");
                    }
                } else {
                    if (checkPriority(child)) {
                        queue.offer(child);
                    }
                }
            }
        }
    }

    private boolean checkPriority (Config current) {
        if (current.numOfWeeks < bestWeekNum) {
            return true;

        } else if (current.numOfWeeks == bestWeekNum) {
            for (int i = 0; i < current.numOfWeeks; i++) {
                if (current.weeks.get(i).questsCompleted < bestComb.get(i).questsCompleted) {
                    return true;
                }
            }
        }

        return false;
    }
*/
    private void printSolution() {
        if (bestComb.isEmpty()) {
            System.out.println("No solution found!");
            return;
        }

        System.out.println("=== BEST SOLUTION ===");
        System.out.println("Total weeks: " + bestWeekNum);

        for (int i = 0; i < bestComb.size(); i++) {
            Node week = bestComb.get(i);
            System.out.println("\nWeek " + (i + 1) + ":");
            System.out.println("  Time: " + week.time + " minutes");
            System.out.println("  Quests:");

            for (Quest q : week.quests) {
                System.out.println(q.getImportance() + "\t(" + q.getEstimatedTime() + " min) \t" + q.getName());
            }
        }
    }
}
