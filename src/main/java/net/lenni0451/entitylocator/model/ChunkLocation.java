package net.lenni0451.entitylocator.model;

import org.bukkit.Location;

public record ChunkLocation(int x, int z) {

    public static ChunkLocation of(final Location location) {
        return new ChunkLocation(location.getChunk().getX(), location.getChunk().getZ());
    }

    public ChunkLocation add(final int xOffset, final int zOffset) {
        return new ChunkLocation(this.x + xOffset, this.z + zOffset);
    }

}
