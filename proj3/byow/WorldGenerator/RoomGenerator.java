package byow.WorldGenerator;

import byow.TileEngine.*;
import java.util.HashSet;

public class RoomGenerator {

    private final TETile[][] world;
    private final int WIDTH; // The maximum width of the map
    private final int HEIGHT; // The maximum height of the map
    private final HashSet<Room> rooms; // Stores all the rooms in the map

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
        this.rooms = new HashSet<>();
    }

    /**
     * Add a rectangular room into the world
     * @param X: Should be non-negative
     * @param Y: Should be non-negative
     * @param width: Should be positive
     * @param height: Should be positive
     */
    public boolean addRectangularRoom(int X, int Y, int width, int height) {
        assert X >= 0 && Y >= 0: "The coordinate of the origin should be non-negative";
        assert width > 2 && height > 2: "The width and height should be bigger than 2";

        // Validate the width and height
        width = Math.min(width, WIDTH - X);
        height = Math.min(height, HEIGHT - Y);
        if (width <= 2 || height <= 2) {
            // This room cannot be generated
            return false;
        }

        // Check for overlapping
        Room newRoom = new Room(X, Y, width, height);
        if (overlapping(newRoom)) {
            // New room overlaps with one or more existing rooms
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

        // New room generated. Add it to the room set
        rooms.add(newRoom);
        return true;
    }

    private boolean overlapping(Room newRoom) {
        // Check boundaries, with each existing room
        for (Room existingRoom: rooms) {
            if (!(newRoom.X + newRoom.width <= existingRoom.X
                    || newRoom.Y + newRoom.height <= existingRoom.Y
                    || newRoom.X >= existingRoom.X + existingRoom.width
                    || newRoom.Y >= existingRoom.Y + existingRoom.height)) {
                return true;
            }
        }
        return false;
    }

    private class Room {
        int width;
        int height;
        int X;
        int Y;

        Room(int x, int y, int w, int h) {
            this.X = x;
            this.Y = y;
            this.width = w;
            this.height = h;
        }
    }
}
