import java.io.FileNotFoundException;

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

    public void startBruteForce() throws FileNotFoundException {
        Node node = new Node();
        Data data = new Data();
        Quest[] quests = data.creatListQuests();
        maxQuests = 0;

        bruteForce(quests, 0, node);
    }
}
