package maze;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;
import java.util.Arrays;

public class BattleFieldGenerator {
    private Stack<Node> stack = new Stack<>();
    private Random rand = new Random();
    public static int[][] battleField;



    private static int dimension;

    private static final int OPEN_SPACE = 1;
    private static final int OBSTACLE = 2;
    private static final int HIGH_GROUND = 3;
    private static final int LOW_GROUND = 4;
    private static final int RIVER = 5;

    public int[][] cloneBattleField() {
        return battleField;
    }

    public static int getDimension() {
        return dimension;
    }
    public static void setDimension(int dimension) {
        BattleFieldGenerator.dimension = dimension;
    }

    public BattleFieldGenerator(int dim) {
        battleField = new int[dim][dim];
        dimension = dim;
    }

    public void generate() {
        generateBattleField();

        for(int i = 0; i<dimension; i++ ){
            for(int j = 0; j<dimension; j++){
                if(battleField[i][j] == 0){
                    battleField[i][j] = OPEN_SPACE;
                }
                Node node = new Node(i,j);
                checkAndConvertToOpenSpace(node);
            }
        }
    }

    public void generateBattleField() {
        stack.push(new Node(0, 0));
        while (!stack.empty()) {
            Node next = stack.pop();
            if (validNextNode(next)) {
                battleField[next.y][next.x] = OPEN_SPACE;
                ArrayList<Node> neighbors = findNeighbors(next);
                randomlyAddNodesToStack(neighbors);
                addRandomTerrain(next);
                expandTerrain(next);
            }
        }
    }

    private boolean validNextNode(Node node) {
        int numNeighboringOnes = 0;
        for (int y = node.y - 1; y <= node.y + 1; y++) {
            for (int x = node.x - 1; x <= node.x + 1; x++) {
                if (pointOnGrid(x, y) && (x != node.x || y != node.y) && battleField[y][x] == OPEN_SPACE) {
                    numNeighboringOnes++;
                }
            }
        }
        return numNeighboringOnes < 3 && battleField[node.y][node.x] != OPEN_SPACE;
    }

    private void randomlyAddNodesToStack(ArrayList<Node> nodes) {
        while (!nodes.isEmpty()) {
            int targetIndex = rand.nextInt(nodes.size());
            stack.push(nodes.remove(targetIndex));
        }
    }

    private ArrayList<Node> findNeighbors(Node node) {
        ArrayList<Node> neighbors = new ArrayList<>();
        for (int y = node.y - 1; y <= node.y + 1; y++) {
            for (int x = node.x - 1; x <= node.x + 1; x++) {
                if (pointOnGrid(x, y) && pointNotNode(node, x, y) && battleField[y][x] != OPEN_SPACE) {
                    neighbors.add(new Node(x, y));
                }
            }
        }
        return neighbors;
    }

    private boolean pointOnGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < dimension && y < dimension;
    }

    private boolean pointNotNode(Node node, int x, int y) {
        return x != node.x || y != node.y;
    }

    private void addRandomTerrain(Node node) {
        int terrainType = rand.nextInt(12);
        switch (terrainType) {
            case 0:
                battleField[node.y][node.x] = OBSTACLE;
                break;
            case 1:
                battleField[node.y][node.x] = HIGH_GROUND;
                break;
            case 2:
                battleField[node.y][node.x] = LOW_GROUND;
                break;
            case 3:
                battleField[node.y][node.x] = RIVER;
                break;
            default:
                battleField[node.y][node.x] = OPEN_SPACE;
                break;
        }
    }

    private void checkAndConvertToOpenSpace(Node node) {
        int terrainType = battleField[node.y][node.x];
        boolean shouldConvert = true;

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                // Skip the current node
                if (dx == 0 && dy == 0) continue;

                int nx = node.x + dx;
                int ny = node.y + dy;

                // Check if any neighbor has the same terrain type
                if (pointOnGrid(nx, ny) && battleField[ny][nx] == terrainType) {
                    shouldConvert = false;
                    break;
                }
            }
            // If we already found a matching neighbor, break out of the loop
            if (!shouldConvert) break;
        }

        // If no neighbors match, convert to open space
        if (shouldConvert) {
            battleField[node.y][node.x] = OPEN_SPACE;
        }
    }

    private void expandTerrain(Node node) {
        int terrainType = battleField[node.y][node.x];
        if (terrainType != OPEN_SPACE) {
            for (int i = 0; i < 20; i++) { // Try to expand 3 times
                int dx = rand.nextInt(3) - 1; // -1, 0, 1
                int dy = rand.nextInt(3) - 1; // -1, 0, 1
                int nx = node.x + dx;
                int ny = node.y + dy;
                if (pointOnGrid(nx, ny) && battleField[ny][nx] == OPEN_SPACE) {
                    battleField[ny][nx] = terrainType;
                }
            }
        }
    }

    public String getRawBattleField() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : battleField) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }

    public String getSymbolicBattleField() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                char symbol = ' ';
                switch (battleField[i][j]) {
                    case OPEN_SPACE:
                        symbol = ' ';
                        break;
                    case OBSTACLE:
                        symbol = 'X';
                        break;
                    case HIGH_GROUND:
                        symbol = 'H';
                        break;
                    case LOW_GROUND:
                        symbol = 'L';
                        break;
                    case RIVER:
                        symbol = '~';
                        break;
                    default:
                        break;
                }
                sb.append(symbol).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }



}
