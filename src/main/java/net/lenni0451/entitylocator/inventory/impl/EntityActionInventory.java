package net.lenni0451.entitylocator.inventory.impl;

import net.lenni0451.entitylocator.Main;
import net.lenni0451.entitylocator.inventory.simple.ItemContainer;
import net.lenni0451.entitylocator.inventory.simple.SimpleInventory;
import net.lenni0451.entitylocator.model.EntityCollection;
import net.lenni0451.entitylocator.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static net.lenni0451.entitylocator.inventory.simple.ClickListener.left;

public class EntityActionInventory extends SimpleInventory {

    private final EntityCollection entity;

    public EntityActionInventory(final EntityCollection entity) {
        super("§6" + entity.count() + "§5 entities selected", 1);
        this.entity = entity;
    }

    @Override
    protected void init(Player player, ItemContainer items) {
        items.add(ItemBuilder.create(Material.ENDER_PEARL).name("§aTeleport").build(), left(() -> {
            this.entity.teleport(player);
        }));
        items.add(ItemBuilder.create(Material.BARRIER).name("§cRemove").lore("§4Warning! This immediately removes all entities in this list!").build(), left(() -> {
            this.entity.remove();
            Main.getInstance().getInventoryManager().closeInventory(player);
            player.sendMessage("§aSuccessfully removed " + this.entity.count() + " entities!");
        }));
        items.set(8, ItemBuilder.create(Material.ARROW).name("§7Back").build(), left(() -> {
            Main.getInstance().getInventoryManager().popInventory(player);
        }));
    }

}
