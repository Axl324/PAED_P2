import java.io.FileNotFoundException;

public class TheWallOfDeadlines {

    private void recursive(int[] config, int level) throws FileNotFoundException {
        for (int i = 0; i < Data.numQuests(); i++) {
            if (level < 3) {
                config[level] = i;
                recursive(config, level + 1 );
            }
            else {
                printConfig(config);
                return;
            }
        }
    }

    private void printConfig(int[] config) {
        System.out.print("[ ");
        for (int i = 0; i < config.length; i++) {
            System.out.print(config[i] + " ");
        }
        System.out.print("]");
        System.out.println();
    }

    public void brutForce() throws FileNotFoundException {
        int[] config = new int[3];

        recursive(config, 0);
    }
}
