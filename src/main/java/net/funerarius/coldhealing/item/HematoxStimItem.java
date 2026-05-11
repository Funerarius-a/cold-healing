package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.ModConfigs;
import net.funerarius.coldhealing.client.ModSounds;
import net.funerarius.coldhealing.compat.LsoCompat;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class HematoxStimItem extends BaseHealingItem implements IStim {

    private final float healthActivationThreshold;

    public HematoxStimItem(Properties properties, float healthActivationThreshold) {
        super(properties);
        this.healthActivationThreshold = healthActivationThreshold;
    }

    @Override public int getUseTicks(ItemStack stack) { return 30; }
    @Override public SoundEvent getOpenSound() { return ModSounds.INJECTOR_START.get(); }
    @Override public SoundEvent getUseSound(ItemStack stack) { return null; }
    @Override public SoundEvent getFinishSound(ItemStack stack) { return null; } // Stims dont have finish sounds
    @Override public int getItemBarColor(ItemStack stack) { return 0x76071f; }
    @Override public String getTooltipKey() { return "tooltip.coldhealing.hematox_g.tooltip"; }

    @Override
    public void applyCureServer(LivingEntity entity, ItemStack stack) {
        applyStimEffects(entity, stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.BOW; }

    @Override
    public boolean playBreakSound() { return false; }

// Stim Pouch Integration

    @Override
    public boolean shouldAutoInject(LivingEntity entity, ItemStack stimStack) {
        if (this.healthActivationThreshold > 0 && entity.getHealth() <= this.healthActivationThreshold) {
            return true;
        }
        return ModConfigs.hasRemovableEffect(entity, ModConfigs.HEMATOX_REMOVE.get());
    }

    @Override
    public boolean applyStimEffects(LivingEntity entity, ItemStack stimStack) {
        entity.heal(10.0F);
        ModConfigs.removeConfiguredEffects(entity, ModConfigs.HEMATOX_REMOVE.get());
        ModConfigs.addConfiguredEffects(entity, ModConfigs.HEMATOX_ADD.get());

        if (entity instanceof Player player) {
            LsoCompat.healAllLimbs(player, 0.25F, 800);
        }

        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                ModSounds.INJECTOR_USE.get(), net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
        return true;
    }
}