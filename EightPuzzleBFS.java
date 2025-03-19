// javac EightPuzzleBFS.java && java EightPuzzleBFS

import java.util.*;

class EightPuzzleBFS {
    private static final int[][] GOAL_STATE = {
        {0, 1, 2},
        {3, 4, 5},
        {6, 7, 8}
    };

    private static final int[][] DIRECTIONS = {
        {0, -1}, {0, 1}, {-1, 0}, {1, 0}
    };

    static class Node {
        int[][] state;
        Node parent;
        String move;
        int depth;

        Node(int[][] state, Node parent, String move, int depth) {
            this.state = state;
            this.parent = parent;
            this.move = move;
            this.depth = depth;
        }

        boolean isGoal() {
            return Arrays.deepEquals(this.state, GOAL_STATE);
        }

        String getStateKey() {
            StringBuilder key = new StringBuilder();
            for (int[] row : state) {
                for (int num : row) {
                    key.append(num).append(',');
                }
            }
            return key.toString();
        }
    }

    public static void bfs(int[][] initialState) {
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        Node root = new Node(initialState, null, null, 0);
        queue.add(root);
        visited.add(root.getStateKey());

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (currentNode.isGoal()) {
                printSolution(currentNode);
                return;
            }

            for (int[] direction : DIRECTIONS) {
                Node childNode = move(currentNode, direction);
                if (childNode != null && !visited.contains(childNode.getStateKey())) {
                    queue.add(childNode);
                    visited.add(childNode.getStateKey());
                }
            }
        }

        System.out.println("No solution found.");
    }

    private static Node move(Node node, int[] direction) {
        int[][] newState = copyState(node.state);
        int[] blankPosition = findBlank(newState);
        int newRow = blankPosition[0] + direction[0];
        int newCol = blankPosition[1] + direction[1];

        if (newRow >= 0 && newRow < 3 && newCol >= 0 && newCol < 3) {
            newState[blankPosition[0]][blankPosition[1]] = newState[newRow][newCol];
            newState[newRow][newCol] = 0;
            return new Node(newState, node, getDirectionName(direction), node.depth + 1);
        }

        return null;
    }

    private static int[] findBlank(int[][] state) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private static int[][] copyState(int[][] state) {
        int[][] newState = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(state[i], 0, newState[i], 0, 3);
        }
        return newState;
    }

    private static String getDirectionName(int[] direction) {
        if (Arrays.equals(direction, new int[]{0, -1})) return "Left";
        if (Arrays.equals(direction, new int[]{0, 1})) return "Right";
        if (Arrays.equals(direction, new int[]{-1, 0})) return "Up";
        if (Arrays.equals(direction, new int[]{1, 0})) return "Down";
        return "";
    }

    private static void printSolution(Node node) {
        if (node == null) return;
        printSolution(node.parent);
        if (node.move != null) {
            System.out.println("Move: " + node.move);
            printState(node.state);
        }
    }

    private static void printState(int[][] state) {
        for (int[] row : state) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[][] initialState = {
            {8, 0, 6},
            {5, 4, 7},
            {2, 3, 1}
        };

        bfs(initialState);
    }
}   