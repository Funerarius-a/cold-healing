package net.funerarius.coldhealing.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class StimPouchItem extends Item {
    public static final int MAX_SLOTS = 6;

    public StimPouchItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pouch, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY) return false;

        ItemStack itemInSlot = slot.getItem();
        if (itemInSlot.isEmpty()) {
            removeLastItem(pouch).ifPresent(slot::set);
            return true;
        } else {
            if (isValidStim(itemInSlot) && addItem(pouch, itemInSlot)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pouch, ItemStack itemInHand, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) return false;

        if (itemInHand.isEmpty()) {
            removeLastItem(pouch).ifPresent(access::set);
            return true;
        } else {
            if (isValidStim(itemInHand) && addItem(pouch, itemInHand)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidStim(ItemStack stack) {

        return true;
    }

    private static boolean addItem(ItemStack pouch, ItemStack newItem) {
        if (newItem.isEmpty()) return false;

        CompoundTag tag = pouch.getOrCreateTag();
        if (!tag.contains("Items")) {
            tag.put("Items", new ListTag());
        }
        ListTag items = tag.getList("Items", 10);

        if (items.size() >= MAX_SLOTS) return false;

        ItemStack toInsert = newItem.split(1);
        CompoundTag itemTag = new CompoundTag();
        toInsert.save(itemTag);
        items.add(0, itemTag);

        return true;
    }

    private static Optional<ItemStack> removeLastItem(ItemStack pouch) {
        CompoundTag tag = pouch.getOrCreateTag();
        if (!tag.contains("Items")) return Optional.empty();

        ListTag items = tag.getList("Items", 10);
        if (items.isEmpty()) return Optional.empty();

        CompoundTag itemTag = items.getCompound(0);
        ItemStack stack = ItemStack.of(itemTag);
        items.remove(0);

        return Optional.of(stack);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        NonNullList<ItemStack> list = NonNullList.create();

        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("Items")) {
            ListTag itemsTag = tag.getList("Items", 10);
            for (int i = 0; i < itemsTag.size(); i++) {
                list.add(ItemStack.of(itemsTag.getCompound(i)));
            }
        }

        return Optional.of(new StimPouchTooltip(list));
    }

    @Override
    public boolean isBarVisible(ItemStack pouch) {
        return getItemsCount(pouch) > 0;
    }

    @Override
    public int getBarWidth(ItemStack pouch) {
        return Math.min(13 * getItemsCount(pouch) / MAX_SLOTS, 13);
    }

    @Override
    public int getBarColor(ItemStack pouch) {
        return 0x575757;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.coldhealing.stim_pouch.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public static int getItemsCount(ItemStack pouch) {
        CompoundTag tag = pouch.getTag();
        if (tag == null || !tag.contains("Items")) return 0;
        return tag.getList("Items", 10).size();
    }
}