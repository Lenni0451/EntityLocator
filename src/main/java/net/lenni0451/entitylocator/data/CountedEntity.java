package net.lenni0451.entitylocator.data;

import org.bukkit.entity.EntityType;

public record CountedEntity(EntityType type, int count) {
}
