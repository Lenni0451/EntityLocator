package net.lenni0451.entitylocator.inventory.simple;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class ItemContainer {

    private final ItemStack[] items;
    private final ClickListener[] clickListener;

    public ItemContainer(final int slots) {
        this.items = new ItemStack[slots];
        this.clickListener = new ClickListener[slots];
    }

    public ItemStack[] getItems() {
        return this.items;
    }

    public ClickListener[] getClickListener() {
        return this.clickListener;
    }

    public ItemContainer fill(final ItemStack item) {
        return this.fill(item, null);
    }

    public ItemContainer fill(final ItemStack item, @Nullable final ClickListener listener) {
        for (int i = 0; i < this.items.length; i++) {
            this.items[i] = item;
            this.clickListener[i] = listener;
        }
        return this;
    }

    public ItemContainer fillRow(final int row, final ItemStack item) {
        return this.fillRow(row, item, null);
    }

    public ItemContainer fillRow(final int row, final ItemStack item, @Nullable final ClickListener listener) {
        for (int i = row * 9; i < (row + 1) * 9; i++) {
            this.items[i] = item;
            this.clickListener[i] = listener;
        }
        return this;
    }

    public ItemContainer fillColumn(final int column, final ItemStack item) {
        return this.fillColumn(column, item, null);
    }

    public ItemContainer fillColumn(final int column, final ItemStack item, @Nullable final ClickListener listener) {
        for (int i = column; i < this.items.length; i += 9) {
            this.items[i] = item;
            this.clickListener[i] = listener;
        }
        return this;
    }

    public ItemContainer set(final int slot, final ItemStack item) {
        return this.set(slot, item, null);
    }

    public ItemContainer set(final int slot, final ItemStack item, @Nullable final ClickListener listener) {
        this.items[slot] = item;
        this.clickListener[slot] = listener;
        return this;
    }

}
