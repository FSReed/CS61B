package byow.WorldGenerator;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

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
        room.addRectangularRoom(30, 30, 3, 3);
        room.addRectangularRoom(22, 6, 12, 8);
        room.addRectangularRoom(30, 0, 7, 3);
        ter.renderFrame(world);
    }

    public void RoomOutOfBoundsTest() {
        width = 40;
        height = 40;
        TETile[][] world = InitWorld(width, height);
        room.addRectangularRoom(30, 30, 50, 50);
        room.addRectangularRoom(0, 39, 3, 3);
        room.addRectangularRoom(37, 0, 3, 5);
        ter.renderFrame(world);
    }

    public void BigWorld() {
        width = 96;
        height = 54;
        TETile[][] world = InitWorld(width, height);
        room.addRectangularRoom(30, 30, 50, 50);
        room.addRectangularRoom(0, 39, 3, 3);
        room.addRectangularRoom(37, 0, 3, 5);
        ter.renderFrame(world);
    }

    /**
     * Generate random rooms on the world map
     * @param iter: Used to test the efficiency of the algorithm
     * @param ROOMS: Max room number
     * @param ATTEMPT: Max attempt to make in each iteration
     * @param min_size: Minimum room width(height)
     * @param max_size: Maximum room width(height)
     */
    public void RandomRooms(int iter, int ROOMS, int ATTEMPT, int min_size, int max_size) {
        width = 90;
        height = 45;
        Random currentSeed = new Random();
        int total_room = 0;
        int total_attempt = 0;
        while (iter > 0) {
            TETile[][] world = InitWorld(width, height);
            Random RANDOM = new Random(currentSeed.nextInt());
            int rooms = 0;
            int attempts = 0;
            while (rooms < ROOMS && attempts < ATTEMPT) {
                int X = RANDOM.nextInt(width);
                int Y = RANDOM.nextInt(height);
                int width = RANDOM.nextInt(max_size) + min_size;
                int height = RANDOM.nextInt(max_size) + min_size;
                if (room.addRectangularRoom(X, Y, width, height)) {
                    rooms += 1;
                }
                attempts += 1;
            }
            System.out.printf("Use %d iterations to generate %d rooms\n", attempts, rooms);
//            ter.renderFrame(world);
            iter -= 1;
            total_room += rooms;
            total_attempt += attempts;
        }
        System.out.printf("Average efficiency: %.2f rooms/attempt\n", (double) total_room / total_attempt);
    }

    public void GenerateTunnel(int length) {
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
//        tester.BigWorld();
        tester.RandomRooms(100, 100, 30, 5, 10);
    }
}
