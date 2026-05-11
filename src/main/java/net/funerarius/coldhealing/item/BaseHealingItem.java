package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.client.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseHealingItem extends Item {

    public BaseHealingItem(Properties properties) {
        super(properties);
    }

// Mandatory

    public abstract int getUseTicks(ItemStack stack);
    public abstract SoundEvent getOpenSound();
    public abstract SoundEvent getUseSound(ItemStack stack);
    public abstract SoundEvent getFinishSound(ItemStack stack);
    public abstract int getItemBarColor(ItemStack stack);
    public abstract String getTooltipKey();

    public abstract void applyCureServer(LivingEntity entity, ItemStack stack);

// Additional stuff

    public void applyCureClient(LivingEntity entity, ItemStack stack) {}
    public boolean canUse(Level level, Player player, ItemStack stack) { return true; }
    public boolean playBreakSound() { return true; }
    public boolean isContinuousUse() { return false; }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.CROSSBOW; }

    @Override
    public int getUseDuration(ItemStack stack) { return 72000; }

// Healing code is now here

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this) || !canUse(level, player, stack)) {
            return InteractionResultHolder.fail(stack);
        }

        if (getOpenSound() != null) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    getOpenSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("HealTimer", 0);
        tag.putInt("MaxHealTimer", getUseTicks(stack));

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        CompoundTag tag = stack.getOrCreateTag();
        int currentTimer = tag.getInt("HealTimer");
        currentTimer++;

        if (livingEntity instanceof Player player && !canUse(level, player, stack)) {
            tag.putInt("HealTimer", 0);
            return;
        }

        if (currentTimer == 5 && !level.isClientSide && getUseSound(stack) != null) {
            playTrackingSound(level, livingEntity, getUseSound(stack));
        }

        int maxTimer = tag.contains("MaxHealTimer") ? tag.getInt("MaxHealTimer") : getUseTicks(stack);

        if (currentTimer >= maxTimer) {

            if (!level.isClientSide) {
                SoundEvent finishSound = getFinishSound(stack);

                applyCureServer(livingEntity, stack);

                if (finishSound != null) {
                    level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                            finishSound, SoundSource.PLAYERS, 1.0F, 1.0F);
                }

                stack.hurtAndBreak(1, livingEntity, (entity) -> {
                    if (playBreakSound()) entity.broadcastBreakEvent(livingEntity.getUsedItemHand());
                });
            } else {
                applyCureClient(livingEntity, stack);
            }

            tag.putInt("HealTimer", 0);
            tag.putInt("MaxHealTimer", getUseTicks(stack));

        } else {
            tag.putInt("HealTimer", currentTimer);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        stack.getOrCreateTag().putInt("HealTimer", 0);

        if (!level.isClientSide && getUseSound(stack) != null) {
            for (Player player : level.players()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundStopSoundPacket(getUseSound(stack).getLocation(), SoundSource.PLAYERS));
                }
            }
        }
    }

    @Override
    public int getBarColor(ItemStack stack) { return getItemBarColor(stack); }

    @Override
    public boolean isBarVisible(ItemStack stack) { return true; }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable(getTooltipKey()));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    protected void playTrackingSound(Level level, LivingEntity livingEntity, SoundEvent soundEvent) {
        float randomPitch = 0.8F + level.getRandom().nextFloat() * 0.4F;
        if (livingEntity instanceof Player player) {
            level.playSound(player, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundEvent, SoundSource.PLAYERS, 1.0F, randomPitch);
        }
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            var soundHolder = net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS.getHolder(soundEvent).orElse(null);
            if (soundHolder != null) {
                serverPlayer.connection.send(new ClientboundSoundEntityPacket(soundHolder, SoundSource.PLAYERS, serverPlayer, 1.0F, randomPitch, level.getRandom().nextLong()));
            }
        }
    }
}