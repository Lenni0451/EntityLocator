package net.lenni0451.entitylocator.model;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public record EntityCollection(World world, ChunkLocation chunkLocation, List<CountedEntity> entities, int count) {

    public void teleport(final Player player) {
        Chunk chunk = this.world.getChunkAt(this.chunkLocation.x(), this.chunkLocation.z());
        Location location = chunk.getBlock(0, 0, 0).getLocation();
        location.add(0.5, chunk.getWorld().getHighestBlockYAt(location) + 1, 0.5);
        player.teleport(location);
    }

    public void remove() {
        this.entities
                .stream()
                .flatMap(e -> e.entities().stream())
                .filter(e -> !(e instanceof Player))
                .forEach(Entity::remove);
    }

}
