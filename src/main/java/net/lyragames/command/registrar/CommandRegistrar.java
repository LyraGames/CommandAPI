package net.lyragames.command.registrar;

import lombok.RequiredArgsConstructor;
import net.lyragames.command.CommandAPI;
import net.lyragames.command.annotation.Command;
import net.lyragames.command.annotation.Permission;
import net.lyragames.command.argument.Argument;
import net.lyragames.command.command.LyraCommand;
import net.lyragames.command.executor.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/17/2022
 * Project: CommandAPI
 */

@RequiredArgsConstructor
public class CommandRegistrar {

    private final CommandAPI commandAPI;

    public RegistrarCompleter register(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Command.class)) continue;

            Command command = method.getAnnotation(Command.class);

            boolean hasPermission = method.isAnnotationPresent(Permission.class);
            String permission = hasPermission ? method.getAnnotation(Permission.class).value() : "";

            LyraCommand lyraCommand = new LyraCommand(command.name(), command.aliases(), command.description(), command.async(), permission);
            lyraCommand.setMethod(method);

            lyraCommand.setCommandClass(object);

            List<Argument> arguments = commandAPI.getArgumentHandler().buildArguments(method);
            lyraCommand.getArguments().addAll(arguments);

            Optional<Argument> argument = arguments.stream().filter(Argument::isSender).findFirst();

            if (argument.isPresent()) {
                if (argument.get().getType() == Player.class) {
                    lyraCommand.setPlayerOnly(true);
                }else if (argument.get().getType() == ConsoleCommandSender.class) {
                    lyraCommand.setConsoleOnly(true);
                }
            }

            commandAPI.getCommandContainer().getCommands().add(lyraCommand);
        }

        return commandAPI.getRegistrarCompleter();
    }

    protected void registerBukkit(LyraCommand lyraCommand) {
        try {
           /* Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            if (!bukkitCommandMap.isAccessible()) {
                if (JavaUtil.getJavaVersion() > 8) {
                    CommandMap commandMap = Bukkit.getCommandMap();
                    commandMap.register(lyraCommand.getName(), new CommandExecutor(commandAPI, lyraCommand));

                    return;
                }

                bukkitCommandMap.setAccessible(true);
            } */

            //CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            CommandMap commandMap = (CommandMap) Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer());

            commandMap.register(lyraCommand.getName().split("\\s+")[0], new CommandExecutor(commandAPI, lyraCommand));
        }catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
