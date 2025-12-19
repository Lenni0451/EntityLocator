package net.lenni0451.entitylocator.locator;

import net.lenni0451.entitylocator.model.ChunkLocation;
import net.lenni0451.entitylocator.model.CountedEntity;
import net.lenni0451.entitylocator.model.EntityCollection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class PerChunkLocator {

    public static List<EntityCollection> locateEntities(final Predicate<EntityType> filter) {
        List<EntityCollection> entities = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            entities.addAll(collectEntities(world, EntityLocator.getEntitiesInChunks(world, filter)));
        }
        sort(entities);
        return entities;
    }

    private static List<EntityCollection> collectEntities(final World world, final Map<ChunkLocation, List<Entity>> chunkEntities) {
        List<EntityCollection> entities = new ArrayList<>();
        for (Map.Entry<ChunkLocation, List<Entity>> entry : chunkEntities.entrySet()) {
            List<CountedEntity> countedEntities = EntityLocator.countEntities(entry.getValue());
            entities.add(new EntityCollection(world, entry.getKey(), countedEntities, entry.getValue().size()));
        }
        return entities;
    }

    private static void sort(final List<EntityCollection> entities) {
        entities.sort((o1, o2) -> Integer.compare(o2.count(), o1.count()));
    }

}
