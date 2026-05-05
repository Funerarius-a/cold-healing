package net.funerarius.coldhealing.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class StimPouchTooltip implements TooltipComponent {
    private final NonNullList<ItemStack> items;

    public StimPouchTooltip(NonNullList<ItemStack> items) {
        this.items = items;
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }
}