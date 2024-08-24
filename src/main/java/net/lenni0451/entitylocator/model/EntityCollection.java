package net.lenni0451.entitylocator.model;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public record EntityCollection(World world, ChunkLocation chunkLocation, List<CountedEntity> entities, int count) {

    private static final Particle PARTICLE = Particle.REDSTONE;
    private static final int COUNT = 1;
    private static final Object DATA = new Particle.DustOptions(Color.RED, 2);

    public void teleport(final Player player) {
        Chunk chunk = this.world.getChunkAt(this.chunkLocation.x(), this.chunkLocation.z());
        Location location = chunk.getBlock(0, 0, 0).getLocation();
        location.add(0.5, chunk.getWorld().getHighestBlockYAt(location) + 1, 0.5);
        player.teleport(location);

        for (int x = 0; x <= 64; x++) {
            player.spawnParticle(PARTICLE, location.clone().add(x / 4D, 0.5, 0), COUNT, 0, 0, 0, DATA);
            player.spawnParticle(PARTICLE, location.clone().add(x / 4D, 0.5, 16), COUNT, 0, 0, 0, DATA);
        }
        for (int z = 0; z <= 64; z++) {
            player.spawnParticle(PARTICLE, location.clone().add(0, 0.5, z / 4D), COUNT, 0, 0, 0, DATA);
            player.spawnParticle(PARTICLE, location.clone().add(16, 0.5, z / 4D), COUNT, 0, 0, 0, DATA);
        }
    }

    public void remove() {
        this.entities
                .stream()
                .flatMap(e -> e.entities().stream())
                .filter(e -> !(e instanceof Player))
                .forEach(Entity::remove);
    }

}
