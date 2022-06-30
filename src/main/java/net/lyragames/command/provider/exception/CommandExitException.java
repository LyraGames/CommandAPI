package net.lyragames.command.provider.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/10/2022
 * Project: CommandAPI
 */

@RequiredArgsConstructor @Getter
public class CommandExitException extends Exception {

    private final String exitMessage;
}
