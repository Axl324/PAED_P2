import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
            node.timeWhitDiscount = node.calculateTotalTime();
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

    private void branchAndBound(Quest[] quests) {

        PriorityQueue<Node> configs = new PriorityQueue<>(Comparator.comparingDouble(n -> -n.estimatedQuestsCompleted));

        Node firstConfig = greedy(quests);
        printConfig(firstConfig);
        maxQuests = firstConfig.questsCompleted;

        Node root = new Node();
        root.level = 0;
        root.estimatedQuestsCompleted = estimateCost(quests, root);
        configs.add(root);

        while (!configs.isEmpty()) {
            Node config = configs.poll();

            if (config.estimatedQuestsCompleted > maxQuests) {
                if (config.level == quests.length) {
                    config.timeWhitDiscount = config.calculateTotalTime();
                    if (config.timeWhitDiscount <= maxTime && config.validTimeSameDay() && config.questsCompleted > maxQuests) {
                        maxQuests = config.questsCompleted;
                        printConfig(config);
                    }
                }
                else {
                    List<Node> childrens = expand(quests, config);

                    for (int i = 0; i < childrens.size(); i++) {
                        Node child = childrens.get(i);

                        child.estimatedQuestsCompleted = estimateCost(quests, child);

                        if (child.estimatedQuestsCompleted > maxQuests) {
                            configs.add(child);
                        }
                    }
                }
            }
        }
    }


    private int estimateCost(Quest[] quests, Node config) {
        int remaining = quests.length - config.level;
        return config.questsCompleted + remaining * 5;
    }

    private List<Node> expand (Quest[] quests, Node config) {
        List<Node> expandedConfig = new ArrayList<>();
        Quest quest = quests[config.level];

        Node auxConfig = new Node(config);
        auxConfig.addToQuests(quest);
        auxConfig.updateTime(quest);
        auxConfig.updateQuestsCompleted(quest);
        auxConfig.level++;
        auxConfig.timeWhitDiscount = auxConfig.calculateTotalTime();

        if (auxConfig.validTimeSameDay() && auxConfig.timeWhitDiscount <= maxTime) {
            expandedConfig.add(auxConfig);
        }

        Node skip = new Node(config);

        skip.level++;
        expandedConfig.add(skip);

        return expandedConfig;
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
        //printConfig(config);
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

        //backtracking(quests, 0, node);
        branchAndBound(quests);
    }
}
