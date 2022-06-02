package fr.mrcoq.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WWTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> sub = new ArrayList<>();

        if(args.length == 1) {
            return Arrays.stream(SubCommands.values()).map(subCommands -> subCommands.getArgs()[0]).collect(Collectors.toList());
        } else {
            Optional<SubCommands> subCommand = Arrays.stream(SubCommands.values()).filter(subCommands -> args.length <= (subCommands.getArgs().length) && Arrays.stream(subCommands.getArgs()).anyMatch(arg -> arg.equalsIgnoreCase(args[args.length-2]))).findFirst();
            subCommand.ifPresent(subCommands -> sub.add(subCommands.getArgs()[args.length-1]));
        }


        return sub;
    }
}
