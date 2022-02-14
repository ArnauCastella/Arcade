import edu.salleurl.arcade.Arcade;
import edu.salleurl.arcade.ArcadeBuilder;

public class Main {
    public static void main(String[] args) {
        Arcade arcade = new ArcadeBuilder()
                .setLabyrinthColumns(24)
                .setLabyrinthRows(24)
                .setWordsColumns(12)
                .setWordsRows(12)
// Optional, to set a specific input instead of getting a random one
                .setSeed(40)
// DemoLabyrinthSolver implements LabyrinthSolver
                .setLabyrinthSolver(new DemoLabyrinthSolver())
// DemoWordsSolver implements WordsSolver
                .setWordsSolver(new DemoWordsSolver())
                .build();
        arcade.run();
    }
}
