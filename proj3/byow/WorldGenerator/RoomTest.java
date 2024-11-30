package byow.WorldGenerator;

import java.util.Random;

public class RoomTest {

    private static int width;
    private static int height;

    public static void BasicRooms() {
        width = 40;
        height = 40;
        Game game = new Game(width, height);
        game.addRoom(0, 0, 20, 20);
        game.addRoom(22, 22, 5, 5);
        game.addRoom(30, 30, 3, 3);
        game.addRoom(22, 6, 12, 8);
        game.addRoom(30, 0, 7, 3);
        game.drawMap();
    }

    public static void RoomOutOfBoundsTest() {
        width = 40;
        height = 40;
        Game game = new Game(width, height);
        game.addRoom(30, 30, 50, 50);
        game.addRoom(0, 39, 3, 3);
        game.addRoom(37, 0, 3, 5);
        game.drawMap();
    }

    public static void BigWorld() {
        width = 96;
        height = 54;
        Game game = new Game(width, height);
        game.addRoom(30, 30, 50, 50);
        game.addRoom(0, 39, 3, 3);
        game.addRoom(37, 0, 3, 5);
        game.drawMap();
    }

    /**
     * Generate random rooms on the world map
     * @param iter: Used to test the efficiency of the algorithm
     * @param ROOMS: Max room number
     * @param ATTEMPT: Max attempt to make in each iteration
     * @param min_size: Minimum room width(height)
     * @param max_size: Maximum room width(height)
     */
    public static void RandomRooms(int iter, int ROOMS, int ATTEMPT, int min_size, int max_size) {
        width = 90;
        height = 45;
        int total_room = 0;
        int total_attempt = 0;
        while (iter > 0) {
            Random RANDOM = new Random();
            Game game = new Game(width, height);
            int rooms = 0;
            int attempts = 0;
            while (rooms < ROOMS && attempts < ATTEMPT) {
                int X = RANDOM.nextInt(width);
                int Y = RANDOM.nextInt(height);
                int width = RANDOM.nextInt(max_size) + min_size;
                int height = RANDOM.nextInt(max_size) + min_size;
                if (game.addRoom(X, Y, width, height)) {
                    rooms += 1;
                }
                attempts += 1;
            }
            System.out.printf("Use %d iterations to generate %d rooms\n", attempts, rooms);
            iter -= 1;
            total_room += rooms;
            total_attempt += attempts;
//            game.drawMap();
        }
        System.out.printf("Average efficiency: %.2f rooms/attempt\n", (double) total_room / total_attempt);
    }
}
