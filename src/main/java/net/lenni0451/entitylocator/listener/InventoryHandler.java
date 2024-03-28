package net.lenni0451.entitylocator.listener;

import net.lenni0451.entitylocator.data.EntityCollection;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryHandler implements Listener {

    private final Map<Player, List<EntityCollection>> openInventories;
    private final Map<Player, Integer> openInventoryPages;

    public InventoryHandler() {
        this.openInventories = new HashMap<>();
        this.openInventoryPages = new HashMap<>();
    }

    public void openInventory(final Player player, final List<EntityCollection> entities) {
        this.openInventories.put(player, entities);
        this.openInventoryPages.put(player, 0);
        this.openInventory(player);
    }

    private void openInventory(final Player player) {
        List<EntityCollection> entities = this.openInventories.get(player);
        if (entities == null) return;

        int maxPages = (int) Math.ceil(entities.size() / 45F);
        int page = this.openInventoryPages.get(player);
        if (page < 0) page = 0;
        if (page >= maxPages) page = maxPages - 1;
        List<EntityCollection> pageEntities = entities.subList(page * 45, Math.min(entities.size(), (page + 1) * 45));

        Inventory inventory;
        boolean newInventory;
        if (player.getOpenInventory().getTitle().startsWith("§5EntityLocator")) {
            inventory = player.getOpenInventory().getTopInventory();
            inventory.clear();
            newInventory = false;
        } else {
            inventory = Bukkit.createInventory(player, 9 * 6, "§5EntityLocator §7| §aPage " + (page + 1) + "/" + maxPages);
            newInventory = true;
        }
        for (int i = 0; i < pageEntities.size(); i++) {
            EntityCollection entity = pageEntities.get(i);
            ItemStack item = new ItemStack(Material.SPAWNER);
            item.setAmount(Math.min(64, entity.count()));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6" + entity.world().getName() + " §7| §e" + entity.chunkLocation().x() + "§7/§e" + entity.chunkLocation().z() + " §7| §a" + entity.count() + " Entities");
            List<String> lore = new ArrayList<>();
            lore.add("§aWorld: §6" + entity.world().getName());
            lore.add("§aChunk: §6" + entity.chunkLocation().x() + "§7/§6" + entity.chunkLocation().z());
            lore.add("§aEntities: §6" + entity.count());
            lore.add("§aTypes: §6" + String.join("§7, §6", entity.types().stream().map(Enum::name).toList()));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(i, item);
        }
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        if (page > 0) {
            ItemStack back = new ItemStack(Material.ARROW);
            ItemMeta meta = back.getItemMeta();
            meta.setDisplayName("§6Back");
            back.setItemMeta(meta);
            inventory.setItem(45, back);
        }
        if (page < maxPages - 1) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta meta = next.getItemMeta();
            meta.setDisplayName("§6Next");
            next.setItemMeta(meta);
            inventory.setItem(53, next);
        }

        if (newInventory) player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!this.openInventories.containsKey((Player) event.getWhoClicked())) return;
        event.setCancelled(true);
        if (event.getSlot() > 53) return;

        Player player = (Player) event.getWhoClicked();
        List<EntityCollection> entities = this.openInventories.get(player);
        if (entities == null) return;

        int maxPages = (int) Math.ceil(entities.size() / 45F);
        int page = this.openInventoryPages.get(player);
        if (page < 0) page = 0;
        if (page >= maxPages) page = maxPages - 1;
        List<EntityCollection> pageEntities = entities.subList(page * 45, Math.min(entities.size(), (page + 1) * 45));

        if (event.getSlot() >= 0 && event.getSlot() <= pageEntities.size() - 1) {
            this.openInventories.remove(player);
            this.openInventoryPages.remove(player);
            player.closeInventory();

            EntityCollection entity = pageEntities.get(event.getSlot());
            Chunk chunk = entity.world().getChunkAt(entity.chunkLocation().x(), entity.chunkLocation().z());
            Location location = chunk.getBlock(0, 0, 0).getLocation();
            location.add(0.5, chunk.getWorld().getHighestBlockYAt(location) + 1, 0.5);
            player.teleport(location);
        } else if (event.getSlot() == 45 && page > 0) {
            this.openInventoryPages.put(player, page - 1);
            this.openInventory(player);
        } else if (event.getSlot() == 53 && page < maxPages - 1) {
            this.openInventoryPages.put(player, page + 1);
            this.openInventory(player);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        this.openInventories.remove((Player) event.getPlayer());
        this.openInventoryPages.remove((Player) event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.openInventories.remove(event.getPlayer());
        this.openInventoryPages.remove(event.getPlayer());
    }

}
