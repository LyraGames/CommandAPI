package net.lyragames.command.container;

import lombok.Getter;
import net.lyragames.command.command.LyraCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/8/2022
 * Project: CommandAPI
 */

@Getter
public class CommandContainer {

    private final List<LyraCommand> commands = new ArrayList<>();

    public LyraCommand getCommand(String name) {
        return commands.stream().filter(command -> command.getName().equalsIgnoreCase(name) || Arrays.stream(command.getAliases()).anyMatch(s -> s.equalsIgnoreCase(name)))
                .findFirst().orElse(null);
    }
}
