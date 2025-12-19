package net.lenni0451.entitylocator.utils;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.UnaryOperator;

public class ItemBuilder {

    public static ItemBuilder create(final Material material) {
        return new ItemBuilder(material);
    }


    private final Material material;
    private String name = null;
    private int amount = 1;
    private List<String> lore = null;
    private boolean glow = false;
    private UnaryOperator<ItemMeta> metaOperator = null;

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

    public ItemBuilder meta(final UnaryOperator<ItemMeta> metaOperator) {
        this.metaOperator = metaOperator;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(this.material, this.amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return itemStack;

        itemMeta.addItemFlags(ItemFlag.values());
        if (this.name != null) itemMeta.setDisplayName(this.name);
        if (this.lore != null) itemMeta.setLore(this.lore);
        if (this.glow) itemMeta.addEnchant(Registry.ENCHANTMENT.iterator().next(), 1, true);
        if (this.metaOperator != null) itemMeta = this.metaOperator.apply(itemMeta);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
