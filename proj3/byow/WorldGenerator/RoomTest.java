package byow.WorldGenerator;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class RoomTest {

    private TERenderer ter;
    private RoomGenerator room;
    private int width;
    private int height;

    public void BasicRooms() {
        width = 40;
        height = 40;
        TETile[][] world = InitWorld(width, height);
        TERenderer ter = new TERenderer();
        RoomGenerator room = new RoomGenerator(world, width, height);
        ter.initialize(width, height);
        room.addRectangularRoom(0, 0, 20, 20);
        room.addRectangularRoom(22, 22, 5, 5);
        room.addRectangularRoom(30, 30, 1, 1);
        room.addRectangularRoom(22, 6, 12, 8);
        room.addRectangularRoom(30, 0, 7, 3);
        ter.renderFrame(world);
    }

    public void BigWorld() {
        width = 96;
        height = 54;
        TETile[][] world = InitWorld(width, height);
        room.addRectangularRoom(30, 30, 50, 50);
        room.addRectangularRoom(0, 39, 1, 1);
        room.addRectangularRoom(37, 0, 1, 5);
        ter.renderFrame(world);
    }

    public void RoomOutOfBoundsTest() {
        width = 40;
        height = 40;
        TETile[][] world = InitWorld(width, height);
        room.addRectangularRoom(30, 30, 50, 50);
        room.addRectangularRoom(0, 39, 1, 1);
        room.addRectangularRoom(37, 0, 1, 5);
        ter.renderFrame(world);
    }

    private TETile[][] InitWorld(int width, int height) {
        TETile[][] world = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        ter = new TERenderer();
        room = new RoomGenerator(world, width, height);
        ter.initialize(width, height);
        return world;
    }

    public static void main(String[] args) {
        RoomTest tester = new RoomTest();
//        tester.BasicRooms();
//        tester.RoomOutOfBoundsTest();
        tester.BigWorld();
    }
}
