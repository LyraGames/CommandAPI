package net.lyragames.command.command;

import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.Setter;
import net.lyragames.command.argument.Argument;

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

@Getter @Setter
public class LyraCommand {

    private String name;
    private String[] aliases;
    private String description;

    private boolean async;
    private boolean playerOnly, consoleOnly;

    private Object commandClass;

    private final List<Argument> arguments = new ArrayList<>();
    private final List<LyraCommand> subCommands = new ArrayList<>();

    private String permission;

    private Method method;

    public LyraCommand(String name, String[] aliases, String description, boolean async, String permission) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.async = async;
        this.permission = permission;
    }

    public LyraCommand findSubCommand(String[] args) {
        for (LyraCommand command : subCommands) {
            String[] commandSplit = command.getName().split("\\s+");

            String name = Joiner.on(" ").join(Arrays.asList(commandSplit).subList(1, commandSplit.length));

            String joinedArgs = Joiner.on(" ").join(args);

            if (joinedArgs.contains(name)) return command;
        }

        return null;
    }
}
