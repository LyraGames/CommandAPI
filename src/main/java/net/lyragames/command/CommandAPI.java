package net.lyragames.command;

import lombok.Getter;
import net.lyragames.command.argument.ArgumentHandler;
import net.lyragames.command.container.CommandContainer;
import net.lyragames.command.provider.Provider;
import net.lyragames.command.provider.container.ProviderContainer;
import net.lyragames.command.registrar.CommandRegistrar;
import net.lyragames.command.registrar.RegistrarCompleter;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/8/2022
 * Project: CommandAPI
 */

@Getter
public class CommandAPI {

    protected ProviderContainer providerContainer;
    protected CommandContainer commandContainer;
    protected ArgumentHandler argumentHandler;

    protected CommandRegistrar commandRegistrar;
    protected RegistrarCompleter registrarCompleter;

    public CommandAPI() {
        this.providerContainer = new ProviderContainer();
        this.commandContainer = new CommandContainer();
        this.argumentHandler = new ArgumentHandler(this);

        this.commandRegistrar = new CommandRegistrar(this);
        this.registrarCompleter = new RegistrarCompleter(this);
    }

    public RegistrarCompleter beginCommandRegister() {
        return registrarCompleter;
    }

    public CommandAPI bind(Class<?> clazz, Provider<?> provider) {
        providerContainer.getProviders().put(clazz, provider);
        return this;
    }
}
