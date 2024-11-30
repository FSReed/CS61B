package byow.WorldGenerator;

import byow.TileEngine.*;
import java.util.Set;

public class RoomGenerator {

    /**
     * Add a rectangular room into the world
     * @param X: Should be non-negative
     * @param Y: Should be non-negative
     * @param width: Should be positive
     * @param height: Should be positive
     */
    public boolean addRectangularRoom(int X, int Y, int width, int height, int WIDTH, int HEIGHT,
                                      TETile[][] world, Set<Room> existingRooms) {
        // Parameters should be reasonable
        assert X >= 0 && Y >= 0: "The coordinate of the origin should be non-negative";
        assert width > 2 && height > 2: "The width and height should be bigger than 2";

        // Validate the width and height
        width = Math.min(width, WIDTH - X);
        height = Math.min(height, HEIGHT - Y);
        if (width <= 2 || height <= 2) {
            // This room cannot be generated
            return false;
        }

        if (overlapping(X, Y, width, height, existingRooms)) {
            return false;
        }

        // Build walls
        // Horizontal
        for (int i = X; i < X + width; i++) {
            world[i][Y] = Tileset.WALL;
            world[i][Y + height - 1] = Tileset.WALL;
        }
        // Vertical
        for (int j = Y + 1; j < Y + height; j++) {
            world[X][j] = Tileset.WALL;
            world[X + width - 1][j] = Tileset.WALL;
        }

        // Make floors
        for (int i = X + 1; i < X + width - 1; i++) {
            for (int j = Y + 1; j < Y + height - 1; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }

        // Update existing rooms:
        System.out.printf("A new room with size (%d, %d) was generated at (%d, %d)\n", width, height, X, Y);
        existingRooms.add(new Room(X, Y, width, height));

        return true;
    }

    private boolean overlapping(int X, int Y, int width, int height, Set<Room> existingRooms) {
        // Check boundaries, with each existing room
        for (Room room: existingRooms) {
            if (!(X + width <= room.X
                    || Y + height <= room.Y
                    || X >= room.X + room.width
                    || Y >= room.Y + room.height)) {
                return true;
            }
        }
        return false;
    }
}
