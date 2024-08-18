package net.lenni0451.entitylocator.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    public static ItemBuilder create(final Material material) {
        return new ItemBuilder(material);
    }


    private final Material material;
    private String name = null;
    private int amount = 1;
    private List<String> lore = null;
    private boolean glow = false;

    private ItemBuilder(final Material material) {
        this.material = material;
    }

    public ItemBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder amount(final int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder lore(final String... lore) {
        this.lore = List.of(lore);
        return this;
    }

    public ItemBuilder lore(final List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder glow(final boolean glow) {
        this.glow = glow;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(this.material, this.amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return itemStack;

        itemMeta.addItemFlags(ItemFlag.values());
        if (this.name != null) itemMeta.setDisplayName(this.name);
        if (this.lore != null) itemMeta.setLore(this.lore);
        if (this.glow) itemMeta.addEnchant(Enchantment.values()[0], 1, true);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
