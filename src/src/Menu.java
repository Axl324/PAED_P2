import java.awt.desktop.SystemEventListener;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu {

    private Scanner scanner = new Scanner(System.in);

    public void showProblems() {
        System.out.println("Choose which problem do you want to solve");
        System.out.println("\t1. The wall of deadlines");
        System.out.println("\t2. The festival of infinite quests");

        System.out.print("Choose an option: ");
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
            showProblems();
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

        while (true) {
            System.out.println("\n---------|The Festival Of Infinite Quests|---------\n");
            System.out.println("Choose which strategy do you want to use:");
            System.out.println("\t1. Greedy");
            System.out.println("\t2. Backtracking");

            System.out.print("Choose an option: ");

            try {
                int option = scanner.nextInt();
                if (option == 1 || option == 2) {
                    theFestivalOfQuests.start(option);
                    break;
                } else {
                    System.out.println("Invalid intput\n");
                }
            } catch (Exception e) {
                System.out.println("Invalid intput, must be a number\n");
                scanner.nextLine();
            }
        }
    }
}
