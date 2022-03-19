import edu.salleurl.arcade.words.controller.WordsRenderer;
import edu.salleurl.arcade.words.model.WordsSolver;
import edu.salleurl.arcade.words.model.WordsValidator;

import java.util.Scanner;

public class DemoWordsSolver implements WordsSolver {

    private boolean viable(int[] config, String s) {
        if (Math.abs(config[0] - config[2]) == Math.abs(config[1] - config[3])) {
            //Diagonal
            if ((s.length()-1 == config[2]-config[0]) || (s.length()-1 == config[3]-config[1])) {
                return config[1] < config[3];
            } else {
                return false;
            }
        } else {
            if ((s.length()-1 == config[2]-config[0]) || (s.length()-1 == config[3]-config[1])) {
                return config[0] <= config[2] && config[1] <= config[3];
            } else {
                return false;
            }
        }
    }

    private boolean completable(int[] config, char[][] chars, String s, int k) {
        if (k >= 1) {
            return chars[config[0]][config[1]] == s.charAt(0);
        } else {
            return true;
        }
    }

    private void backtracking(int[] config, int k, char[][] chars, String s) throws SolutionFoundException {
        config[k] = 0;
        while (config[k] < chars.length) {
            if (k == config.length - 1) {
                //Possible solution
                if (viable(config, s)) {
                    if (WordsValidator.validate(chars, s, config)) {
                        System.out.println("Solution found");
                        throw new SolutionFoundException();
                    }
                }
            } else {
                // Non-solution case
                //Pruning
                if (completable(config, chars, s, k)) {
                    try {
                        backtracking(config, k + 1, chars, s);
                    } catch (SolutionFoundException e){
                        throw new SolutionFoundException();
                    }
                }
            }
            config[k]++;
        }
    }

    private int[] greedy(char[][] chars, String s) {
        for (int row1 = 0; row1 < chars.length; row1++) {
            for (int col1 = 0; col1 < chars.length; col1++) {
                if (chars[row1][col1] == s.charAt(0)) {
                    for (int row2 = row1; row2 < chars.length; row2++) {
                        for (int col2 = col1; col2 < chars.length; col2++) {
                            int[] config = {row1, col1, row2, col2};
                            if (viable(config, s)) {
                                if (WordsValidator.validate(chars, s, config)) {
                                    return config;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int[] solve(char[][] chars, String s, WordsRenderer wordsRenderer) {
        System.out.print("WORD SEARCH: Choose the algorithm you want to use (1 - Backtracking / 2 - Greedy): ");
        int input = new Scanner(System.in).nextInt();
        long startTime = System.nanoTime();
        switch (input) {
            case 1 -> {
                int[] config = new int[4];
                try {
                    backtracking(config, 0, chars, s);
                    return config;
                } catch (SolutionFoundException e) {
                    System.out.println("Solution found in " + (System.nanoTime()-startTime)/1000 + " microseconds.");
                    wordsRenderer.render(chars, s, config, 3000);
                    return config;
                }

            }
            case 2 -> {
                int[] config = greedy(chars, s);
                System.out.println("Solution found in " + (System.nanoTime()-startTime)/1000 + " microseconds.");
                wordsRenderer.render(chars, s, config, 3000);
                return config;
            }
            default -> {
                System.out.println("Introduce valid number (1 or 2)");
                return null;
            }
        }
    }
}
