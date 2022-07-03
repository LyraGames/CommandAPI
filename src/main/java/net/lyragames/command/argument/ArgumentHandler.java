package net.lyragames.command.argument;

import lombok.RequiredArgsConstructor;
import net.lyragames.command.CommandAPI;
import net.lyragames.command.annotation.*;
import net.lyragames.command.command.LyraCommand;
import net.lyragames.command.provider.Provider;
import net.lyragames.command.provider.exception.CommandExitException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/8/2022
 * Project: CommandAPI
 */

@RequiredArgsConstructor
public class ArgumentHandler {

    private final CommandAPI commandAPI;

    public List<Argument> buildArguments(Method method) {
        List<Argument> arguments = new ArrayList<>();

        int i = 0;
        for (Parameter parameter : method.getParameters()) {
            if (method.getParameters().length > i + 1 && parameter.isAnnotationPresent(Optional.class)) {
                throw new IllegalArgumentException("The optional parameter has to be the last parameter.");
            }

            if (method.getParameters().length > i + 1 && parameter.isAnnotationPresent(Combined.class)) {
                throw new IllegalArgumentException("The optional parameter has to be the last parameter.");
            }

            Argument argument = new Argument(parameter.getType());

            if (parameter.isAnnotationPresent(Named.class)) {
                argument.setNamed(true);
                argument.setName(parameter.getAnnotation(Named.class).value());
            }

            if (parameter.isAnnotationPresent(Optional.class)) {
                argument.setOptional(true);
            }

            if (parameter.isAnnotationPresent(Combined.class)) {
                argument.setCombined(true);
            }

            if (parameter.isAnnotationPresent(Flag.class)) {
                argument.setFlag(true);
                argument.setFlagString(parameter.getAnnotation(Flag.class).value());

                if (parameter.getType() != Boolean.class && parameter.getType() != boolean.class) {
                    throw new IllegalArgumentException("The flag parameter needs to be a boolean!");
                }
            }

            if (parameter.isAnnotationPresent(Sender.class)) {
                if (parameter.getType() == CommandSender.class || parameter.getType() == ConsoleCommandSender.class || parameter.getType() == Player.class) {
                    argument.setSender(true);
                }else {
                    throw new IllegalArgumentException("The sender parameter is not compatible with '" + parameter.getType().getSimpleName() + "'");
                }
            }


            arguments.add(argument);
            i++;
        }

        return arguments;
    }

    public List<Object> processArguments(CommandSender sender, String[] args, Method method, LyraCommand lyraCommand) throws CommandExitException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Object> toReturn = new ArrayList<>();

        List<Argument> requiredArgs = buildArguments(method);
        List<Argument> essentialArgs = requiredArgs.stream()
                .filter(argument -> !argument.isOptional() && !argument.isSender() && !argument.isFlag()).collect(Collectors.toList());

        int parameters = method.getParameterCount();

        int indexed = 0;
        for (Argument argument : requiredArgs) {

            // usage message
            if (args.length < essentialArgs.size()) {
                throw new CommandExitException(ChatColor.RED + generateUsage(lyraCommand, requiredArgs));
            }

            // flags
            if (argument.isFlag()) {
                if (args.length <= indexed) {
                    toReturn.add(false);
                    continue;
                }

                String flagArg = args[indexed];
                boolean flag = argument.getFlagString().equalsIgnoreCase(flagArg) || flagArg.equalsIgnoreCase("-" + argument.getFlagString());

                toReturn.add(flag);

                if (flag) {
                    indexed++;
                }

                continue;
            }

            // combined
            if ((parameters - indexed) >= 1 && argument.isCombined()) {
                if (argument.getType() != String.class) {
                    throw new IllegalArgumentException("The combined parameter needs to be a string");
                }

                StringBuilder builder = new StringBuilder();

                for (String s : Arrays.copyOfRange(args, indexed, args.length)) {
                    builder.append(s).append(" ");
                }

                toReturn.add(builder.toString());
                break;
            }

            // optionals
            if ((parameters - indexed) > 1 && argument.isOptional()) {
                if ((args.length - indexed) >= 1) {
                    toReturn.add(provide(argument, args[indexed]));
                }else {
                    toReturn.add(null);
                }

                continue;
            }

            // sender
            if (argument.isSender()) {
                toReturn.add(sender);
                continue;
            }

            toReturn.add(provide(argument, args[indexed]));

            indexed++;
        }

        return toReturn;
    }

    public Object provide(Argument argument, String arg) throws CommandExitException {
        Object object = null;

        if (argument.getType() == String.class) {
            object = arg;
        }else {
            Provider<?> provider = commandAPI.getProviderContainer().getProviders().get(argument.getType());

            if (provider == null) {
                throw new IllegalArgumentException("There is no supported provider for '" + argument.getType().getName() + "'");
            }

            object = provider.provide(arg);
        }

        return object;
    }

    public String generateUsage(LyraCommand lyraCommand, List<Argument> arguments) {
        StringBuilder builder = new StringBuilder();

        builder.append("/").append(lyraCommand.getName()).append(" ");

        int indexed = 0;

        for (Argument argument : arguments) {
            if (argument.isSender()) continue;

            if (argument.isOptional() || argument.isFlag()) {
                builder.append("[");
            }else {
                builder.append("<");
            }

            if (argument.isNamed()) {
                builder.append(argument.getName());
            }else if (argument.isFlag()) {
                builder.append("-").append(argument.getFlagString());
            }else {
                builder.append("arg").append(indexed);
            }

            if (argument.isOptional() || argument.isFlag()) {
                builder.append("]").append(" ");
            }else {
                builder.append(">").append(" ");
            }

            indexed++;
        }

        return builder.toString();
    }
}
