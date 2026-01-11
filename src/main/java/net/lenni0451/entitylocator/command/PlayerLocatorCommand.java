package net.lenni0451.entitylocator.command;

import net.lenni0451.entitylocator.Main;
import net.lenni0451.entitylocator.inventory.impl.PlayerListInventory;
import net.lenni0451.entitylocator.locator.PlayerDistanceLocator;
import net.lenni0451.entitylocator.model.EntityFilter;
import net.lenni0451.entitylocator.model.NearbyEntities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class PlayerLocatorCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be executed by players!");
            return true;
        }
        if (!player.hasPermission("entitylocator.use")) {
            player.sendMessage("§cYou do not have permission to use this command!");
            return true;
        }

        EntityFilter entityFilter = EntityFilter.fromArgs(player, args);
        List<NearbyEntities> nearbyEntities = PlayerDistanceLocator.locateEntities(entityFilter);
        Main.getInstance().getInventoryManager().openInventory(player, new PlayerListInventory(nearbyEntities));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NonNull final CommandSender sender, @NonNull final Command command, @NonNull final String label, @NonNull final String[] args) {
        return EntityFilter.tabComplete(args);
    }

}
