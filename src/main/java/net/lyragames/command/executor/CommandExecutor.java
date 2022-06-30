package net.lyragames.command.executor;

import lombok.Getter;
import net.lyragames.command.CommandAPI;
import net.lyragames.command.command.LyraCommand;
import net.lyragames.command.provider.exception.CommandExitException;
import net.lyragames.command.util.JavaUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/17/2022
 * Project: CommandAPI
 */

@Getter
public class CommandExecutor extends BukkitCommand {

    private final CommandAPI commandAPI;
    private LyraCommand lyraCommand;

    public CommandExecutor(CommandAPI commandAPI, LyraCommand command) {
        super(command.getName().split("\\s+")[0]);

        this.commandAPI = commandAPI;
        this.lyraCommand = command;

        this.description = command.getDescription();

        this.setAliases(Arrays.asList(command.getAliases()));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        LyraCommand lyraCommand1 = lyraCommand;

        if (lyraCommand.findSubCommand(args) != null) {
            lyraCommand1 = lyraCommand.findSubCommand(args);
        }

        if (lyraCommand1.isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This is a player only command!");
            return true;
        }

        if (lyraCommand1.isConsoleOnly() && !(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(ChatColor.RED + "This is a console only command!");
            return true;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!lyraCommand1.getPermission().isEmpty()) {
                if (!player.hasPermission(lyraCommand1.getPermission())) {
                    player.sendMessage(ChatColor.RED + "You dont have enough permissions to execute this command!");
                    return true;
                }
            }
        }

        Method method = lyraCommand1.getMethod();

        if (method == null || lyraCommand1.getCommandClass() == null) {
            sender.sendMessage(ChatColor.RED + "That command is not correctly registered!");
            return true;
        }

        if (!method.isAccessible()) {
            if (JavaUtil.getJavaVersion() > 8) {
                throw new IllegalArgumentException("The method is not accessible!");
            }

            method.setAccessible(true);
        }

        try {
            String[] commandNameArgs = lyraCommand1.getName().split("\\s+");

            List<Object> arguments = commandAPI.getArgumentHandler().processArguments(sender, commandNameArgs.length == 1 ? args : Arrays.copyOfRange(args, commandNameArgs.length - 1, args.length), method, lyraCommand1);

            method.invoke(lyraCommand1.getCommandClass(), arguments.toArray());
        } catch (CommandExitException e) {
            sender.sendMessage(e.getExitMessage());
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            sender.sendMessage(ChatColor.RED + "An error occurred while running the command!");
            e.printStackTrace();
        }

        return false;
    }
}
