# Project 3 BYOW Doc (By: FSReed)

## Phase 1: World Generator

### Requirements:

- **Rooms**:
  - Random size
  - Random location
  - No rooms with no way to enter
  - At least some rooms should be rectangular. Other shapes are also welcomed.
- **Hallways**:
  - Be capable to generate turns(intersections)
  - Random location
  - With width of 1 or 2

> Besides, all rooms and hallways should be connected, and must have walls that are visually distinct from floors and unused spaces.

### Design:

1. Add a class: `RoomGenerator`, with one method `addRectangularRoom` according to the left bottom position, width and height of the room.
2. [TODO]
