package net.lenni0451.entitylocator.model;

import org.bukkit.World;

import java.util.List;

public record EntityCollection(World world, ChunkLocation chunkLocation, List<CountedEntity> types, int count) {
}
