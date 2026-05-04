package net.funerarius.coldhealing.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.funerarius.coldhealing.ModConfigs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class AfakItem extends Item {

    public AfakItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Som de abrir o zíper no começo
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1.0F, 1.0F);

        stack.getOrCreateTag().putInt("HealTimer", 0);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    // 1. ANIMAÇÃO: Alterado de BOW para CROSSBOW
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        if (level.isClientSide) return;

        CompoundTag tag = stack.getOrCreateTag();
        int currentTimer = tag.getInt("HealTimer");
        currentTimer++;

        int remainingUses = stack.getMaxDamage() - stack.getDamageValue();

        if (remainingUses > 1) {
            // CURA CURTA (Bandagem)
            if (currentTimer >= 40) {
                livingEntity.heal(1.0F);

                ModConfigs.removeConfiguredEffects(livingEntity, ModConfigs.AFAK_STAGE1_REMOVE.get());

                // 2. SOM: Som de equipar armadura de Ferro
                level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 1.0F, 1.0F);

                stack.hurtAndBreak(1, livingEntity, (entity) -> entity.broadcastBreakEvent(livingEntity.getUsedItemHand()));
                tag.putInt("HealTimer", 0);
            } else {
                tag.putInt("HealTimer", currentTimer);
            }
        } else if (remainingUses == 1) {
            // CURA LONGA (Injeção final)
            if (currentTimer >= 80) {
                livingEntity.heal(6.0F);

                ModConfigs.removeConfiguredEffects(livingEntity, ModConfigs.AFAK_STAGE2_REMOVE.get());

                // 2. SOM: Som de equipar armadura de Netherite (Geralmente mais pesado/metálico)
                level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        SoundEvents.ARMOR_EQUIP_NETHERITE, SoundSource.PLAYERS, 1.0F, 1.0F);

                stack.hurtAndBreak(1, livingEntity, (entity) -> entity.broadcastBreakEvent(livingEntity.getUsedItemHand()));
                tag.putInt("HealTimer", 0);
            } else {
                tag.putInt("HealTimer", currentTimer);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        stack.getOrCreateTag().putInt("HealTimer", 0);
    }

    // 4. COR DA BARRA: Sobrescrevemos a cor padrão que muda de verde pra vermelho
    @Override
    public int getBarColor(ItemStack stack) {
        // Retorna um azul estilo "médico" (formato Hexadecimal RGB).
        // Se quiser mudar a cor depois, basta pegar o código HEX de qualquer cor na internet (ex: no Google "color picker"),
        // e trocar o "#" por "0x". Exemplo: #0088FF vira 0x0088FF.
        return 0x0088FF;
    }
}