package net.lyragames.command.registrar;

import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import net.lyragames.command.CommandAPI;
import net.lyragames.command.command.LyraCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/30/2022
 * Project: CommandAPI
 */

@RequiredArgsConstructor
public class RegistrarCompleter {

    private final CommandAPI commandAPI;

    public RegistrarCompleter register(Object object) {
        return commandAPI.getCommandRegistrar().register(object);
    }

    public CommandAPI endRegister() {
        for (LyraCommand command : commandAPI.getCommandContainer().getCommands()) {
            String[] name = command.getName().split("\\s+");

            if (name.length == 1) {
                commandAPI.getCommandRegistrar().registerBukkit(command);
                continue;
            }

            LyraCommand parent = null;

            for (int i = 0; i < name.length; i++) {
                String commandName = Joiner.on(" ").join(Arrays.asList(name).subList(0, i));

                if (commandAPI.getCommandContainer().getCommand(commandName) == null) continue;

                if (parent == null) {
                    parent = commandAPI.getCommandContainer().getCommand(commandName);
                }

                parent.getSubCommands().add(command);
            }

            if (parent == null) {
                LyraCommand parentCommand = new LyraCommand(name[0], new String[0], "", false, "");
                parentCommand.getSubCommands().add(command);

                commandAPI.getCommandContainer().getCommands().add(parentCommand);
                commandAPI.getCommandRegistrar().registerBukkit(parentCommand);
            }else {
                commandAPI.getCommandRegistrar().registerBukkit(parent);
            }

            commandAPI.getCommandContainer().getCommands().remove(command);
        }

        return commandAPI;
    }
}
