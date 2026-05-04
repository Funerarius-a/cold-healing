package net.funerarius.coldhealing;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ModConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Listas que vão guardar os efeitos lidos do arquivo .toml
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AFAK_STAGE1_REMOVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AFAK_STAGE2_REMOVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IFAK_REMOVE;

    static {
        BUILDER.push("AFAK Settings");
        AFAK_STAGE1_REMOVE = BUILDER.comment("Efeitos que o AFAK remove nos usos normais (Bandagem)")
                .defineList("afak_stage1_remove", List.of("minecraft:wither", "minecraft:poison"), obj -> true);

        AFAK_STAGE2_REMOVE = BUILDER.comment("Efeitos que o AFAK remove no ultimo uso (Injecao)")
                .defineList("afak_stage2_remove", List.of(), obj -> true); // Vazio por padrão
        BUILDER.pop();

        BUILDER.push("IFAK Settings");
        IFAK_REMOVE = BUILDER.comment("Efeitos que o IFAK remove")
                .defineList("ifak_remove", List.of("minecraft:wither"), obj -> true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    // --- FUNÇÃO MÁGICA PARA REMOVER EFEITOS COM SEGURANÇA ---
    public static void removeConfiguredEffects(LivingEntity entity, List<? extends String> configList) {
        for (String effectId : configList) {
            ResourceLocation loc = new ResourceLocation(effectId);
            // Procura o efeito no jogo. Se o mod não existir, ele retorna nulo e não crasha!
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(loc);

            if (effect != null && entity.hasEffect(effect)) {
                entity.removeEffect(effect);
            }
        }
    }
}
