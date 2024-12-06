package byow.WorldGenerator;

import byow.TileEngine.*;
import java.util.HashSet;

public class Game {
    /* Basic properties */
    private final int WIDTH; // The maximum width of the map
    private final int HEIGHT; // The maximum height of the map
    private boolean PAINTING;

    /* For Game Engine */
    private final TETile[][] world;
    private final TERenderer ter;

    /* For Room Generation */
    private final HashSet<Room> rooms;
    private final RoomGenerator roomGen;

    public Game(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.PAINTING = false;
        /* Generate the world tile map */
        world = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        this.ter = new TERenderer();

        /* Construct a room generator */
        this.rooms = new HashSet<>();
        this.roomGen = new RoomGenerator();

        /* TODO: Connect the rooms */
    }

    private void initializeRenderer() {
        ter.initialize(WIDTH, HEIGHT);
        this.PAINTING = true;
    }

    /**
     * Add a room at (X, Y) with size (width, height)
     * Currently, only rectangular rooms are supported
     */
    public boolean addRoom(int X, int Y, int width, int height) {
        return roomGen.addRectangularRoom(X, Y, width, height, WIDTH, HEIGHT, world, rooms);
    }

    public void drawMap() {
        if (!PAINTING) {
            this.initializeRenderer();
        }
        ter.renderFrame(world);
    }

    public static void main(String[] args) {
        int width = 90;
        int height = 45;
        Game game = new Game(width, height);
        game.addRoom(0, 0, 20, 20);
        game.drawMap();
    }
}
