package net.lenni0451.entitylocator.inventory.simple;

import net.lenni0451.entitylocator.inventory.ClickType;
import net.lenni0451.entitylocator.inventory.ManagedInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public abstract class SimpleInventory extends ManagedInventory {

    private final Map<Player, ItemContainer> itemContainers;

    public SimpleInventory(final String title, final int rows) {
        super(title, rows);
        this.itemContainers = new HashMap<>();
    }

    protected abstract void init(final Player player, final ItemContainer items);

    @Override
    public void open(Inventory inventory, Player player) {
        ItemContainer items = new ItemContainer(inventory.getSize());
        this.init(player, items);
        inventory.setContents(items.getItems());
        this.itemContainers.put(player, items);
    }

    @Override
    public void close(Player player) {
        this.itemContainers.remove(player);
    }

    @Override
    public void onClick(Player player, int slot, ClickType clickType) {
        ItemContainer items = this.itemContainers.get(player);
        if (items == null) return;

        ClickListener listener = items.getClickListener()[slot];
        if (listener != null) listener.onClick(clickType);
    }

}
