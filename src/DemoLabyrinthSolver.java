import edu.salleurl.arcade.labyrinth.controller.LabyrinthRenderer;
import edu.salleurl.arcade.labyrinth.model.LabyrinthSolver;
import edu.salleurl.arcade.labyrinth.model.LabyrinthValidator;
import edu.salleurl.arcade.labyrinth.model.enums.Cell;
import edu.salleurl.arcade.labyrinth.model.enums.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class DemoLabyrinthSolver implements LabyrinthSolver {
    @Override
    public List<Direction> solve(Cell[][] labyrinth, LabyrinthRenderer labyrinthRenderer) {
        int best = Integer.MAX_VALUE;
        List<Direction> bestConfig = new ArrayList<>();
        PriorityQueue<LabyrinthConfig> queue = new PriorityQueue<>();

        LabyrinthConfig initial = new LabyrinthConfig(labyrinth);
        queue.offer(initial);

        while(!queue.isEmpty()) {
            LabyrinthConfig first = queue.poll();
            Iterable<LabyrinthConfig> successors = first.expand();
            for (LabyrinthConfig successor : successors) {
                if (LabyrinthValidator.validate(labyrinth, successor.getConfig())) {
                    if (successor.getConfig().size() < best) {
                        best = successor.getConfig().size();
                        bestConfig = new ArrayList<>(successor.getConfig());
                        System.out.println("Best one!");
                        labyrinthRenderer.render(labyrinth, bestConfig, 10000);
                    }
                } else {
                    if (successor.completable(labyrinth) && successor.getConfig().size() < best) {
                        queue.offer(successor);
                        labyrinthRenderer.render(labyrinth, successor.getConfig(), 100);
                    }
                }
            }
        }
        return bestConfig;
    }
}