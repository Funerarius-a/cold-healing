package net.funerarius.coldhealing;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ModConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Listas de Remover
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AFAK_STAGE1_REMOVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AFAK_STAGE2_REMOVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IFAK_REMOVE;

    // NOVAS Listas de Adicionar
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AFAK_STAGE1_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AFAK_STAGE2_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IFAK_ADD;

    static {
        BUILDER.push("AFAK Settings");
        AFAK_STAGE1_REMOVE = BUILDER.comment("Efeitos removidos pelo AFAK no estagio 1 (Bandagem)")
                .defineList("afak_stage1_remove", List.of("minecraft:wither", "minecraft:poison"), obj -> true);

        // Exemplo: Não adiciona nada por padrão no estágio 1
        AFAK_STAGE1_ADD = BUILDER.comment("Efeitos adicionados pelo AFAK no estagio 1. Formato: \"mod:efeito, amplificador, duracao_em_ticks\"")
                .defineList("afak_stage1_add", List.of(), obj -> true);

        AFAK_STAGE2_REMOVE = BUILDER.comment("Efeitos removidos pelo AFAK no estagio 2 (Injecao)")
                .defineList("afak_stage2_remove", List.of(), obj -> true);

        // Exemplo: Adiciona Regeneração Nível 2 (Amplificador 1) por 100 ticks (5 segundos)
        AFAK_STAGE2_ADD = BUILDER.comment("Efeitos adicionados pelo AFAK no estagio 2. Formato: \"mod:efeito, amplificador, duracao_em_ticks\"")
                .defineList("afak_stage2_add", List.of("minecraft:regeneration, 1, 100"), obj -> true);
        BUILDER.pop();

        BUILDER.push("IFAK Settings");
        IFAK_REMOVE = BUILDER.comment("Efeitos removidos pelo IFAK")
                .defineList("ifak_remove", List.of("minecraft:wither"), obj -> true);

        IFAK_ADD = BUILDER.comment("Efeitos adicionados pelo IFAK. Formato: \"mod:efeito, amplificador, duracao_em_ticks\"")
                .defineList("ifak_add", List.of(), obj -> true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    // Função que remove os efeitos (Já tínhamos feito)
    public static void removeConfiguredEffects(LivingEntity entity, List<? extends String> configList) {
        for (String effectId : configList) {
            ResourceLocation loc = new ResourceLocation(effectId);
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(loc);

            if (effect != null && entity.hasEffect(effect)) {
                entity.removeEffect(effect);
            }
        }
    }

    // NOVA FUNÇÃO: Que lê a String, divide nas vírgulas e aplica o efeito
    public static void addConfiguredEffects(LivingEntity entity, List<? extends String> configList) {
        for (String entry : configList) {
            try {
                // Divide o texto onde tem vírgula.
                // Exemplo: "minecraft:regeneration, 1, 100" vira["minecraft:regeneration", " 1", " 100"]
                String[] parts = entry.split(",");

                if (parts.length >= 3) {
                    // .trim() remove os espaços em branco que a pessoa possa ter deixado
                    String effectId = parts[0].trim();
                    int amplifier = Integer.parseInt(parts[1].trim());
                    int duration = Integer.parseInt(parts[2].trim());

                    ResourceLocation loc = new ResourceLocation(effectId);
                    MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(loc);

                    if (effect != null) {
                        // Aplica o efeito no jogador! O Minecraft pede na ordem: (Efeito, Duração, Amplificador)
                        entity.addEffect(new MobEffectInstance(effect, duration, amplifier));
                    }
                }
            } catch (Exception e) {
                // Se o jogador digitar letras no lugar de números na config,
                // esse bloco try/catch captura o erro e simplesmente ignora, evitado o crash.
                System.out.println("ColdHealing - Erro de formatação no efeito: " + entry);
            }
        }
    }
}