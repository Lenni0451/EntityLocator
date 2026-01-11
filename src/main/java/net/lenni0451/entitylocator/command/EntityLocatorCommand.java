package net.lenni0451.entitylocator.command;

import net.lenni0451.entitylocator.Main;
import net.lenni0451.entitylocator.inventory.impl.EntityListInventory;
import net.lenni0451.entitylocator.locator.PerChunkLocator;
import net.lenni0451.entitylocator.model.EntityCollection;
import net.lenni0451.entitylocator.model.EntityFilter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

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

        EntityFilter entityFilter = EntityFilter.fromArgs(player, args);
        List<EntityCollection> entities = PerChunkLocator.locateEntities(entityFilter);
        Main.getInstance().getInventoryManager().openInventory(player, new EntityListInventory(entities));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return EntityFilter.tabComplete(args);
    }

}
