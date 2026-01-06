import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TheFestivalOfQuests {
    List<Node> bestComb;            // Weeks with the Best combination of quests
    int bestWeekNum;                // Best number of weeks
    int maxTime = 1200;             // Max time 20 hours
    int numIterations;

    public void start(int option) throws FileNotFoundException {
        Node node = new Node();
        Data data = new Data();
        Quest[] quests = data.creatListQuests();
        List<Node> currSolution = new ArrayList<>();
        currSolution.add(node);
        numIterations = 0;

        bestComb = new ArrayList<>();

        // Sort quests by importance (Legendary -> Epic -> Common)
        node.sortByImportance(quests);
        long start = System.nanoTime();

        switch(option) {
            case 1: // Greedy Algorithm
                bestComb.add(node);
                bestWeekNum = 1;

                greedy(quests);
                break;
            case 2: // Backtracking Algorithm
                bestWeekNum = Integer.MAX_VALUE;
                backtracking(quests, 0, currSolution);
                break;
            case 3: // Brute Force
                bestWeekNum = Integer.MAX_VALUE;
                bruteForce(quests, 0, currSolution);
                break;
        }

        long end = System.nanoTime();

        printSolution();
        System.out.println("\n========== ALGORITHM ANALYSIS ==========");
        System.out.println("\tTime: " + (end - start)/1000000 + " ms");
        System.out.println("\tNum Iterations: " + numIterations);
    }

    private void greedy(Quest[] quests) {
        // Sort quests by efficiency (Relation between importance and time)
        preparation(quests);

        //Analyze and prioritize important quests first
        for (Quest quest : quests) {
            numIterations++;
            // Find the best possible week to place the quest
            Node bestWeek = findBestWeek(quest);

            if (quest.rarityWeight() == 2 || quest.rarityWeight() == 5) {
                if (bestWeek != null) {
                    addQuestToWeek(bestWeek, quest);

                } else {
                    Node newWeek = new Node();
                    addQuestToWeek(newWeek, quest);

                    bestComb.add(newWeek);
                    bestWeekNum++;
                }
            }
        }

        // Analyze common quests
        for (Quest quest : quests) {
            numIterations++;
            Node bestWeek = findBestWeek(quest);

            if (quest.rarityWeight() == 1) {
                if (bestWeek != null) {
                    addQuestToWeek(bestWeek, quest);

                } else {
                    Node newWeek = new Node();
                    addQuestToWeek(newWeek, quest);

                    bestComb.add(newWeek);
                    bestWeekNum++;
                }
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
            // If common, check if week has less than 6
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

    private void backtracking(Quest[] quests, int level, List<Node> currSolution) {
        numIterations++;
        // Final call - Decide if the solution is better
        if (level >= quests.length) {
            if (currSolution.size() < bestWeekNum) {
                bestWeekNum = currSolution.size();

                bestComb.clear();
                for (Node node : currSolution) {
                    bestComb.add(new Node(node));
                }

                System.out.println("New best solution found with " + bestWeekNum + " weeks");
            }
            return;
        }

        // Prune if solution is worse
        if (currSolution.size() >= bestWeekNum) {
            return;
        }

        Quest currQuest = quests[level];

        //Prune if there are more epic/legendary quests
        if (currQuest.rarityWeight() == 1 && hasMoreImportantQuests(level, quests)) {
            return;
        }

        // Add to existing week
        for (int i = 0; i < currSolution.size(); i++) {
            Node week = currSolution.get(i);

            // Chek if time doesn't exceed
            if (week.time + currQuest.getEstimatedTime() <= maxTime) {
                // If common, check for number of common quests
                if (currQuest.rarityWeight() == 1 && totalCommonQuest(week) >= 6) {
                    continue;
                }

                addQuestToWeek(week, currQuest);
                backtracking(quests, level + 1, currSolution);

                // Remove quest
                week.quests.removeLast();
                week.time -= currQuest.getEstimatedTime();
                week.questsCompleted -= currQuest.rarityWeight();
            }
        }

        // Create a new week
        if (currSolution.size() + 1 < bestWeekNum) {
            Node newWeek = new Node();

            addQuestToWeek(newWeek, currQuest);
            currSolution.add(newWeek);
            backtracking(quests, level + 1, currSolution);
            currSolution.removeLast();
        }
    }

    private boolean hasMoreImportantQuests (int level, Quest[] quests) {
        for (int i = level + 1; i < quests.length; i++) {
            if (quests[i].rarityWeight() == 2 || quests[i].rarityWeight() == 5) {
                return true;
            }
        }

        return false;
    }

    private void addQuestToWeek (Node week, Quest quest) {
        week.addToQuests(quest);
        week.updateTime(quest);
        week.updateQuestsCompleted(quest);
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


    private void bruteForce (Quest[] quests, int level, List<Node> currSolution) {
        numIterations++;
        // Final call - Decide if the solution is better
        if (level >= quests.length) {
            if (currSolution.size() < bestWeekNum) {
                bestWeekNum = currSolution.size();

                bestComb.clear();
                for (Node node : currSolution) {
                    bestComb.add(new Node(node));
                }

                System.out.println("New best solution found with " + bestWeekNum + " weeks");
            }
            return;
        }

        Quest currQuest = quests[level];

        // Add to existing week
        for (int i = 0; i < currSolution.size(); i++) {
            Node week = currSolution.get(i);

            // Chek if time doesn't exceed
            if (week.time + currQuest.getEstimatedTime() <= maxTime) {
                // If common, check for number of common quests
                if(!(currQuest.rarityWeight() == 1 && totalCommonQuest(week) >= 6)) {
                    addQuestToWeek(week, currQuest);
                    bruteForce(quests, level + 1, currSolution);

                    // Remove quest
                    week.quests.removeLast();
                    week.time -= currQuest.getEstimatedTime();
                    week.questsCompleted -= currQuest.rarityWeight();
                }
            }
        }

        // Create new week
        Node newWeek = new Node();

        addQuestToWeek(newWeek, currQuest);
        currSolution.add(newWeek);
        bruteForce(quests, level + 1, currSolution);
        currSolution.removeLast();
    }

    private void printSolution() {
        if (bestComb.isEmpty()) {
            System.out.println("\nNo solution found!");
            return;
        }

        System.out.println("\n=== BEST SOLUTION ===");
        System.out.println("Total weeks: " + bestWeekNum);

        for (int i = 0; i < bestComb.size(); i++) {
            Node week = bestComb.get(i);
            System.out.println("\nWeek " + (i + 1) + ":");
            System.out.println("\tTime: " + week.time + " minutes");
            System.out.println("\tQuests:");

            for (Quest q : week.quests) {
                System.out.println(q.getImportance() + "\t(" + q.getEstimatedTime() + " min) \t" + q.getName());
            }
        }
    }
}
