package net.lenni0451.entitylocator.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class ManagedInventory {

    private final String title;
    private final int rows;

    public ManagedInventory(final String title, final int rows) {
        this.title = title;
        this.rows = rows;
    }

    public String getTitle() {
        return this.title;
    }

    public int getRows() {
        return this.rows;
    }

    public abstract void open(final Inventory inventory, final Player player);

    public abstract void close(final Player player);

    public abstract void onClick(final Player player, final int slot, final ClickType clickType);

}
