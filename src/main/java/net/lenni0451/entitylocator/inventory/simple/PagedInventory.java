package net.lenni0451.entitylocator.inventory.simple;

import net.lenni0451.entitylocator.Main;
import net.lenni0451.entitylocator.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.lenni0451.entitylocator.inventory.simple.ClickListener.left;

public abstract class PagedInventory extends SimpleInventory {

    private final Map<Player, Integer> pages;
    private final int pageCount;

    public PagedInventory(final String title, final int rows, final int pageCount) {
        super(title, rows);
        this.pages = new HashMap<>();
        this.pageCount = pageCount;
    }

    protected abstract void init(final Player player, final ItemContainer items, final int page);

    @Override
    protected void init(Player player, ItemContainer items) {
        int page = this.pages.getOrDefault(player, 0);
        this.init(player, items, page);
        this.initNavBar(player, items, page);
    }

    protected void initNavBar(final Player player, final ItemContainer items, final int page) {
        items.fillRow(this.getRows() - 1, ItemBuilder.create(Material.BLACK_STAINED_GLASS_PANE).name(" ").build());

        boolean hasPrevious = page > 0;
        boolean hasNext = page < this.pageCount - 1;
        if (hasPrevious) {
            String name = "§aPrevious Page";
            int amount;
            if (page <= 64) {
                amount = page;
            } else {
                name += " §7(" + page + "/" + this.pageCount + ")";
                amount = 1;
            }
            items.set(this.getRows() * 9 - 9, ItemBuilder.create(Material.ARROW).name(name).amount(amount).build(), left(() -> {
                this.pages.put(player, page - 1);
                Main.getInstance().getInventoryManager().refreshInventory(player);
            }));
        }
        if (hasNext) {
            String name = "§aNext Page";
            int amount;
            if ((page + 2) <= 64) {
                amount = page + 2;
            } else {
                name += " §7(" + (page + 2) + "/" + this.pageCount + ")";
                amount = 1;
            }
            items.set(this.getRows() * 9 - 1, ItemBuilder.create(Material.ARROW).name(name).amount(amount).build(), left(() -> {
                this.pages.put(player, page + 1);
                Main.getInstance().getInventoryManager().refreshInventory(player);
            }));
        }
    }

    protected <T> List<T> getPage(final List<T> list, final int page) {
        int itemsPerPage = this.getRows() * 9 - 9;
        int start = page * itemsPerPage;
        int end = Math.min(list.size(), (page + 1) * itemsPerPage);
        return list.subList(start, end);
    }

    @Override
    public void close(Player player) {
        super.close(player);
        this.pages.remove(player);
    }

}
