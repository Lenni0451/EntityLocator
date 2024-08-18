package net.lenni0451.entitylocator.inventory.impl;

import net.lenni0451.entitylocator.inventory.simple.ItemContainer;
import net.lenni0451.entitylocator.inventory.simple.PagedInventory;
import net.lenni0451.entitylocator.model.EntityCollection;
import net.lenni0451.entitylocator.utils.ItemBuilder;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.lenni0451.entitylocator.inventory.simple.ClickListener.left;

public class EntityListInventory extends PagedInventory {

    private static final int ITEMS_PER_PAGE = 9 * 5;

    private final List<EntityCollection> entities;

    public EntityListInventory(final List<EntityCollection> entities) {
        super("§5EntityLocator §7| §a" + entities.stream().mapToInt(EntityCollection::count).sum() + " Entities", 6, (int) Math.ceil((double) entities.size() / ITEMS_PER_PAGE));
        this.entities = entities;
    }

    @Override
    protected void init(Player player, ItemContainer items, int page) {
        List<EntityCollection> slice = this.getPage(this.entities, page);
        for (int i = 0; i < slice.size(); i++) {
            EntityCollection entity = slice.get(i);
            items.set(i, this.createEntityItem(entity), left(() -> {
                Chunk chunk = entity.world().getChunkAt(entity.chunkLocation().x(), entity.chunkLocation().z());
                Location location = chunk.getBlock(0, 0, 0).getLocation();
                location.add(0.5, chunk.getWorld().getHighestBlockYAt(location) + 1, 0.5);
                player.teleport(location);
            }));
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
                        "§aTypes: §6" + String.join("§7, §6", entity.types().stream().map(e -> e.count() + "x " + e.type().name()).toList())
                )
                .build();
    }

}
