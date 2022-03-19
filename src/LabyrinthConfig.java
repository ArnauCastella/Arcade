import edu.salleurl.arcade.labyrinth.model.enums.Cell;
import edu.salleurl.arcade.labyrinth.model.enums.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LabyrinthConfig implements Comparable<LabyrinthConfig> {
    private final List<Direction> config;
    private int i, j; //Partial position
    private int exitI, exitJ;
    private final boolean[][] visited;

    public LabyrinthConfig(Cell[][] labyrinth) {
        config = new ArrayList<>();
        visited = new boolean[labyrinth.length][labyrinth.length];

        boolean found = false;
        for(i = 0; i < labyrinth.length && !found; ++i) {
            for(j = 0; j < labyrinth[i].length && !found; ++j) {
                if (labyrinth[i][j] == Cell.START) {
                    found = true;
                }
            }
        }
        i--;
        j--;
        visited[i][j] = true;

        found = false;
        for(exitI = 0; exitI < labyrinth.length && !found; ++exitI) {
            for(exitJ = 0; exitJ < labyrinth[exitI].length && !found; ++exitJ) {
                if (labyrinth[exitI][exitJ] == Cell.EXIT) {
                    found = true;
                }
            }
        }
        this.exitJ--;
        this.exitI--;
    }

    private LabyrinthConfig(LabyrinthConfig that) {
        this.config = new ArrayList<>(that.config);
        this.i = that.i;
        this.j = that.j;
        this.exitI = that.exitI;
        this.exitJ = that.exitJ;
        this.visited = Arrays.stream(that.visited).map(boolean[]::clone).toArray(boolean[][]::new);
    }

    public Iterable<LabyrinthConfig> expand() {
        Collection<LabyrinthConfig> successors = new ArrayList<>();

        for (int i = 0; i < Direction.values().length; i++) {
            LabyrinthConfig successor = new LabyrinthConfig(this);
            successor.config.add(Direction.values()[i]);
            switch (successor.config.get(successor.config.size() - 1)) {
                case UP -> successor.i--;
                case DOWN -> successor.i++;
                case LEFT -> successor.j--;
                case RIGHT -> successor.j++;
            }
            if (!successor.visited[successor.i][successor.j]) {
                successor.visited[successor.i][successor.j] = true;
                successors.add(successor);
            }
        }
        return successors;
    }

    //isSolution() implemented in LabyrinthValidator.validate()

    public boolean completable(Cell[][] labyrinth) {
        try {
            return labyrinth[this.i][this.j] != Cell.WALL;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public int estimate() {
        return Math.abs(exitI - i) + Math.abs(exitJ - j);
    }

    @Override
    public int compareTo(LabyrinthConfig that) {
        return this.estimate() - that.estimate();
    }

    public List<Direction> getConfig() {
        return config;
    }

    public boolean isAtEnd(Cell[][] labyrinth) {
        return labyrinth[i][j] == Cell.EXIT;
    }
}