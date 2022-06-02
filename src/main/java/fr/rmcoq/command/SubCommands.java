package fr.rmcoq.command;

import fr.rmcoq.LoupGarousse;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.function.Consumer;

public enum SubCommands {
    HELP(false, new String[]{ "help" }, "Afficher les commandes disponibles", player -> {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("§c----------------------------\n");

        for(SubCommands subCommand : SubCommands.values()) {
            if(!subCommand.isOp() || player.isOp()) {
                stringBuilder.append(" §8- §6/lg ").append(String.join(" ", subCommand.getArgs())).append(" §7: ").append(subCommand.getDescription()).append("\n");
            }
        }

        stringBuilder.append("§c----------------------------\n");

        player.sendMessage(stringBuilder.toString());
    }),
    JOIN(false, new String[]{ "join" }, "Rejoindre la partie", player -> {
        if(LoupGarousse.getGame().isStarted()) {
            player.sendMessage("§cLa partie est déjà commencée");
            return;
        }

        if(LoupGarousse.getGame().join(player)) {
            player.sendMessage("§7Vous avez §6rejoinds §7la partie !");
        } else {
            player.sendMessage("§7Vous êtes §cdéjà §7dans §7la partie");
        }
    }),
    LEAVE(false, new String[]{ "leave" }, "Quitter la partie", player -> {
        if(LoupGarousse.getGame().isStarted()) {
            player.sendMessage("§cLa partie est déjà commencée");
            return;
        }

        if(LoupGarousse.getGame().leave(player)) {
            player.sendMessage("§7Vous avez §6quitté §7la partie !");
        } else {
            player.sendMessage("§7Vous n'êtes §cpas §7dans §7la partie");
        }
    }),
    START(true, new String[]{ "start" }, "Lancer la partie", player -> {
        if(LoupGarousse.getGame().isStarted()) {
            player.sendMessage("§cLa partie est déjà commencée");
            return;
        }

        LoupGarousse.getGame().start();

    });

    private final boolean op;
    private final String[] args;
    private final String description;
    private final Consumer<Player> action;

    SubCommands(boolean op, String[] args, String description, Consumer<Player> action) {
        this.op = op;
        this.args = args;
        this.description = description;
        this.action = action;
    }

    public void run(Player player) {
        if(!this.isOp() || player.isOp()) {
            this.action.accept(player);
        } else {
            player.sendMessage("§cVous n'avez pas la permission d'exécuter cela");
        }
    }

    public String[] getArgs() {
        return args;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOp() {
        return op;
    }
}
