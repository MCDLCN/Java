
# CrawlInMyDungeon

A **console-based dungeon crawler written in Java**, inspired by Dungeons & Dragons mechanics.  
The project focuses on clean architecture, separation of concerns, and extensible game systems.

---

## Features

- Turn-based **combat system**
- **Tile-based board** exploration
- **Inventory and equipment** management
- **Experience and leveling** system
- **Stat training and resting**
- **Character inspection**
- **Save / load persistence**
- Modular architecture with services and repositories

---

## UML Diagram

A UML class diagram describing the current architecture is provided as **PlantUML plaintext**.

File:

`chronicle_of_gaia.puml`

You can render it with:

- https://www.planttext.com/

---

## Project Architecture

The project is structured into clear layers:

| Layer | Responsibility |
|------|---------------|
| **Game** | Orchestrates gameplay and runtime flow |
| **GameSession** | Stores current play session state |
| **Board / Tiles** | World representation |
| **Entities** | Player and enemy logic |
| **Items / Inventory** | Equipment and consumables |
| **Services** | Combat, session management |
| **Repositories** | Persistence |
| **UI** | Console interaction |

---

## Project Structure

```
src/
 ├── main_logic/
 │    ├── Game.java
 │    ├── Board.java
 │    ├── Dice.java
 │
 │    ├── session/
 │    │    └── GameSession.java
 │
 │    ├── service/
 │    │    ├── CombatService.java
 │    │    ├── GameSessionService.java
 │    │    └── InventoryConsoleService.java
 │
 │    └── enums/
 │         ├── EncounterResult.java
 │         ├── EnemyType.java
 │         ├── Stat.java
 │         └── ItemCode.java
 │
 ├── model/
 │    ├── entities/
 │    │    ├── Creature.java
 │    │    ├── Equipment.java
 │    │    ├── Stats.java
 │    │    ├── classes/
 │    │    │    ├── PlayerCharacter.java
 │    │    │    ├── Warrior.java
 │    │    │    └── Wizard.java
 │    │    └── evilaaaneighbours/
 │    │         ├── Enemy.java
 │    │         ├── Goblin.java
 │    │         ├── Bandit.java
 │    │         └── EnemyMage.java
 │
 │    ├── inventory/
 │    │    ├── Inventory.java
 │    │    └── InventoryEntry.java
 │
 │    └── items/
 │         ├── Item.java
 │         ├── ItemFactory.java
 │         ├── ItemDefinition.java
 │         ├── consumables/
 │         ├── offensives/
 │         ├── defensives/
 │         └── scrolls/
 │
 ├── tiles/
 │    ├── Tile.java
 │    ├── EmptyTile.java
 │    ├── EnemyTile.java
 │    └── ChestTile.java
 │
 ├── persistence/
 │    ├── CharacterRepository.java
 │    ├── InventoryRepository.java
 │    ├── BoardRepository.java
 │    └── ClassRepository.java
 │
 └── ui/
      ├── Menu.java
      └── Console.java
```

---

## Gameplay Systems

### Combat

Combat is handled by `CombatService`.

Rules:

- Attack roll: `d20 + modifier`
- **Natural 20**
  - Automatic hit
  - Doubles **damage dice only**
- **Natural 1**
  - Automatic miss

Enemy attack modifiers:

| Enemy | Attack Stat |
|------|-------------|
| Goblin | DEX |
| Bandit | DEX |
| Enemy Mage | INT |

---

### Scroll Mechanics

- Scrolls are consumed on **every attack attempt**
- Scrolls disappear when uses reach **0**
- Scrolls are automatically **unequipped when depleted**

---

### Flee Mechanic

Escape attempt uses a contested roll:

```
Player: d20 + DEX
Enemy:  d20 + WIS
```

If successful:

- Player escapes combat

---

### Inventory System

Inventory distinguishes between:

| Type | Handling |
|----|-----------|
| Stackable items | Managed by item code |
| Non‑stackable items | Managed via `InventoryEntry` |

Selecting an equipped item **unequips it**.

Invalid actions **do not close the inventory menu**.

---

### Training System

Available on **Empty Tiles**.

Rules:

- Each stat requires **2 training actions** to increase
- Progress is stored **per stat**
- Stat cap = **20**
- Cancel option available

Training progress is persisted in saves.

---

### Rest System

Players may rest on empty tiles.

Healing:

```
Level × HitDie
```

HP is clamped to `maxHp`.

---

### Experience & Leveling

Enemies grant **XP when defeated**.

Leveling up:

- Recalculates **Max HP**
- Grants **2 stat points**

Stat points can be distributed freely (up to **20 per stat**).

Increasing **CON** recalculates max HP accordingly.

---

### Character Inspection

Available in the **post‑tile menu**.

Displays:

- Name
- Class
- Level
- HP / Max HP
- XP progress
- Stats
- Training progress

---

## Game Flow

```
Roll
 → Move
 → Tile Interaction
 → Combat / Rest / Train / Loot
 → Post‑Tile Menu
```

Post‑tile options:

- Manage inventory
- Inspect character
- Save game
- Return to main menu
- Quit game

---

## Win & Loss Conditions

### Victory

Player wins if they:

- Land on the final tile
- Overshoot the final tile

### Defeat

If the player dies:

- The game **does not autosave**
- The player may reload the previous save

---

## Build & Run

```
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out Main
```

---

## Development Status

### Implemented

- Board system
- Tile system
- Combat system
- Enemy types
- Inventory system
- Equipment system
- Scroll mechanics
- Rest system
- Training system
- XP & leveling
- Character inspection
- Save / load system
