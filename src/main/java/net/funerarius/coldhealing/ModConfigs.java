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

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AFAK_STAGE1_REMOVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AFAK_STAGE2_REMOVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IFAK_REMOVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IBUPROFEN_REMOVE;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AFAK_STAGE1_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AFAK_STAGE2_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IFAK_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IBUPROFEN_ADD;

    static {
        BUILDER.push("AFAK Settings");
        AFAK_STAGE1_REMOVE = BUILDER.comment("Effects that are removed by the first stage of the AFAK")
                .defineList("afak_stage1_remove", List.of("minecraft:wither", "minecraft:poison"), obj -> true);

        AFAK_STAGE1_ADD = BUILDER.comment("Effects added by the first stage of the AFAK. Format: \"mod:effect, amplifier, duration_ticks\"")
                .defineList("afak_stage1_add", List.of(), obj -> true);

        AFAK_STAGE2_REMOVE = BUILDER.comment("Effects that are removed by the second stage of the AFAK")
                .defineList("afak_stage2_remove", List.of(), obj -> true);

        AFAK_STAGE2_ADD = BUILDER.comment("Effects added by the second stage of the AFAK. Format: \"mod:effect, amplifier, duration_ticks\"")
                .defineList("afak_stage2_add", List.of("minecraft:regeneration, 1, 100"), obj -> true);
        BUILDER.pop();

        BUILDER.push("IFAK Settings");
        IFAK_REMOVE = BUILDER.comment("Effects removed by the IFAK")
                .defineList("ifak_remove", List.of("minecraft:wither"), obj -> true);

        IFAK_ADD = BUILDER.comment("Effects added by the IFAK. Format: \"mod:effect, amplifier, duration_ticks\"")
                .defineList("ifak_add", List.of(), obj -> true);
        BUILDER.pop();

        BUILDER.push("Ibuprofen Settings");
        IBUPROFEN_REMOVE = BUILDER.comment("Effects removed by the Ibuprofen")
                .defineList("ibuprofen_remove", List.of("minecraft:poison", "minecraft:wither"), obj -> true);

        IBUPROFEN_ADD = BUILDER.comment("Effects added by the Ibuprofen. Format: \"mod:effect, amplifier, duration_ticks\"")
                .defineList("ibuprofen_add", List.of("minecraft:weakness, 0, 200", "minecraft:mining_fatigue, 0, 100"), obj -> true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static void removeConfiguredEffects(LivingEntity entity, List<? extends String> configList) {
        for (String effectId : configList) {
            ResourceLocation loc = new ResourceLocation(effectId);
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(loc);

            if (effect != null && entity.hasEffect(effect)) {
                entity.removeEffect(effect);
            }
        }
    }

    public static void addConfiguredEffects(LivingEntity entity, List<? extends String> configList) {
        for (String entry : configList) {
            try {

                String[] parts = entry.split(",");

                if (parts.length >= 3) {

                    String effectId = parts[0].trim();
                    int amplifier = Integer.parseInt(parts[1].trim());
                    int duration = Integer.parseInt(parts[2].trim());

                    ResourceLocation loc = new ResourceLocation(effectId);
                    MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(loc);

                    if (effect != null) {

                        entity.addEffect(new MobEffectInstance(effect, duration, amplifier));
                    }
                }
            } catch (Exception e) {

                System.out.println("ColdHealing - Error in the effect formatting: " + entry);
            }
        }
    }
}