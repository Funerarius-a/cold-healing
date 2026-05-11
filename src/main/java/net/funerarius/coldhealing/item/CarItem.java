package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.ModConfigs;
import net.funerarius.coldhealing.client.ModSounds;
import net.funerarius.coldhealing.compat.LsoCompat;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CarItem extends BaseHealingItem {

    public CarItem(Properties properties) {
        super(properties);
    }

    private boolean isBandageMode(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("IsBandageMode");
    }

// Initial check
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

// Save current mode
        boolean hasDebuff = ModConfigs.hasRemovableEffect(player, ModConfigs.CAR_BANDAGE_REMOVE.get());
        stack.getOrCreateTag().putBoolean("IsBandageMode", hasDebuff);

// Return values
        return super.use(level, player, hand);
    }

    @Override public int getUseTicks(ItemStack stack) { return isBandageMode(stack) ? 40 : 60; }
    @Override public SoundEvent getOpenSound() { return ModSounds.OPEN_GENERIC.get(); }
    @Override public SoundEvent getUseSound(ItemStack stack) { return isBandageMode(stack) ? ModSounds.BANDAGE_USE.get() : ModSounds.INJECTOR_KIT_USE.get(); }
    @Override public SoundEvent getFinishSound(ItemStack stack) { return isBandageMode(stack) ? ModSounds.BANDAGE_FINISH.get() : ModSounds.INJECTOR_PUTAWAY.get(); }
    @Override public int getItemBarColor(ItemStack stack) { return 0x2de03c; }
    @Override public String getTooltipKey() { return "tooltip.coldhealing.car.tooltip"; }

    @Override public boolean isContinuousUse() { return true; }

    @Override
    public void applyCureServer(LivingEntity entity, ItemStack stack) {

        if (isBandageMode(stack)) {
            entity.heal(1.0F);
            ModConfigs.removeConfiguredEffects(entity, ModConfigs.CAR_BANDAGE_REMOVE.get());
            ModConfigs.addConfiguredEffects(entity, ModConfigs.CAR_BANDAGE_ADD.get());
        } else {
            entity.heal(4.0F);
            ModConfigs.addConfiguredEffects(entity, ModConfigs.CAR_HEAL_ADD.get());

            if (entity instanceof Player player) {
                LsoCompat.healMostDamagedLimb(player, 0.45F, 200);
            }
        }

// Check debuff for next cycle
        boolean nextDebuff = ModConfigs.hasRemovableEffect(entity, ModConfigs.CAR_BANDAGE_REMOVE.get());
        stack.getOrCreateTag().putBoolean("IsBandageMode", nextDebuff);
    }
}