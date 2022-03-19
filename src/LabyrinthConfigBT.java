import edu.salleurl.arcade.labyrinth.model.enums.Cell;
import edu.salleurl.arcade.labyrinth.model.enums.Direction;

import java.util.ArrayList;
import java.util.List;

public class LabyrinthConfigBT {
    private final List<Direction> config;
    private int i, j; //Partial position
    private final boolean[][] visited;

    public LabyrinthConfigBT(Cell[][] labyrinth) {
        config = new ArrayList<>();
        visited = new boolean[labyrinth.length][labyrinth.length];

        boolean found = false;
        for(i = 0; i < labyrinth.length && !found; i++) {
            for(j = 0; j < labyrinth[i].length && !found; j++) {
                if (labyrinth[i][j] == Cell.START) {
                    found = true;
                }
            }

        }
        visited[--i][--j] = true;
    }

    //Backtracking
    public boolean viable (Cell[][] labyrinth) {
        try {
            return (!visited[i][j] && labyrinth[this.i][this.j] != Cell.WALL);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public boolean prepareLevel() {
        for (int i = 0; i < Direction.values().length; i++) {
            if (!alreadyVisited(Direction.values()[i])) {
                addDirection(Direction.values()[i]);
                return true;
            }
        }
        return false;
    }

    public void addDirection(Direction d) {
        config.add(d);
        switch (d) {
            case UP -> i--;
            case DOWN -> i++;
            case LEFT -> j--;
            case RIGHT -> j++;
        }
        visited[i][j] = true;
    }

    public void removeDirection() {
        Direction lastD = config.get(config.size()-1);
        visited[i][j] = false;
        switch (lastD) {
            case UP -> i++;
            case DOWN -> i--;
            case LEFT -> j++;
            case RIGHT -> j--;
        }
        config.remove(config.size()-1);
    }

    public boolean nextSuccessor() {
        Direction lastD = config.get(config.size()-1);
        removeDirection();
        if (lastD == Direction.values()[Direction.values().length-1]) {
            return false;
        } else {
            Direction nextD = lastD;
            int i;
            for (i = 0; i < Direction.values().length; i++) {
                if (Direction.values()[i] == lastD) {
                    nextD = Direction.values()[i+1];
                    break;
                }
            }
            i = Math.min(i+2, Direction.values().length-1);
            while (alreadyVisited(nextD)) {
                nextD = Direction.values()[i++];
                if (alreadyVisited(nextD) && nextD == Direction.values()[Direction.values().length-1]){
                    return false;
                }
            }
            addDirection(nextD);
            return true;
        }
    }

    private boolean alreadyVisited(Direction d) {
        int i = this.i;
        int j = this.j;
        switch (d) {
            case UP -> i--;
            case DOWN -> i++;
            case LEFT -> j--;
            case RIGHT -> j++;
        }
        return visited[i][j];
    }

    public List<Direction> getConfig() {
        return config;
    }

    public boolean isAtEnd(Cell[][] labyrinth) {
        return labyrinth[i][j] == Cell.EXIT;
    }

    public boolean completable(Cell[][] labyrinth) {
        try {
            return labyrinth[i][j] != Cell.WALL;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
