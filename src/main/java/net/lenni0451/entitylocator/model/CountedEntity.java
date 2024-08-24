package net.lenni0451.entitylocator.model;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.List;

public record CountedEntity(EntityType type, List<Entity> entities) {
}
