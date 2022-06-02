package fr.rmcoq.command;

import fr.rmcoq.LoupGarousse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public class WWCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("§cImpossible d'exécuter cela depuis la console");
            return false;
        }

        Player player = (Player) sender;

        if(args.length == 0) {
            player.sendMessage("§cFaites : /lg help");
            return true;
        }

        Optional<SubCommands> subCommand = Arrays.stream(SubCommands.values()).filter(subCommands -> Arrays.equals(subCommands.getArgs(), args)).findFirst();

        if(subCommand.isPresent()) {
            subCommand.get().run(player);
            return true;
        } else {
            player.sendMessage("Ya une couille dans le paté");
        }

        /*
        if(args.length == 1 && args[0].equalsIgnoreCase("help")) {
            player.sendMessage(
                    "§c----------------------------" + "\n" +
                    " §8- §6/lg join §7:  Rejoindre la partie" + "\n" +
                    " §8- §6/lg leave §7: Quitter la partie"  + "\n" +
                    " §8- §6/lg start §7: Lancer la partie"  + "\n" +
                    "§c----------------------------"
            );
        } else if(args.length == 1 && args[0].equalsIgnoreCase("join")) {

            if(LoupGarousse.getGame().join(player)) {
                player.sendMessage("§7Vous avez §6rejoinds §7la partie !");
            } else {
                player.sendMessage("§7Vous êtes §cdéjà §7dans §7la partie");
            }

        } else if(args.length == 1 && args[0].equalsIgnoreCase("leave")) {

            if(LoupGarousse.getGame().leave(player)) {
                player.sendMessage("§7Vous avez §6quitté §7la partie !");
            } else {
                player.sendMessage("§7Vous êtes §cpas §7dans §7la partie");
            }

        } else {
                player.sendMessage("§cFaites : /lg help");
        }*/

        return false;
    }

}
