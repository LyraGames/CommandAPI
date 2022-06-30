package net.lyragames.command.provider.impl.bukkit;

import net.lyragames.command.provider.Provider;
import net.lyragames.command.provider.exception.CommandExitException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/10/2022
 * Project: CommandAPI
 */
public class PlayerProvider implements Provider<Player> {

    @Override
    public Player provide(String s) throws CommandExitException {

        Player player = Bukkit.getPlayer(s);

        if (player == null) {
            throw new CommandExitException(ChatColor.RED + "That player is offline!");
        }

        return player;
    }
}
