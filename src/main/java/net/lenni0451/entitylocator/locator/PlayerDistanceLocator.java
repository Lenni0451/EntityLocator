package net.lenni0451.entitylocator.locator;

import net.lenni0451.entitylocator.model.ChunkLocation;
import net.lenni0451.entitylocator.model.CountedEntity;
import net.lenni0451.entitylocator.model.NearbyEntities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlayerDistanceLocator {

    public static List<NearbyEntities> locateEntities(final Predicate<EntityType> filter) {
        int viewDistance = Bukkit.getViewDistance();
        List<NearbyEntities> result = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Map<ChunkLocation, List<Entity>> entities = EntityLocator.getEntitiesInChunks(player.getWorld(), entityType -> true);
            ChunkLocation playerChunkLocation = ChunkLocation.of(player.getLocation());
            List<CountedEntity> entitiesNearby = new ArrayList<>();
            for (int x = -viewDistance; x <= viewDistance; x++) {
                for (int z = -viewDistance; z <= viewDistance; z++) {
                    ChunkLocation chunkLocation = playerChunkLocation.add(x, z);
                    entitiesNearby.addAll(EntityLocator.countEntities(entities.getOrDefault(chunkLocation, Collections.emptyList())));
                }
            }
            entitiesNearby.removeIf(entity -> !filter.test(entity.type()));
            result.add(new NearbyEntities(player, mergeEntities(entitiesNearby)));
        }
        result.sort(Comparator.comparingInt((NearbyEntities o) -> o.entities().stream().mapToInt(c -> c.entities().size()).sum()).reversed());
        return result;
    }

    private static List<CountedEntity> mergeEntities(final List<CountedEntity> entities) {
        return entities.stream()
                .collect(Collectors.groupingBy(CountedEntity::type))
                .entrySet().stream()
                .map(m -> new CountedEntity(m.getKey(), m.getValue().stream().flatMap(e -> e.entities().stream()).toList()))
                .sorted((o1, o2) -> Integer.compare(o2.entities().size(), o1.entities().size()))
                .toList();
    }

}
