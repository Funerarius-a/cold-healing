package net.funerarius.coldhealing.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IStim {
    boolean shouldAutoInject(LivingEntity entity, ItemStack stimStack);

    boolean applyStimEffects(LivingEntity entity, ItemStack stimStack);
}