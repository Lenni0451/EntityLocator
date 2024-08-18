package net.lenni0451.entitylocator;

import net.lenni0451.entitylocator.command.EntityLocatorCommand;
import net.lenni0451.entitylocator.inventory.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }


    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        instance = this;

        this.inventoryManager = new InventoryManager();

        Bukkit.getPluginManager().registerEvents(this.inventoryManager, this);
        this.getCommand("entitylocator").setExecutor(new EntityLocatorCommand());
    }

    @Override
    public void onDisable() {
        this.inventoryManager.closeAll();
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

}
