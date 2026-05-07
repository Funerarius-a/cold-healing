package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.ModConfigs;
import net.funerarius.coldhealing.client.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CmsItem extends Item {

    public CmsItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.OPEN_GENERIC.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

        stack.getOrCreateTag().putInt("HealTimer", 0);
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

        if (currentTimer >= 160) {
            livingEntity.heal(1.0F);

            ModConfigs.removeConfiguredEffects(livingEntity, ModConfigs.CMS_REMOVE.get());
            ModConfigs.addConfiguredEffects(livingEntity, ModConfigs.CMS_ADD.get());

            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                    SoundEvents.ARMOR_EQUIP_NETHERITE, SoundSource.PLAYERS, 1.0F, 1.0F);

            stack.hurtAndBreak(1, livingEntity, (entity) -> entity.broadcastBreakEvent(livingEntity.getUsedItemHand()));
            tag.putInt("HealTimer", 0);
        } else {
            tag.putInt("HealTimer", currentTimer);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        stack.getOrCreateTag().putInt("HealTimer", 0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x24c1ed;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.coldhealing.cms.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
