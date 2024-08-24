package net.lenni0451.entitylocator.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public class InventoryManager implements Listener {

    private final Map<Player, PlayerInventory> openInventories;
    private final Map<Player, Stack<PlayerInventory>> inventoryHistory;

    public InventoryManager() {
        this.openInventories = new HashMap<>();
        this.inventoryHistory = new HashMap<>();
    }

    public void openInventory(final Player player, final ManagedInventory inventory) {
        this.closeInventory(player); //Close the current inventory and all history inventories
        this.pushInventory(player, inventory); //Push the new inventory
    }

    public void pushInventory(final Player player, final ManagedInventory inventory) {
        if (!this.openInventories.containsKey(player)) { //If the player has no open inventory just open the new one
            Inventory bukkitInventory = Bukkit.createInventory(player, inventory.getRows() * 9, inventory.getTitle());
            inventory.open(bukkitInventory, player);
            player.openInventory(bukkitInventory);
            this.openInventories.put(player, new PlayerInventory(inventory, bukkitInventory));
        } else { //If the player has an open inventory, push the current inventory to the history and open the new one
            this.inventoryHistory.computeIfAbsent(player, k -> new Stack<>()).push(this.openInventories.remove(player));
            this.pushInventory(player, inventory);
        }
    }

    public void refreshInventory(final Player player) {
        PlayerInventory inventory = this.openInventories.get(player);
        if (inventory == null) return;

        inventory.inventory.open(inventory.bukkitInventory, player);
    }

    public void popInventory(final Player player) {
        if (this.openInventories.containsKey(player)) {
            PlayerInventory inventory = this.openInventories.remove(player);
            player.closeInventory();
            inventory.inventory.close(player);
        }
        Stack<PlayerInventory> history = this.inventoryHistory.get(player);
        if (history == null || history.isEmpty()) return;

        PlayerInventory inventory = history.pop();
        this.openInventories.put(player, inventory);
        player.openInventory(inventory.bukkitInventory);
    }

    public void closeInventory(final Player player) {
        PlayerInventory inventory = this.openInventories.remove(player);
        Stack<PlayerInventory> history = this.inventoryHistory.get(player);
        if (inventory == null) return;

        player.closeInventory();
        inventory.inventory.close(player);
        if (history != null) history.forEach(playerInventory -> playerInventory.inventory.close(player));
    }

    public void closeAll() {
        for (Map.Entry<Player, PlayerInventory> entry : this.openInventories.entrySet()) {
            entry.getKey().closeInventory();
            entry.getValue().inventory.close(entry.getKey());
        }
        this.openInventories.clear();

        for (Map.Entry<Player, Stack<PlayerInventory>> entry : this.inventoryHistory.entrySet()) {
            entry.getValue().forEach(inventory -> inventory.inventory.close(entry.getKey()));
        }
        this.inventoryHistory.clear();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        this.closeInventory((Player) event.getPlayer());
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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.closeInventory(event.getPlayer());
        this.inventoryHistory.remove(event.getPlayer());
    }


    private record PlayerInventory(ManagedInventory inventory, Inventory bukkitInventory) {
    }

}
