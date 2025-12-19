package net.lenni0451.entitylocator.command;

import net.lenni0451.entitylocator.Main;
import net.lenni0451.entitylocator.inventory.impl.EntityListInventory;
import net.lenni0451.entitylocator.locator.PerChunkLocator;
import net.lenni0451.entitylocator.model.EntityCollection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ParametersAreNonnullByDefault
public class EntityLocatorCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be executed by players!");
            return true;
        }
        if (!player.hasPermission("entitylocator.use")) {
            player.sendMessage("§cYou do not have permission to use this command!");
            return true;
        }

        Set<EntityType> whitelist = new HashSet<>();
        Set<EntityType> blacklist = new HashSet<>();
        for (String arg : args) {
            if (!arg.startsWith("+") && !arg.startsWith("-")) {
                player.sendMessage("§cInvalid argument: " + arg);
                return true;
            }

            EntityType entityType;
            try {
                entityType = EntityType.valueOf(arg.substring(1).toUpperCase());
            } catch (Throwable t) {
                player.sendMessage("§cInvalid entity type: " + arg.substring(1));
                return true;
            }

            if (arg.startsWith("+")) whitelist.add(entityType);
            else blacklist.add(entityType);
        }

        List<EntityCollection> entities = PerChunkLocator.locateEntities(entityType -> {
            if (!whitelist.isEmpty() && !whitelist.contains(entityType)) return false;
            return !blacklist.contains(entityType);
        });
        Main.getInstance().getInventoryManager().openInventory(player, new EntityListInventory(entities));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        String arg = args.length == 0 ? "" : args[args.length - 1].toLowerCase();
        if (arg.isEmpty()) {
            completions.add("+");
            completions.add("-");
        } else if (arg.startsWith("+") || arg.startsWith("-")) {
            String prefix = arg.substring(0, 1);
            String search = arg.substring(1);
            for (EntityType value : EntityType.values()) {
                if (value.name().toLowerCase().startsWith(search)) completions.add(prefix + value.name().toLowerCase());
            }
        }
        return completions;
    }

}
