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
    public boolean addRectangularRoom(int X, int Y, int width, int height) {
        for (int i = X; i <= X + width + 1; i++) {
            world[i][Y] = Tileset.WALL;
            world[i][Y + height + 1] = Tileset.WALL;
        }

        for (int j = Y + 1; j <= Y + height; j++) {
            world[X][j] = Tileset.WALL;
            world[X + width + 1][j] = Tileset.WALL;
        }

        return true;
    }
}
