# CrawlInMyDungeon (Java)

A small WIP “dungeon crawler” / board-based RPG prototype.

## Project structure

- `src/`
  - `crawlinmydungeon/`
    - `Game.java` — game orchestration (creates the board + the player)
    - `Board.java` — board generation + movement logic + clearing tiles
    - `Dice.java` — generic dice roller
  - `entities/`
    - `Character.java` — abstract base class for all characters (PC + NPC)
    - `classes/`
      - `Wizard.java` — player class (extends `Character`)
      - `Warrior.java` — player class (extends `Character`)
    - `evilaaaneighbours/`
      - `Goblin.java` — enemy (extends `Character`)
      - `Bandit.java` — enemy (extends `Character`)
  - `items/`
    - `armours/`
      - `Armour.java` — armour item model

## Current gameplay idea

- The `Board` is an array of tiles (encounters / chest / empty).
- The `Game` creates a `Board` and a player `Character`.
- Movement is intended to be driven by a roll (ex: via `Dice`), returning whatever is on the destination tile.

## Build & run (simple CLI)

From the project root:

```bash
# Compile all Java files into ./out
mkdir -p out
javac -d out $(find src -name "*.java")

# Run (Main is currently empty)
java -cp out Main
