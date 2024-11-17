package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    /** Adds a hexagon with side length s to the given position (X, Y) in the world
     * Note the position (X, Y) refers to the top left corner of the squared area
     * @param world: Tile[][]
     *             (X, Y): Bottom left corner of the hexagon
     *             tile: The model of the tile
     */
    public static void addHexagon(TETile[][] world, int X, int Y, int s, TETile tile) {
        for (int row = 0; row < 2 * s; row++) {
            drawRow(world, X, Y, s, row, tile);
        }
    }

    /**
     * Draw a single line of the hexagon
     * @param world: Tile array
     * @param X: X
     * @param Y: Y
     * @param s: side length
     * @param row: current row
     * @param tile: tile to be rendered
     */
    private static void drawRow(TETile[][] world, int X, int Y, int s, int row, TETile tile) {
        int blanks;
        int elements;

        if (row < s) {
            // Lower half
            blanks = s - 1 - row;
            elements = row * 2 + s;
        } else {
            // Upper half
            blanks = row - s;
            elements = 5 * s - 2 * row - 2;
        }

        for (int col = 0; col < elements; col++) {
            world[X + blanks + col][Y + row] = tile;
        }
    }

    private static void hexWorldDemo(int s) {
        int width = 11 * s - 6;
        int height = 10 * s;
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        TETile[][] world = new TETile[width][height];
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        for (int i = 0; i < 5; i++) {
            int X = i * (2 * s - 1);
            int order = Math.abs(i - 2);
            int Y = order * s;
            drawHexagonColumn(world, X, Y, s, 5 - order);
        }
        ter.renderFrame(world);
    }

    /**
     * Draw a column of hexagons onto the canvas
     * @param world: Tile map
     * @param X
     * @param Y
     * @param s: side length
     * @param n: Number of hexagons
     */
    private static void drawHexagonColumn(TETile[][] world, int X, int Y, int s, int n) {
        for (int i = 0; i < n; i++) {
            int r = RANDOM.nextInt(4);
            TETile t = switch (r) {
                case 0 -> Tileset.MOUNTAIN;
                case 1 -> Tileset.SAND;
                case 2 -> Tileset.GRASS;
                case 3 -> Tileset.TREE;
                default -> Tileset.NOTHING;
            };
            addHexagon(world, X, Y + i * 2 * s, s, t);

        }
    }

    public static void main (String[] args) {
        hexWorldDemo(4);
    }
}
