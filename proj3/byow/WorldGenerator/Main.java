package byow.WorldGenerator;

import byow.TileEngine.*;

public class Main {
    public static void main(String[] args) {
        int width = 40;
        int height = 40;
        TETile[][] world = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        TERenderer render = new TERenderer();
        render.initialize(width, height);
        RoomGenerator ter = new RoomGenerator(world, width, height);
        ter.addRectangularRoom(0, 0, 20, 20);
        ter.addRectangularRoom(20, 20, 5, 5);
        ter.addRectangularRoom(30, 30, 1, 1);
        ter.addRectangularRoom(30, 0, 7, 3);
        render.renderFrame(world);
    }
}
