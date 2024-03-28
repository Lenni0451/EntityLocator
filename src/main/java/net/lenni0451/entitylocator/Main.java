package net.lenni0451.entitylocator;

import net.lenni0451.entitylocator.command.EntityLocatorCommand;
import net.lenni0451.entitylocator.listener.InventoryHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }


    private InventoryHandler inventoryHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.inventoryHandler = new InventoryHandler();

        Bukkit.getPluginManager().registerEvents(this.inventoryHandler, this);
        this.getCommand("entitylocator").setExecutor(new EntityLocatorCommand());
    }

    public InventoryHandler getInventoryHandler() {
        return this.inventoryHandler;
    }

}
