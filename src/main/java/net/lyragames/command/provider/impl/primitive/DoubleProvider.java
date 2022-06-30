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
public class DoubleProvider implements Provider<Double> {

    @Override
    public Double provide(String s) throws CommandExitException {

        try {
            return Double.parseDouble(s);
        }catch (Exception e) {
            throw new CommandExitException(ChatColor.RED + "Not a valid double!");
        }
    }
}
