package net.lenni0451.entitylocator.data;

import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.util.Set;

public record EntityCollection(World world, ChunkLocation chunkLocation, Set<EntityType> types, int count) {
}
