import java.awt.desktop.SystemEventListener;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu {

    private Scanner scanner = new Scanner(System.in);

    public void showProblems() {
        System.out.println("Chose which problem do you want to solve");
        System.out.println("\t1. The wall of deadlines");
        System.out.println("\t2. The festival of infinite quests");

        System.out.print("Chose an option: ");
        try {
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    showTheWallOfDeadlines();
                    break;
                case 2:
                    showTheFestivalOfInfiniteQuests();
                    break;
                default:
                    System.out.println("Invalid intput Try again\n");
                    showProblems();
            }
        }
        catch (Exception e) {
            System.out.println("Invalid intput, must be a number\n");
        }
    }

    private void showTheWallOfDeadlines() {
        TheWallOfDeadlines theWallOfDeadlines = new TheWallOfDeadlines();
        System.out.println("\n---------|The Wall Of Deadlines|---------\n");
        System.out.print("Chose the time limit (in minutes): ");
        try {
            theWallOfDeadlines.maxTime = scanner.nextInt();
            theWallOfDeadlines.start();

        }catch (Exception e) {
           System.out.println("Invalid time");
        }

    }

    private void showTheFestivalOfInfiniteQuests() throws FileNotFoundException {
        TheFestivalOfQuests theFestivalOfQuests = new TheFestivalOfQuests();
        System.out.println("\n---------|The Festival Of Infinite Quests|---------\n");

        theFestivalOfQuests.start();
    }
}
