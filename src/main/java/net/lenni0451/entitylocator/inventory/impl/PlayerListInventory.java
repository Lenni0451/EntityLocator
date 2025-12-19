package net.lenni0451.entitylocator.inventory.impl;

import net.lenni0451.entitylocator.inventory.ClickType;
import net.lenni0451.entitylocator.inventory.simple.ItemContainer;
import net.lenni0451.entitylocator.inventory.simple.PagedInventory;
import net.lenni0451.entitylocator.model.ChunkLocation;
import net.lenni0451.entitylocator.model.CountedEntity;
import net.lenni0451.entitylocator.model.NearbyEntities;
import net.lenni0451.entitylocator.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerListInventory extends PagedInventory {

    private static final int ITEMS_PER_PAGE = 9 * 5;
    private static final int ENTITY_TYPE_GROUP_COUNT = 5;

    private final List<NearbyEntities> nearbyEntities;

    public PlayerListInventory(final List<NearbyEntities> nearbyEntities) {
        super(
                "§5PlayerLocator §7| §a" + nearbyEntities.size() + " Players",
                6,
                (int) Math.ceil((double) nearbyEntities.size() / ITEMS_PER_PAGE)
        );
        this.nearbyEntities = nearbyEntities;
    }

    @Override
    protected void init(Player player, ItemContainer items, int page) {
        List<NearbyEntities> slice = this.getPage(this.nearbyEntities, page);
        for (NearbyEntities nearbyEntities : slice) {
            items.add(this.createPlayerItem((Player) nearbyEntities.entity(), nearbyEntities.entities()), clickType -> {
                if (ClickType.LEFT.equals(clickType)) {
                    player.teleport(nearbyEntities.entity());
                }
            });
        }
    }

    private ItemStack createPlayerItem(final Player player, final List<CountedEntity> entities) {
        int entityCount = entities.stream().mapToInt(c -> c.entities().size()).sum();
        ChunkLocation playerChunkLocation = ChunkLocation.of(player.getLocation());
        List<String> lore = new ArrayList<>();
        lore.add("§aWorld: §6" + player.getWorld().getName());
        lore.add("§aChunk: §6" + playerChunkLocation.x() + "§7/§6" + playerChunkLocation.z());
        lore.add("§aTypes:");
        for (CountedEntity[] countedEntities : this.groupEntities(entities)) {
            StringBuilder line = new StringBuilder("§6");
            for (int i = 0; i < countedEntities.length; i++) {
                CountedEntity countedEntity = countedEntities[i];
                line
                        .append("§e")
                        .append(countedEntity.entities().size())
                        .append("§7x§6")
                        .append(countedEntity.type().name());
                if (i < countedEntities.length - 1) line.append("§7, §6");
            }
            lore.add(line.toString());
        }
        lore.add("");
        lore.add("§7Left-click to teleport");
        return ItemBuilder
                .create(Material.PLAYER_HEAD)
                .name("§6" + player.getName() + " §7| §a" + entityCount + " Entities nearby")
                .amount(Math.min(64, entityCount))
                .lore(lore.toArray(new String[0]))
                .meta(meta -> {
                    SkullMeta skullMeta = (SkullMeta) meta;
                    skullMeta.setOwningPlayer(player);
                    return skullMeta;
                })
                .build();
    }

    private List<CountedEntity[]> groupEntities(final List<CountedEntity> entities) {
        int groupCount = (int) Math.ceil((float) entities.size() / ENTITY_TYPE_GROUP_COUNT);
        List<CountedEntity[]> groupedEntityTypes = new ArrayList<>(groupCount);
        for (int i = 0; i < groupCount; i++) {
            int fromIndex = i * ENTITY_TYPE_GROUP_COUNT;
            int toIndex = Math.min(fromIndex + ENTITY_TYPE_GROUP_COUNT, entities.size());
            CountedEntity[] group = new CountedEntity[toIndex - fromIndex];
            for (int j = fromIndex; j < toIndex; j++) {
                group[j - fromIndex] = entities.get(j);
            }
            groupedEntityTypes.add(group);
        }
        return groupedEntityTypes;
    }

}
