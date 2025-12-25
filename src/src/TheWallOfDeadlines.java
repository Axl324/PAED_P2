import java.io.FileNotFoundException;
import java.util.PriorityQueue;

public class TheWallOfDeadlines {
    int maxTime;
    int maxQuests;

    private void backtracking(Quest[] quests, int level, Node node) {
        if (!node.validTimeSameDay()) {
            return;
        }

        // Final call
        if (level == quests.length) {
            node.calculateTotalTime();
            if (node.timeWhitDiscount <= maxTime && node.questsCompleted > maxQuests) {
                maxQuests = node.questsCompleted;
                printConfig(node);
            }
            return;
        }

        // Skip quest
        backtracking(quests, level + 1, node);

        Quest quest = quests[level];

        int prevTime = node.time;
        int prevCompleted = node.questsCompleted;

        node.addToQuests(quest);
        node.updateTime(quest);
        node.updateQuestsCompleted(quest);

        backtracking(quests, level + 1, node);

        node.quests.removeLast();
        node.time = prevTime;
        node.questsCompleted = prevCompleted;
    }

    private void bruteForce(Quest[] quests, int level, Node node) {
        // Final call
        if (level == quests.length) {
            node.calculateTotalTime();
            if (node.timeWhitDiscount <= maxTime && node.questsCompleted > maxQuests && node.validTimeSameDay()) {
                maxQuests = node.questsCompleted;
                printConfig(node);
            }
            return;
        }

        // Skip quest
        bruteForce(quests, level + 1, node);

        // Store quest in a new node
        Quest quest = quests[level];
        Node newNode = new Node(node);

        newNode.addToQuests(quest);
        newNode.updateTime(quest);
        newNode.updateQuestsCompleted(quest);

        bruteForce(quests, level + 1, newNode);


        newNode.quests.removeLast();
    }

    private Node greedy(Quest[] quests) {
        Node config = new Node();

        preparation(quests);
       for (int i = 0; i < quests.length; i++) {

            config.addToQuests(quests[i]);
            config.updateTime(quests[i]);

            int totalTime = config.calculateTotalTime();


            if (totalTime > maxTime || !config.validTimeSameDay()) {
                config.quests.removeLast();
                config.time -= quests[i].getEstimatedTime();

            }
            else {
                config.timeWhitDiscount = totalTime;
                config.updateQuestsCompleted(quests[i]);
            }
        }
        printConfig(config);
        return config;
    }

    private void preparation(Quest[] quests) {
        //Insertion Sort
        for (int i = 1; i < quests.length; i++) {

            Quest currentQuest = quests[i];
            int j = i - 1;

            double currentEfficiency = (double) currentQuest.rarityWeight() / currentQuest.getEstimatedTime();

            while (j >= 0) {
                double compareEfficiency = (double) quests[j].rarityWeight() / quests[j].getEstimatedTime();
                if (
                        currentEfficiency > compareEfficiency ||
                        (currentEfficiency == compareEfficiency && quests[j].rarityWeight() < currentQuest.rarityWeight()) ||
                        (currentEfficiency == compareEfficiency && quests[j].rarityWeight() == currentQuest.rarityWeight() && quests[j].getEstimatedTime() > currentQuest.getEstimatedTime())
                ) {
                    quests[j + 1] = quests[j];
                    j--;
                }
                else {
                    break;
                }
            }

            quests[j + 1] = currentQuest;
        }
    }

    private void printConfig(Node node) {

        System.out.println("\n--------------------------");
        for (int i = 0; i < node.quests.size(); i++) {
            System.out.println(node.quests.get(i).getEstimatedTime() +
                    "  \t|    " + node.quests.get(i).getSubject() +
                    "    |    " + node.quests.get(i).getName());
        }
        System.out.println("\nTotal time: " + node.timeWhitDiscount + "\nQuest completed: " + node.questsCompleted);
        System.out.println("--------------------------");
    }

    public void start() throws FileNotFoundException {
        Node node = new Node();
        Data data = new Data();
        Quest[] quests = data.creatListQuests();
        maxQuests = 0;

        greedy(quests);
    }
}
