package net.lyragames.command.provider.container;

import lombok.Getter;
import net.lyragames.command.provider.Provider;
import net.lyragames.command.provider.impl.bukkit.PlayerProvider;
import net.lyragames.command.provider.impl.primitive.BooleanProvider;
import net.lyragames.command.provider.impl.primitive.DoubleProvider;
import net.lyragames.command.provider.impl.primitive.IntegerProvider;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/10/2022
 * Project: CommandAPI
 */

@Getter
public class ProviderContainer {

    private final ConcurrentMap<Class<?>, Provider<?>> providers = new ConcurrentHashMap<>();

    public ProviderContainer() {
        providers.put(Integer.class, new IntegerProvider());
        providers.put(int.class, new IntegerProvider());

        providers.put(Double.class, new DoubleProvider());
        providers.put(double.class, new DoubleProvider());

        providers.put(Boolean.class, new BooleanProvider());
        providers.put(boolean.class, new BooleanProvider());

        providers.put(Player.class, new PlayerProvider());
    }
}
