package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.ModConfigs;
import net.funerarius.coldhealing.client.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class IbuprofenItem extends BaseHealingItem {

    public IbuprofenItem(Properties properties) {
        super(properties);
    }

    @Override public int getUseTicks(ItemStack stack) { return 40; }
    @Override public SoundEvent getOpenSound() { return ModSounds.PILLS_BOTTLE_OPEN.get(); }
    @Override public SoundEvent getUseSound(ItemStack stack) { return ModSounds.PILLS_BOTTLE_USE.get(); }
    @Override public SoundEvent getFinishSound(ItemStack stack) { return ModSounds.PILLS_BOTTLE_CLOSE.get(); }
    @Override public int getItemBarColor(ItemStack stack) { return 0xf2591d; }
    @Override public String getTooltipKey() { return "tooltip.coldhealing.ibuprofen.tooltip"; }

    @Override public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.DRINK; }
    @Override public SoundEvent getDrinkingSound() { return SoundEvents.EMPTY; }
    @Override public boolean playBreakSound() { return false; }

    @Override
    public boolean canUse(Level level, Player player, ItemStack stack) {
        if (ModConfigs.hasIncompatibleEffect(player, ModConfigs.IBUPROFEN_INCOMPATIBLE.get())) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.coldhealing.incompatible_effect"), true);
            }
            return false; // Cannot use when incompatible effect(s) are present
        }
        return true;
    }

    @Override
    public void applyCureServer(LivingEntity entity, ItemStack stack) {
        ModConfigs.removeConfiguredEffects(entity, ModConfigs.IBUPROFEN_REMOVE.get());
        ModConfigs.addConfiguredEffects(entity, ModConfigs.IBUPROFEN_ADD.get());
    }
}