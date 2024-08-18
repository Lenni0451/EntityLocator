package net.lenni0451.entitylocator.command;

import net.lenni0451.entitylocator.EntityLocator;
import net.lenni0451.entitylocator.Main;
import net.lenni0451.entitylocator.model.EntityCollection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class EntityLocatorCommand implements CommandExecutor, Listener {

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
        if (args.length != 0) {
            player.sendMessage("§cUsage: /entitylocator");
            return true;
        }

        List<EntityCollection> entities = EntityLocator.locateEntities();
        Main.getInstance().getInventoryHandler().openInventory(player, entities);
        return true;
    }

}
