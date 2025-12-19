package net.lenni0451.entitylocator.inventory.impl;

import net.lenni0451.entitylocator.Main;
import net.lenni0451.entitylocator.inventory.simple.ItemContainer;
import net.lenni0451.entitylocator.inventory.simple.PagedInventory;
import net.lenni0451.entitylocator.model.EntityCollection;
import net.lenni0451.entitylocator.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EntityListInventory extends PagedInventory {

    private static final int ITEMS_PER_PAGE = 9 * 5;

    private final List<EntityCollection> entities;

    public EntityListInventory(final List<EntityCollection> entities) {
        super(
                "§5EntityLocator §7| §a" + entities.stream().mapToInt(EntityCollection::count).sum() + " Entities",
                6,
                (int) Math.ceil((double) entities.size() / ITEMS_PER_PAGE)
        );
        this.entities = entities;
    }

    @Override
    protected void init(Player player, ItemContainer items, int page) {
        List<EntityCollection> slice = this.getPage(this.entities, page);
        for (EntityCollection entity : slice) {
            items.add(this.createEntityItem(entity), clickType -> {
                switch (clickType) {
                    case LEFT -> entity.teleport(player);
                    case RIGHT -> Main.getInstance().getInventoryManager().pushInventory(player, new EntityActionInventory(entity));
                }
            });
        }
    }

    private ItemStack createEntityItem(final EntityCollection entity) {
        return ItemBuilder
                .create(Material.SPAWNER)
                .name("§6" + entity.world().getName() + " §7| §e" + entity.chunkLocation().x() + "§7/§e" + entity.chunkLocation().z() + " §7| §a" + entity.count() + " Entities")
                .amount(Math.min(64, entity.count()))
                .lore(
                        "§aWorld: §6" + entity.world().getName(),
                        "§aChunk: §6" + entity.chunkLocation().x() + "§7/§6" + entity.chunkLocation().z(),
                        "§aEntities: §6" + entity.count(),
                        "§aTypes: §6" + String.join("§7, §6", entity.entities().stream().map(e -> e.entities().size() + "x " + e.type().name()).toList()),
                        "",
                        "§7Left-click to teleport",
                        "§7Right-click to perform action"
                )
                .build();
    }

}
