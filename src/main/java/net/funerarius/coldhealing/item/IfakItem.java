package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.ModConfigs;
import net.funerarius.coldhealing.client.ModSounds;
import net.funerarius.coldhealing.compat.LsoCompat;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class IfakItem extends BaseHealingItem {

    public IfakItem(Properties properties) {
        super(properties);
    }

    @Override public int getUseTicks(ItemStack stack) { return 25; }
    @Override public SoundEvent getOpenSound() { return ModSounds.OPEN_GENERIC.get(); }
    @Override public SoundEvent getUseSound(ItemStack stack) { return ModSounds.BANDAGE_USE.get(); }
    @Override public SoundEvent getFinishSound(ItemStack stack) { return ModSounds.BANDAGE_FINISH.get(); }
    @Override public int getItemBarColor(ItemStack stack) { return 0x0088FF; }
    @Override public String getTooltipKey() { return "tooltip.coldhealing.ifak.tooltip"; }

    @Override
    public void applyCureServer(LivingEntity entity, ItemStack stack) {
        entity.heal(1.0F);

        ModConfigs.removeConfiguredEffects(entity, ModConfigs.IFAK_REMOVE.get());
        ModConfigs.addConfiguredEffects(entity, ModConfigs.IFAK_ADD.get());

        if (entity instanceof Player player) {
            LsoCompat.healMostDamagedLimb(player, 0.1F, 60);
        }
    }
}