package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.ModConfigs;
import net.funerarius.coldhealing.client.ModSounds;
import net.funerarius.coldhealing.compat.LsoCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SplintItem extends BaseHealingItem {

    public SplintItem(Properties properties) {
        super(properties);
    }

    @Override public int getUseTicks(ItemStack stack) { return 80; }
    @Override public SoundEvent getOpenSound() { return ModSounds.SPLINT_START.get(); }
    @Override public SoundEvent getUseSound(ItemStack stack) { return ModSounds.SPLINT_USE.get(); }
    @Override public SoundEvent getFinishSound(ItemStack stack) { return ModSounds.SPLINT_END.get(); }
    @Override public int getItemBarColor(ItemStack stack) { return 0xe82210; }
    @Override public String getTooltipKey() { return "tooltip.coldhealing.splint.tooltip"; }

    @Override public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.BRUSH; }

    @Override
    public boolean canUse(Level level, Player player, ItemStack stack) {
        if (ModConfigs.hasIncompatibleEffect(player, ModConfigs.SPLINT_INCOMPATIBLE.get())) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.coldhealing.incompatible_effect"), true);
            }
            return false;
        }
        return true;
    }

    @Override
    public void applyCureServer(LivingEntity entity, ItemStack stack) {
        ModConfigs.removeConfiguredEffects(entity, ModConfigs.SPLINT_REMOVE.get());
        ModConfigs.addConfiguredEffects(entity, ModConfigs.SPLINT_ADD.get());

        if (entity instanceof Player player) {
            LsoCompat.tryRestoreSpecificBrokenLimbs(player, "LEFT_LEG", "RIGHT_LEG", "LEFT_ARM", "RIGHT_ARM");
        }
    }
}