package net.lyragames.command.provider;

import net.lyragames.command.provider.exception.CommandExitException;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/10/2022
 * Project: CommandAPI
 */
public interface Provider<T> {

    T provide(String s) throws CommandExitException;
}
