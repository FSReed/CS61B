package byow.WorldGenerator;

import byow.TileEngine.*;

public class RoomGenerator {

    private final TETile[][] world;
    private final int WIDTH;
    private final int HEIGHT;

    /** The constructor of Room Generator
     * It will modify the original TETile array
     * @param world: Given world array
     * @param w: width
     * @param h: height
     */
    public RoomGenerator(TETile[][] world, int w, int h) {
        this.world = world;
        this.WIDTH = w;
        this.HEIGHT = h;
    }

    /**
     * Add a rectangular room into the world
     * @param X: Should be non-negative
     * @param Y: Should be non-negative
     * @param width: Should be positive
     * @param height: Should be positive
     */
    public void addRectangularRoom(int X, int Y, int width, int height) {
        assert X >= 0 && Y >= 0: "The coordinate of the origin should be non-negative";
        assert width > 0 && height > 0: "Room's width and height should be positive";

        // Validate the width and height
        width = Math.min(width, WIDTH - X - 2);
        height = Math.min(height, HEIGHT - Y - 2);
        if (width <= 0 || height <= 0) {
            // Just return, don't generate this room
            return;
        }

        // Build walls
        // Horizontal
        for (int i = X; i <= X + width + 1; i++) {
            world[i][Y] = Tileset.WALL;
            world[i][Y + height + 1] = Tileset.WALL;
        }
        // Vertical
        for (int j = Y + 1; j <= Y + height; j++) {
            world[X][j] = Tileset.WALL;
            world[X + width + 1][j] = Tileset.WALL;
        }

        // Make floors
        for (int i = X + 1; i <= X + width; i++) {
            for (int j = Y + 1; j <= Y + height; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }
}
