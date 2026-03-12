package tile;

import main_logic.enums.EncounterResult;
import main_logic.enums.Stat;
import main_logic.session.GameSession;
import model.entities.classes.PlayerCharacter;
import utilities.Console;

public class EmptyTile implements Tile {

    private static final int REST_OPTION = 4;
    private static final int TRAIN_OPTION = 5;

    @Override
    public String describe() {
        return "You only see grass around. If anything was here before the janitors cleaned up fast";
    }

    @Override
    public EncounterResult onEnter(GameSession session) {
        Console.print("Nothing happens...", Console.ConsoleColor.BRIGHT_GREEN);
        return EncounterResult.NONE;
    }

    @Override
    public int printPostTileActions(int startIndex) {
        Console.print(startIndex + ") Rest");
        Console.print((startIndex + 1) + ") Train");
        return startIndex + 1;
    }

    @Override
    public boolean handlePostTileAction(GameSession session, int choice) {
        if (choice == REST_OPTION) {
            int healed = session.getPlayer().rest();
            Console.print(
                    session.getPlayer().getName() + " rests and heals " + healed + " HP.",
                    Console.ConsoleColor.GREEN
            );
            Console.print("HP: " + session.getPlayer().getHp() + "/" + session.getPlayer().getMaxHp());
            return true;
        }

        if (choice == TRAIN_OPTION) {
            return handleTraining(session);
        }

        return false;
    }

    private boolean handleTraining(GameSession session) {
        Stat[] stats = Stat.values();

        while (true) {
            Console.print("Choose a stat to train:");

            for (int i = 0; i < stats.length; i++) {
                int currentValue = session.getPlayer().getOneStat(stats[i]);
                String suffix = currentValue >= 20 ? " (MAX)" : "";
                Console.print((i + 1) + ") " + stats[i] + suffix);
            }

            int cancelOption = stats.length + 1;
            Console.print(cancelOption + ") Cancel");

            int choice = Console.askInt("Choice: ", 1, cancelOption);

            if (choice == cancelOption) {
                return false;
            }

            Stat selectedStat = stats[choice - 1];
            PlayerCharacter.TrainingResult result = session.getPlayer().train(selectedStat);

            if (result == PlayerCharacter.TrainingResult.CAPPED) {
                Console.print(selectedStat + " is already at 20.", Console.ConsoleColor.YELLOW);
                continue;
            }

            if (result == PlayerCharacter.TrainingResult.INCREASED) {
                Console.print(selectedStat + " increased by 1.", Console.ConsoleColor.GREEN);
                return true;
            }

            Console.print(
                    "Training progress for " + selectedStat + ": "
                            + session.getPlayer().getOneTrainingProgress(selectedStat) + "/2",
                    Console.ConsoleColor.CYAN
            );
            return true;
        }
    }
}