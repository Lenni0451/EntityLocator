package net.lenni0451.entitylocator.model;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public record EntityFilter(Set<EntityType> whitelist, Set<EntityType> blacklist) implements Predicate<EntityType> {

    @Nullable
    public static EntityFilter fromArgs(final Player player, final String[] args) {
        Set<EntityType> whitelist = new HashSet<>();
        Set<EntityType> blacklist = new HashSet<>();
        for (String arg : args) {
            if (!arg.startsWith("+") && !arg.startsWith("-")) {
                player.sendMessage("§cInvalid argument: " + arg);
                return null;
            }

            EntityType entityType;
            try {
                entityType = EntityType.valueOf(arg.substring(1).toUpperCase());
            } catch (Throwable t) {
                player.sendMessage("§cInvalid entity type: " + arg.substring(1));
                return null;
            }

            if (arg.startsWith("+")) {
                whitelist.add(entityType);
            } else {
                blacklist.add(entityType);
            }
        }
        return new EntityFilter(whitelist, blacklist);
    }

    public static List<String> tabComplete(final String[] args) {
        List<String> completions = new ArrayList<>();
        String arg = args.length == 0 ? "" : args[args.length - 1].toLowerCase();
        if (arg.isEmpty()) {
            completions.add("+");
            completions.add("-");
        } else if (arg.startsWith("+") || arg.startsWith("-")) {
            String prefix = arg.substring(0, 1);
            String search = arg.substring(1);
            for (EntityType value : EntityType.values()) {
                if (value.name().toLowerCase().startsWith(search)) {
                    completions.add(prefix + value.name().toLowerCase());
                }
            }
        }
        return completions;
    }


    @Override
    public boolean test(final EntityType entityType) {
        if (!this.whitelist.isEmpty() && !this.whitelist.contains(entityType)) return false;
        return !this.blacklist.contains(entityType);
    }

}
