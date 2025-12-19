package net.lenni0451.entitylocator.command;

import com.google.gson.JsonObject;
import net.lenni0451.entitylocator.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

@ParametersAreNonnullByDefault
public class EntityDumpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("entitylocator.use")) {
            sender.sendMessage("§cYou do not have permission to use this command!");
            return true;
        }

        File dumpFile = new File(Main.getInstance().getDataFolder(), "dump_" + System.currentTimeMillis() + ".jsonl");
        dumpFile.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(dumpFile)) {
            int count = 0;
            int failed = 0;
            long start = System.nanoTime();
            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    try {
                        JsonObject entityData = new JsonObject();
                        this.add(entityData, "id", entity.getEntityId());
                        this.add(entityData, "uuid", entity.getUniqueId().toString());
                        this.add(entityData, "type", entity.getType().name());
                        this.add(entityData, "world", world.getName());
                        this.add(entityData, "x", entity.getLocation().getX());
                        this.add(entityData, "y", entity.getLocation().getY());
                        this.add(entityData, "z", entity.getLocation().getZ());
                        this.add(entityData, "ticks_lived", entity.getTicksLived());
                        this.add(entityData, "name", entity.getName());
                        this.add(entityData, "custom_name", entity.getCustomName());
                        this.add(entityData, "is_dead", entity.isDead());
                        this.add(entityData, "is_empty", entity.isEmpty());
                        this.add(entityData, "is_invulnerable", entity.isInvulnerable());
                        this.add(entityData, "is_persistent", entity.isPersistent());
                        this.add(entityData, "is_valid", entity.isValid());
                        this.add(entityData, "is_in_world", entity.isInWorld());
                        this.add(entityData, "is_dead", entity.isDead());
                        if (entity instanceof LivingEntity livingEntity) {
                            this.add(entityData, "is_collidable", livingEntity.isCollidable());
                            this.add(entityData, "has_ai", livingEntity.hasAI());
                        }
                        fos.write((entityData + "\n").getBytes(StandardCharsets.UTF_8));
                        count++;
                    } catch (Throwable t) {
                        Main.getInstance().getLogger().log(Level.SEVERE, "Failed to get entity data for " + entity, t);
                        failed++;
                    }
                }
            }
            long end = System.nanoTime();
            sender.sendMessage("§aSuccessfully dumped " + count + " (" + failed + " failed) entities to " + dumpFile.getName() + " in " + ((end - start) / 1_000_000) + " ms.");
        } catch (Throwable t) {
            Main.getInstance().getLogger().log(Level.SEVERE, "Failed to dump entities", t);
            sender.sendMessage("§cAn error occurred while dumping entities: " + t.getMessage());
        }
        return true;
    }

    private void add(final JsonObject object, final String key, @Nullable final String value) {
        if (value != null) {
            object.addProperty(key, value);
        }
    }

    private void add(final JsonObject object, final String key, @Nullable final Number value) {
        if (value != null) {
            object.addProperty(key, value);
        }
    }

    private void add(final JsonObject object, final String key, @Nullable final Boolean value) {
        if (value != null) {
            object.addProperty(key, value);
        }
    }

}
