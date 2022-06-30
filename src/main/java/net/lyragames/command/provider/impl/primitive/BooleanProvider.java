package net.lyragames.command.provider.impl.primitive;

import net.lyragames.command.provider.Provider;
import net.lyragames.command.provider.exception.CommandExitException;
import org.bukkit.ChatColor;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/10/2022
 * Project: CommandAPI
 */

public class BooleanProvider implements Provider<Boolean> {

    @Override
    public Boolean provide(String s) throws CommandExitException {

        try {
            return Boolean.parseBoolean(s);
        }catch (Exception e) {
            throw new CommandExitException(ChatColor.RED + "Not a valid boolean!");
        }
    }
}
