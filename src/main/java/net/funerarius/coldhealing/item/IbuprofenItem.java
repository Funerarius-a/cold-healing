package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.ModConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.funerarius.coldhealing.client.ModSounds;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IbuprofenItem extends Item {

    public IbuprofenItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (ModConfigs.hasIncompatibleEffect(player, ModConfigs.IBUPROFEN_INCOMPATIBLE.get())) {

            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("message.coldhealing.incompatible_effect"), true);
            }

            return InteractionResultHolder.fail(stack);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.PILLS_BOTTLE_OPEN.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

        stack.getOrCreateTag().putInt("HealTimer", 0);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) { return 72000; }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.DRINK; }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        if (level.isClientSide) return;

        if (ModConfigs.hasIncompatibleEffect(livingEntity, ModConfigs.IBUPROFEN_INCOMPATIBLE.get())) {

            if (livingEntity instanceof Player player) {
                player.displayClientMessage(
                        Component.translatable("message.coldhealing.incompatible_effect"), true);
            }

            stack.getOrCreateTag().putInt("HealTimer", 0);
            return;
        }

        CompoundTag tag = stack.getOrCreateTag();
        int currentTimer = tag.getInt("HealTimer");
        currentTimer++;

        if (currentTimer == 5) {
            playTrackingSound(level, livingEntity, ModSounds.PILLS_BOTTLE_USE.get());
        }

        if (currentTimer >= 40) {
            livingEntity.heal(0.0F);

            ModConfigs.removeConfiguredEffects(livingEntity, ModConfigs.IBUPROFEN_REMOVE.get());
            ModConfigs.addConfiguredEffects(livingEntity, ModConfigs.IBUPROFEN_ADD.get());

            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                    ModSounds.PILLS_BOTTLE_CLOSE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            stack.hurtAndBreak(1, livingEntity, (entity) -> {});
            tag.putInt("HealTimer", 0);
        } else {
            tag.putInt("HealTimer", currentTimer);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        stack.getOrCreateTag().putInt("HealTimer", 0);

        if (!level.isClientSide) {
            for (Player player : level.players()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundStopSoundPacket(ModSounds.PILLS_BOTTLE_USE.getId(), SoundSource.PLAYERS));
                }
            }
        }
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xf2591d;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.coldhealing.ibuprofen.tooltip"));
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
                        soundHolder,
                        SoundSource.PLAYERS,
                        serverPlayer,
                        1.0F,
                        randomPitch,
                        level.getRandom().nextLong()
                ));
            }
        }
    }
}
