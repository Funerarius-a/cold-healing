package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.ModConfigs;
import net.funerarius.coldhealing.client.ModSounds;
import net.funerarius.coldhealing.compat.LsoCompat;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class AfakItem extends BaseHealingItem {

    public AfakItem(Properties properties) {
        super(properties);
    }

    private boolean isStage1(ItemStack stack) {
        return (stack.getMaxDamage() - stack.getDamageValue()) > 1;
    }

    @Override public int getUseTicks(ItemStack stack) { return isStage1(stack) ? 50 : 80; }
    @Override public SoundEvent getOpenSound() { return ModSounds.OPEN_GENERIC.get(); }
    @Override public SoundEvent getUseSound(ItemStack stack) { return isStage1(stack) ? ModSounds.BANDAGE_USE.get() : ModSounds.INJECTOR_KIT_USE.get(); }
    @Override public SoundEvent getFinishSound(ItemStack stack) { return isStage1(stack) ? ModSounds.BANDAGE_FINISH.get() : ModSounds.INJECTOR_PUTAWAY.get(); }
    @Override public int getItemBarColor(ItemStack stack) { return 0x0088FF; }
    @Override public String getTooltipKey() { return "tooltip.coldhealing.afak.tooltip"; }

    @Override public boolean isContinuousUse() { return true; }

    @Override
    public void applyCureServer(LivingEntity entity, ItemStack stack) {
        if (isStage1(stack)) {
            entity.heal(1.0F);
            ModConfigs.removeConfiguredEffects(entity, ModConfigs.AFAK_STAGE1_REMOVE.get());
            ModConfigs.addConfiguredEffects(entity, ModConfigs.AFAK_STAGE1_ADD.get());

            if (entity instanceof Player player) {
                LsoCompat.healMostDamagedLimb(player, 0.05F, 60);
            }
        } else {
            entity.heal(6.0F);
            ModConfigs.removeConfiguredEffects(entity, ModConfigs.AFAK_STAGE2_REMOVE.get());
            ModConfigs.addConfiguredEffects(entity, ModConfigs.AFAK_STAGE2_ADD.get());

            if (entity instanceof Player player) {
                LsoCompat.healAllLimbs(player, 0.3F, 200);
            }
        }
    }
}