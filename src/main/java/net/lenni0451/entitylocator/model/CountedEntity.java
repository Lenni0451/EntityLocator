package net.lenni0451.entitylocator.model;

import org.bukkit.entity.EntityType;

public record CountedEntity(EntityType type, int count) {
}
