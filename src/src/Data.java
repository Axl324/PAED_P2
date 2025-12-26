import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Scanner;

public class Data {

    private static final String dataset = "Datasets/datasetXL.paed";

    public Quest parseQuest (String quest_info) {
        String[] info = quest_info.split(";");
        String[] date = info[2].split("-");

        return new Quest(
                info[0],
                info[1],
                LocalDate.parse(date[2] + "-" + date[1] + "-" + date[0]),
                Integer.parseInt(info[3]),
                Integer.parseInt(info[4]),
                Integer.parseInt(info[5]),
                info[6],
                info[7]
        );
    }

    public static int numQuests () throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(dataset));
        return  Integer.parseInt(scanner.nextLine());
    }

    // Function to create the array of Quests
    public Quest[] creatListQuests() throws FileNotFoundException{
        int i = 0;
        try {
            Scanner scanner = new Scanner(new File(dataset));
            int numQuests =  Integer.parseInt(scanner.nextLine());
            Quest[] quests = new Quest[numQuests];

            while (i < quests.length) {
                String aux = scanner.nextLine();
                quests[i] = parseQuest(aux);
                i++;
            }

            return quests;
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }

    // Function to display the quests from the simple algorithms.
    public void printQuests (Quest[] quests) {
        for (int i = 0; i < quests.length; i++) {
            System.out.println(quests[i].getDeadline() +" "+ quests[i].getName());
        }
    }

    // Function to display the quests from the recursive algorithms.
    public void printOtherQuests (Quest[] quests) {
        for (int  i = 0; i < quests.length; i++) {
            System.out.println(quests[i].getDifficulty() +"\t"+ quests[i].getProgress()+"\t"+ quests[i].getEstimatedTime() +"   \t "+ quests[i].getName());
        }
    }
}
