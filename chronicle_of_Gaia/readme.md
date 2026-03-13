# Chronicle of Gaia

Chronicle of Gaia is a **console-based dungeon crawler written in Java**, inspired by the mechanics of **Dungeons & Dragons**.
The project focuses on clean architecture, object‑oriented design, and modular gameplay systems.

---

# Running the Game

## Requirements

- Java JDK 17+
- MySQL 8+
- An IDE such as IntelliJ IDEA or Eclipse

---

## Database configuration

Create a `db.properties` file for the database connection.

Example:

db.url=jdbc:mysql://localhost:3306/chronicle_of_gaia

db.user=root

db.password=your_password

db.driver=com.mysql.cj.jdbc.Driver

Place this file inside the `src` folder.

Do not commit real credentials if the repository is public.

---

## Compile

mkdir -p out
javac -d out $(find src -name "*.java")

---

## Run

java -cp out Main

You can also run the `Main` class directly from your IDE.

---

# Game Features

## Character Creation

Players can choose between different classes:

Warrior
- strong melee fighter
- higher survivability

Wizard
- magic oriented gameplay
- uses scrolls and intelligence‑based attacks

Each class defines its own equipment compatibility.

---

## Game Board

The world is represented by a **linear board composed of tiles**.

Tile types:

EmptyTile  
EnemyTile  
ChestTile  

The player moves forward based on dice rolls and interacts with the tile reached.

Victory occurs when the player:
- reaches the final tile
- overshoots the final tile

---

## Combat System

Combat is **turn‑based** and begins when entering an `EnemyTile`.

Attack resolution:

d20 + attack modifier

Special rules:

Natural 20
- automatic hit
- doubles damage dice only

Natural 1
- automatic miss

Enemy attack stats:

Goblin → DEX  
Bandit → DEX  
Enemy Mage → INT  

Combat logic is handled by `CombatService`.

---

## Escape Mechanic

Players can attempt to flee combat.

Calculation:

player: d20 + DEX  
enemy:  d20 + WIS  

If the player wins the roll, the escape succeeds.

---

## Inventory System

The inventory distinguishes two types of items.

Stackable items  
- handled by item code

Non‑stackable items  
- handled through `InventoryEntry`

This allows the game to track individual items even when multiple identical objects exist.

---

## Equipment System

Items are categorized using equipment types.

Examples:
- weapons
- armour
- shields
- scrolls

Each character class can equip only certain equipment types.

Compatibility is enforced through the `canEquip()` logic.

---

## Scroll Mechanics

Scrolls follow special rules.

- a charge is consumed on every attack attempt
- scrolls disappear when their uses reach zero
- they are automatically unequipped when depleted

---

## Rest System

On an `EmptyTile`, the player can rest.

Healing is determined by:

level × hit dice

HP is clamped to the character's max HP.

---

## Training System

Players may train stats on `EmptyTile`.

Rules:

- the player chooses a stat
- two training actions are required to gain +1
- stats are capped at 20
- training progress persists

---

## Experience and Leveling

Enemies grant experience when defeated.

Leveling up:
- recalculates max HP
- grants 2 stat points to distribute

---

## Character Inspection

Players can inspect their character at any time after a tile interaction.

Displayed information includes:

- name
- class
- level
- HP / max HP
- XP
- stats
- equipment
- training progress

---

## Save / Load System

Game progress is stored in a database.

Repositories used:

CharacterRepository  
InventoryRepository  
BoardRepository  
ClassRepository  

Game state is managed through `GameSessionService`.

---

# Project Structure

src/

config/  
database/  
dto/  
main_logic/  
model/  
persistence/  
tile/  
utilities/

---

# UML Diagram

A UML class diagram of the project is available in the repository as:

chronicle_of_gaia.puml

It can be rendered using:

https://plantuml.com  
https://planttext.com

---

# Current Status

Implemented
- board system
- tile interactions
- combat system
- inventory and equipment
- scroll mechanics
- rest and training
- XP and leveling
- character inspection
- database persistence

Possible future improvements
- loot balancing
- enemy progression tuning
- additional encounters or events
