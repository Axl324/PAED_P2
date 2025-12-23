import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main () {
        Data data = new Data();
        try {
            Quest[] quests = data.creatListQuests("Datasets/data.txt");
            data.printQuests(quests);
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file");
        }
    }
}
