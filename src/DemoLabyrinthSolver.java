import edu.salleurl.arcade.labyrinth.controller.LabyrinthRenderer;
import edu.salleurl.arcade.labyrinth.model.LabyrinthSolver;
import edu.salleurl.arcade.labyrinth.model.LabyrinthValidator;
import edu.salleurl.arcade.labyrinth.model.enums.Cell;
import edu.salleurl.arcade.labyrinth.model.enums.Direction;

import java.util.*;

public class DemoLabyrinthSolver implements LabyrinthSolver {

    private int minDist = Integer.MAX_VALUE;
    private List<Direction> minConfig;

    private void backtracking(LabyrinthConfigBT config, Cell[][] labyrinth, LabyrinthRenderer labyrinthRenderer) {
        boolean hasSuccessor = config.prepareLevel();
        while (hasSuccessor) {
            if (config.isAtEnd(labyrinth)) {
                if (LabyrinthValidator.validate(labyrinth, config.getConfig()) && config.getConfig().size() < minDist) {
                    minConfig = new ArrayList<>(config.getConfig());
                    minDist = minConfig.size();
                }
            } else {
                if (config.completable(labyrinth) && config.getConfig().size() < minDist) {
                    //labyrinthRenderer.render(labyrinth, config.getConfig(), 100);
                    backtracking(config, labyrinth, labyrinthRenderer);
                }
            }
            hasSuccessor = config.nextSuccessor();
        }
    }

    @Override
    public List<Direction> solve(Cell[][] labyrinth, LabyrinthRenderer labyrinthRenderer) {
        System.out.print("LABYRINTH: Choose the algorithm you want to use (1 - BnB / 2 - Backtracking): ");
        int input = new Scanner(System.in).nextInt();
        switch (input) {
            case 1 -> {
                long startTime = System.nanoTime();
                int best = Integer.MAX_VALUE;
                List<Direction> bestConfig = new ArrayList<>();
                PriorityQueue<LabyrinthConfig> queue = new PriorityQueue<>();

                LabyrinthConfig initial = new LabyrinthConfig(labyrinth);
                queue.offer(initial);

                while (!queue.isEmpty()) {
                    LabyrinthConfig first = queue.poll();
                    Iterable<LabyrinthConfig> successors = first.expand();
                    for (LabyrinthConfig successor : successors) {
                        if (successor.isAtEnd(labyrinth)) {
                            if (LabyrinthValidator.validate(labyrinth, successor.getConfig()) &&
                                successor.getConfig().size() < best) {
                                best = successor.getConfig().size();
                                bestConfig = new ArrayList<>(successor.getConfig());
                                System.out.println("Best one!");
                                //labyrinthRenderer.render(labyrinth, bestConfig, 3000);
                            }
                        } else {
                            if (successor.completable(labyrinth) && successor.getConfig().size() < best) {
                                queue.offer(successor);
                                //labyrinthRenderer.render(labyrinth, successor.getConfig(), 100);
                            }
                        }
                    }
                }
                System.out.println("Solution found in " + (System.nanoTime()-startTime)/1000 + " microseconds.");
                labyrinthRenderer.render(labyrinth, bestConfig, 3000);
                return bestConfig;
            }
            case 2 -> {
                long startTime = System.nanoTime();
                LabyrinthConfigBT config = new LabyrinthConfigBT(labyrinth);
                backtracking(config, labyrinth, labyrinthRenderer);
                System.out.println("Solution found in " + (System.nanoTime()-startTime)/1000 + " microseconds.");
                labyrinthRenderer.render(labyrinth, minConfig, 3000);
                return minConfig;
            }
            default -> {
                System.out.println("Introduce valid number (1 or 2)");
                return null;
            }
        }
    }
}