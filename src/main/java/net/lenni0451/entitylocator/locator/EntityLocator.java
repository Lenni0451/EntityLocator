package net.lenni0451.entitylocator.locator;

import net.lenni0451.entitylocator.model.ChunkLocation;
import net.lenni0451.entitylocator.model.CountedEntity;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EntityLocator {

    public static Map<ChunkLocation, List<Entity>> getEntitiesInChunks(final World world, final Predicate<EntityType> filter) {
        Map<ChunkLocation, List<Entity>> chunkEntities = new HashMap<>();
        for (Entity entity : world.getEntities()) {
            if (!filter.test(entity.getType())) continue;
            ChunkLocation chunkLocation = ChunkLocation.of(entity.getLocation());
            chunkEntities.computeIfAbsent(chunkLocation, k -> new ArrayList<>()).add(entity);
        }
        return chunkEntities;
    }

    public static List<CountedEntity> countEntities(final List<Entity> entities) {
        return entities
                .stream()
                .collect(Collectors.groupingBy(Entity::getType, Collectors.toList()))
                .entrySet().stream()
                .map(e -> new CountedEntity(e.getKey(), e.getValue()))
                .sorted((o1, o2) -> Integer.compare(o2.entities().size(), o1.entities().size()))
                .toList();
    }

}
