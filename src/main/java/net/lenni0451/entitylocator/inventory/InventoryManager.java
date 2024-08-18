package net.lenni0451.entitylocator.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryManager implements Listener {

    private final Map<Player, PlayerInventory> openInventories;

    public InventoryManager() {
        this.openInventories = new HashMap<>();
    }

    public void openInventory(final Player player, final ManagedInventory inventory) {
        this.closeInventory(player);

        Inventory bukkitInventory = Bukkit.createInventory(player, inventory.getRows() * 9, inventory.getTitle());
        this.openInventories.put(player, new PlayerInventory(inventory, bukkitInventory));
        inventory.open(bukkitInventory, player);
        player.openInventory(bukkitInventory);
    }

    public void refreshInventory(final Player player) {
        PlayerInventory inventory = this.openInventories.get(player);
        if (inventory == null) return;

        inventory.inventory.open(inventory.bukkitInventory, player);
    }

    public void closeInventory(final Player player) {
        PlayerInventory inventory = this.openInventories.remove(player);
        if (inventory == null) return;

        player.closeInventory();
        inventory.inventory.close(player);
    }

    public void closeAll() {
        for (Map.Entry<Player, PlayerInventory> entry : this.openInventories.entrySet()) {
            entry.getKey().closeInventory();
            entry.getValue().inventory.close(entry.getKey());
        }
        this.openInventories.clear();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        PlayerInventory inventory = this.openInventories.remove(event.getPlayer());
        if (inventory == null) return;
        inventory.inventory.close((Player) event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        PlayerInventory inventory = this.openInventories.get(event.getWhoClicked());
        if (inventory == null) return;

        event.setCancelled(true);
        if (Objects.equals(event.getClickedInventory(), inventory.bukkitInventory)) {
            inventory.inventory.onClick((Player) event.getWhoClicked(), event.getSlot(), event.isLeftClick() ? ClickType.LEFT : ClickType.RIGHT);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        PlayerInventory inventory = this.openInventories.get(event.getWhoClicked());
        if (inventory == null) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        PlayerInventory inventory = this.openInventories.get(event.getDestination().getHolder());
        if (inventory == null) return;

        event.setCancelled(true);
    }


    private record PlayerInventory(ManagedInventory inventory, Inventory bukkitInventory) {
    }

}
