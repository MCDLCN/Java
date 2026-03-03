# CrawlInMyDungeon (Java)

Dungeon crawler based on DND.

------------------------------------------------------------------------

## Architecture Overview

The project follows a separation of concerns:

-   **Game** → Orchestrates gameplay and owns runtime state.
-   **Board** → Represents the world (tiles only).
-   **Character hierarchy** → Player and enemies.
-   **Items hierarchy** → Equipment and consumables.
-   **Dice system** → General randomness vs combat damage.
-   **Menu / Console** → User interaction layer.

------------------------------------------------------------------------

## Project Structure

    src/
     ├── crawlinmydungeon/
     │    ├── Game.java        → Game loop, win logic, player position
     │    ├── Board.java       → Board generation (ArrayList<Tile>)
     │    ├── Dice.java        → General purpose dice
     │
     ├── entities/
     │    ├── Character.java   → Abstract base class
     │    ├── classes/
     │    │    ├── Wizard.java
     │    │    └── Warrior.java
     │    └── evilaaaneighbours/
     │         ├── Goblin.java
     │         └── Bandit.java
     │
     ├── tiles/
     │    ├── Tile.java        → Abstract tile
     │    ├── EmptyTile.java
     │    ├── EnemyTile.java
     │    └── ChestTile.java
     │
     ├── items/
     │    ├── OffensiveEquipment.java
     │    ├── DefensiveEquipment.java
     │    ├── armours/
     │    │    └── Armour.java
     │    ├── weapons/
     │    ├── potions/
     │
     ├── ui/
     │    ├── Menu.java
     │    └── Console.java

------------------------------------------------------------------------

### Board Representation

The board is implemented as:

``` java
ArrayList<Tile>
```

-   Board size is configurable via constructor.
-   Tiles are instantiated at game start.
-   Each tile defines its own behavior via polymorphism.

------------------------------------------------------------------------

### Movement & Win Logic

Movement flow:

1.  Player rolls using `Dice`.
2.  `Game` asks `Board` to calculate new position.
3.  If new position \> last tile index → `OutOfBoardException`.
4.  Overshoot = win.
5.  Exact landing on last tile = win.
6.  Otherwise, player interacts with tile.

The exception is intentionally used to represent a valid game-ending
state (overshoot victory).

------------------------------------------------------------------------

## Combat System (Planned)

Combat is not fully implemented yet.

Planned logic:

-   When entering an `EnemyTile`, combat begins.
-   Loop continues until:
    -   Enemy dies → tile becomes empty.
    -   Player flees successfully.
    -   Player dies → game over.

### Flee Mechanic (Planned)

-   Stealth check:
    -   Player DEX modifier
    -   Enemy WIS modifier
-   If flee succeeds:
    -   Player moves back 2 tiles.
-   If flee fails:
    -   Player skips next turn.

------------------------------------------------------------------------

## Inventory & Items

-   Player owns an inventory (collection of items).
-   Potions are stored and consumed manually.
-   Chests will:
    -   Pull random items from an item pool.
    -   Add them to the inventory.
-   Items can be offensive or defensive equipment.

------------------------------------------------------------------------

## Dice System

-   `Dice` → Generic dice roller (movement, checks).
-   `DamageDice` → Dedicated to combat damage/healing.

------------------------------------------------------------------------

## Current Gameplay Flow

1.  Game initializes:
    -   Board
    -   Player class
2.  Game loop:
    -   Roll
    -   Move
    -   Interact with tile
    -   Check win/death conditions
3.  Game ends when:
    -   Player reaches or overshoots final tile
    -   Player dies

------------------------------------------------------------------------

## Build & Run

``` bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out Main
```

------------------------------------------------------------------------

## Status

✔ Board movement working\
✔ Win condition implemented (exact + overshoot)\
✔ OOP hierarchy structured\
✔ ArrayList-based board\
⬜ Combat system\
⬜ Loot generation system\
⬜ Flee mechanics\
⬜ Inventory UI
