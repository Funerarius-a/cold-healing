package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.ModConfigs;
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
import net.funerarius.coldhealing.compat.LsoCompat;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CarItem extends Item {

    public CarItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.OPEN_GENERIC.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("HealTimer", 0);

        boolean hasDebuff = ModConfigs.hasRemovableEffect(player, ModConfigs.CAR_BANDAGE_REMOVE.get());
        tag.putBoolean("IsBandageMode", hasDebuff);

        tag.putInt("MaxHealTimer", hasDebuff ? 40 : 60);

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) { return 72000; }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.CROSSBOW; }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        if (level.isClientSide) return;

        CompoundTag tag = stack.getOrCreateTag();
        int currentTimer = tag.getInt("HealTimer");
        currentTimer++;

        boolean isBandageMode = tag.getBoolean("IsBandageMode");

        if (currentTimer == 5) {
            if (isBandageMode) {
                playTrackingSound(level, livingEntity, ModSounds.BANDAGE_USE.get());
            } else {
                playTrackingSound(level, livingEntity, ModSounds.INJECTOR_KIT_USE.get());
            }
        }

        if (isBandageMode) {
            if (currentTimer >= 40) {
                livingEntity.heal(1.0F);
                ModConfigs.removeConfiguredEffects(livingEntity, ModConfigs.CAR_BANDAGE_REMOVE.get());
                ModConfigs.addConfiguredEffects(livingEntity, ModConfigs.CAR_BANDAGE_ADD.get());

                level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        ModSounds.BANDAGE_FINISH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                stack.hurtAndBreak(1, livingEntity, (entity) -> entity.broadcastBreakEvent(livingEntity.getUsedItemHand()));
                tag.putInt("HealTimer", 0);

                boolean nextDebuff = ModConfigs.hasRemovableEffect(livingEntity, ModConfigs.CAR_BANDAGE_REMOVE.get());
                tag.putBoolean("IsBandageMode", nextDebuff);

                tag.putInt("MaxHealTimer", nextDebuff ? 40 : 60);
            } else {
                tag.putInt("HealTimer", currentTimer);
            }

        } else {
            if (currentTimer >= 60) {
                livingEntity.heal(4.0F);

                ModConfigs.addConfiguredEffects(livingEntity, ModConfigs.CAR_HEAL_ADD.get());

                if (livingEntity instanceof Player player) {
                    LsoCompat.healMostDamagedLimb(player, 0.45F, 200);
                }

                level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        ModSounds.INJECTOR_PUTAWAY.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                stack.hurtAndBreak(1, livingEntity, (entity) -> entity.broadcastBreakEvent(livingEntity.getUsedItemHand()));
                tag.putInt("HealTimer", 0);

                boolean nextDebuff = ModConfigs.hasRemovableEffect(livingEntity, ModConfigs.CAR_BANDAGE_REMOVE.get());
                tag.putBoolean("IsBandageMode", nextDebuff);

                tag.putInt("MaxHealTimer", nextDebuff ? 40 : 60);
            } else {
                tag.putInt("HealTimer", currentTimer);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        stack.getOrCreateTag().putInt("HealTimer", 0);

        if (!level.isClientSide) {
            for (Player player : level.players()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundStopSoundPacket(ModSounds.BANDAGE_USE.getId(), SoundSource.PLAYERS));
                    serverPlayer.connection.send(new ClientboundStopSoundPacket(ModSounds.INJECTOR_KIT_USE.getId(), SoundSource.PLAYERS));
                }
            }
        }
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x2de03c;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) { return true; }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.coldhealing.car.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private void playTrackingSound(Level level, LivingEntity livingEntity, SoundEvent soundEvent) {
        float randomPitch = 0.8F + level.getRandom().nextFloat() * 0.4F;

        if (livingEntity instanceof Player player) {
            level.playSound(player, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                    soundEvent, SoundSource.PLAYERS, 1.0F, randomPitch);
        }

        if (livingEntity instanceof ServerPlayer serverPlayer) {
            var soundHolder = net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS.getHolder(soundEvent).orElse(null);
            if (soundHolder != null) {
                serverPlayer.connection.send(new ClientboundSoundEntityPacket(
                        soundHolder, SoundSource.PLAYERS, serverPlayer, 1.0F, randomPitch, level.getRandom().nextLong()
                ));
            }
        }
    }
}