package net.lenni0451.entitylocator;

import net.lenni0451.entitylocator.data.ChunkLocation;
import net.lenni0451.entitylocator.data.CountedEntity;
import net.lenni0451.entitylocator.data.EntityCollection;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityLocator {

    public static List<EntityCollection> locateEntities() {
        List<EntityCollection> entities = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) entities.addAll(collectEntities(world, getWorldEntities(world)));
        sort(entities);
        return entities;
    }

    private static Map<ChunkLocation, List<Entity>> getWorldEntities(final World world) {
        Map<ChunkLocation, List<Entity>> chunkEntities = new HashMap<>();
        for (Entity entity : world.getEntities()) {
            Chunk chunk = entity.getLocation().getChunk();
            ChunkLocation chunkLocation = new ChunkLocation(chunk.getX(), chunk.getZ());
            chunkEntities.computeIfAbsent(chunkLocation, k -> new ArrayList<>()).add(entity);
        }
        return chunkEntities;
    }

    private static List<EntityCollection> collectEntities(final World world, final Map<ChunkLocation, List<Entity>> chunkEntities) {
        List<EntityCollection> entities = new ArrayList<>();
        for (Map.Entry<ChunkLocation, List<Entity>> entry : chunkEntities.entrySet()) {
            List<CountedEntity> countedEntities = entry
                    .getValue()
                    .stream()
                    .collect(Collectors.groupingBy(Entity::getType, Collectors.summingInt(e -> 1)))
                    .entrySet()
                    .stream()
                    .map(e -> new CountedEntity(e.getKey(), e.getValue()))
                    .sorted((o1, o2) -> Integer.compare(o2.count(), o1.count()))
                    .toList();

            entities.add(new EntityCollection(world, entry.getKey(), countedEntities, entry.getValue().size()));
        }
        return entities;
    }

    private static void sort(final List<EntityCollection> entities) {
        entities.sort((o1, o2) -> Integer.compare(o2.count(), o1.count()));
    }

}
