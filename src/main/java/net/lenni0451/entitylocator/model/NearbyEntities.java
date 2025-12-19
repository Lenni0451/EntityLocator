package net.lenni0451.entitylocator.model;

import org.bukkit.entity.Entity;

import java.util.List;

public record NearbyEntities(Entity entity, List<CountedEntity> entities) {
}
