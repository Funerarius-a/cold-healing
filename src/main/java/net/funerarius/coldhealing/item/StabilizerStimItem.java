package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.ModConfigs;
import net.funerarius.coldhealing.client.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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

public class StabilizerStimItem extends Item implements IStim {

    private final float healthActivationThreshold;

    public StabilizerStimItem(Properties properties, float healthActivationThreshold) {
        super(properties);
        this.healthActivationThreshold = healthActivationThreshold;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.INJECTOR_START.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("HealTimer", 0);

        tag.putInt("MaxHealTimer", 30);

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) { return 72000; }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.BOW; }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        if (level.isClientSide) return;

        CompoundTag tag = stack.getOrCreateTag();
        int currentTimer = tag.getInt("HealTimer");
        currentTimer++;

        if (currentTimer >= 30) {

            applyStimEffects(livingEntity, stack);

            stack.hurtAndBreak(1, livingEntity, (entity) -> {});
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
    public boolean shouldAutoInject(LivingEntity entity, ItemStack stimStack) {
        if (this.healthActivationThreshold > 0 && entity.getHealth() <= this.healthActivationThreshold) {
            return true;
        }
        if (ModConfigs.hasRemovableEffect(entity, ModConfigs.P04_REMOVE.get())) {
            return true;
        }

        return false;
    }

    @Override
    public boolean applyStimEffects(LivingEntity entity, ItemStack stimStack) {
        entity.heal(6.0F);
        ModConfigs.removeConfiguredEffects(entity, ModConfigs.P04_REMOVE.get());
        ModConfigs.addConfiguredEffects(entity, ModConfigs.P04_ADD.get());

        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                ModSounds.INJECTOR_USE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        return true;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xb70c31;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.coldhealing.p_04_stabilizer.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
