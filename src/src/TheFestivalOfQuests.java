import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class TheFestivalOfQuests {
    List<Node> bestComb;            // Best combination of quests per week
    int bestWeekNum;

    public void start () throws FileNotFoundException {
        Data data = new Data();
        Quest[] quests = data.creatListQuests();

        bestComb = new ArrayList<>();
        bestWeekNum = Integer.MAX_VALUE;

        branchAndBound(quests);

        printSolution();
    }

    private void  branchAndBound(Quest[] quests) {
       PriorityQueue<Config> queue = new PriorityQueue<>(Comparator.comparing(config -> config.numOfWeeks));

       Config first = new Config();
       queue.offer(first);

        while (!queue.isEmpty()){
            Config current = queue.poll();

            List<Config> children = current.expand(quests);

            for (Config child : children) {
                if (child.isFull(quests)) {

                    // Check for the number of weeks in total
                    if (child.numOfWeeks <= bestWeekNum) {
                        bestComb = child.weeks;
                        bestWeekNum = child.numOfWeeks;

                        System.out.println("Best comb with " + bestWeekNum + " weeks");
                    }
                } else {
                    if (child.numOfWeeks <= bestWeekNum) {
                        queue.offer(child);
                    }
                }
            }
        }
    }

    private void printSolution() {
        if (bestComb.isEmpty()) {
            System.out.println("No solution found!");
            return;
        }

        System.out.println("\n=== BEST SOLUTION ===");
        System.out.println("Total weeks: " + bestWeekNum);

        for (int i = 0; i < bestComb.size(); i++) {
            Node week = bestComb.get(i);
            System.out.println("\nWeek " + (i + 1) + ":");
            System.out.println("  Time: " + week.time + " minutes");
            System.out.println("  Quests:");

            for (Quest q : week.quests) {
                System.out.println("    - " + q.getName() + " (" + q.getEstimatedTime() + " min)");
            }
        }
    }
}
