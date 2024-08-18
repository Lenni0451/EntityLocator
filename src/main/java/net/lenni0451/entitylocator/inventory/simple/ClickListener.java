package net.lenni0451.entitylocator.inventory.simple;

import net.lenni0451.entitylocator.inventory.ClickType;

@FunctionalInterface
public interface ClickListener {

    static ClickListener left(final Runnable runnable) {
        return clickType -> {
            if (ClickType.LEFT.equals(clickType)) runnable.run();
        };
    }

    static ClickListener right(final Runnable runnable) {
        return clickType -> {
            if (ClickType.RIGHT.equals(clickType)) runnable.run();
        };
    }


    void onClick(final ClickType clickType);

}
