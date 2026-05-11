package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.client.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class MelatoninItem extends BaseHealingItem {

    public MelatoninItem(Properties properties) {
        super(properties);
    }

    @Override public int getUseTicks(ItemStack stack) { return 40; }
    @Override public SoundEvent getOpenSound() { return ModSounds.PILLS_BOTTLE_OPEN.get(); }
    @Override public SoundEvent getUseSound(ItemStack stack) { return ModSounds.PILLS_BOTTLE_USE.get(); }
    @Override public SoundEvent getFinishSound(ItemStack stack) { return ModSounds.PILLS_BOTTLE_CLOSE.get(); }
    @Override public int getItemBarColor(ItemStack stack) { return 0x462085; }
    @Override public String getTooltipKey() { return "tooltip.coldhealing.melatonin.tooltip"; }

    @Override public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.DRINK; }
    @Override public SoundEvent getDrinkingSound() { return SoundEvents.EMPTY; }
    @Override public boolean playBreakSound() { return false; }

    @Override
    public boolean canUse(Level level, Player player, ItemStack stack) {
        return !player.isSleeping();
    }

    @Override
    public void applyCureClient(LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(this, 2400);
        }
    }

    @Override
    public void applyCureServer(LivingEntity entity, ItemStack stack) {
        if (entity instanceof ServerPlayer serverPlayer) {
            ServerLevel serverLevel = serverPlayer.serverLevel();

            int sleepTicks = 6000 + serverLevel.getRandom().nextInt(3001);

            long currentTime = serverLevel.getDayTime();
            serverPlayer.getPersistentData().putBoolean("MelatoninSleep", true);
            serverPlayer.getPersistentData().putLong("MelatoninTargetTime", currentTime + sleepTicks);
            serverPlayer.getPersistentData().putLong("MelatoninStartTime", currentTime);

            serverPlayer.startSleeping(serverPlayer.blockPosition());
            serverLevel.updateSleepingPlayerList();

// Cooldown
            serverPlayer.getCooldowns().addCooldown(this, 2400);
        }
    }
}