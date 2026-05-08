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
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SPLINT_REMOVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CMS_REMOVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> P04_REMOVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ADRENO_REMOVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> HEMATOX_REMOVE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> NBLOCK_REMOVE;


    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AFAK_STAGE1_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> AFAK_STAGE2_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IFAK_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IBUPROFEN_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SPLINT_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CMS_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> P04_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ADRENO_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> HEMATOX_ADD;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> NBLOCK_ADD;


    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IBUPROFEN_INCOMPATIBLE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SPLINT_INCOMPATIBLE;


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
                .defineList("ibuprofen_remove", List.of("minecraft:poison", "minecraft:darkness"), obj -> true);

        IBUPROFEN_ADD = BUILDER.comment("Effects added by the Ibuprofen. Format: \"mod:effect, amplifier, duration_ticks\"")
                .defineList("ibuprofen_add", List.of("minecraft:weakness, 0, 800", "minecraft:mining_fatigue, 0, 200"), obj -> true);

        IBUPROFEN_INCOMPATIBLE = BUILDER.comment("List of effects that when present makes the ibuprofen unusable")
                .defineList("ibuprofen_incompatible", List.of("minecraft:weakness"), obj -> true);
        BUILDER.pop();

        BUILDER.push("Splint Settings");
        SPLINT_REMOVE = BUILDER.comment("Effects removed by the Splint")
                .defineList("splint_remove", List.of("minecraft:slowness"), obj -> true);

        SPLINT_ADD = BUILDER.comment("Effects added by the Splint. Format: \"mod:effect, amplifier, duration_ticks\"")
                .defineList("splint_add", List.of("minecraft:mining_fatigue, 0, 200"), obj -> true);

        SPLINT_INCOMPATIBLE = BUILDER.comment("List of effects that when present makes the splint unusable")
                .defineList("splint_incompatible", List.of("minecraft:wither", "minecraft:poison", "minecraft:weakness"), obj -> true);
        BUILDER.pop();

        BUILDER.push("CMS Settings");
        CMS_REMOVE = BUILDER.comment("Effects removed by the CMS")
                .defineList("cms_remove", List.of("minecraft:slowness"), obj -> true);

        CMS_ADD = BUILDER.comment("Effects added by the CMS. Format: \"mod:effect, amplifier, duration_ticks\"")
                .defineList("cms_add", List.of("minecraft:mining_fatigue, 0, 800", "minecraft:regeneration, 1, 60"), obj -> true);
        BUILDER.pop();

        BUILDER.push("P-04 Stabilizer Settings");
        P04_REMOVE = BUILDER.comment("Effects removed by the P-04")
                .defineList("p_04_remove", List.of("minecraft:wither"), obj -> true);

        P04_ADD = BUILDER.comment("Effects added by the S3. Format: \"mod:effect, amplifier, duration_ticks\"")
                .defineList("p_04_add", List.of("minecraft:mining_fatigue, 0, 800", "minecraft:regeneration, 1, 60"), obj -> true);
        BUILDER.pop();

        BUILDER.push("Adreno Neural S3 Settings");
        ADRENO_REMOVE = BUILDER.comment("Effects removed by the S3")
                .defineList("adreno_remove", List.of("minecraft:slowness"), obj -> true);

        ADRENO_ADD = BUILDER.comment("Effects added by the S3. Format: \"mod:effect, amplifier, duration_ticks\"")
                .defineList("adreno_add", List.of("minecraft:speed, 0, 800"), obj -> true);
        BUILDER.pop();

        BUILDER.push("Hematox-G Settings");
        HEMATOX_REMOVE = BUILDER.comment("Effects removed by the Hematox")
                .defineList("hematox_remove", List.of("minecraft:poison"), obj -> true);

        HEMATOX_ADD = BUILDER.comment("Effects added by the Hematox. Format: \"mod:effect, amplifier, duration_ticks\"")
                .defineList("hematox_add", List.of("minecraft:regeneration, 0, 1200"), obj -> true);
        BUILDER.pop();

        BUILDER.push("S9 N-BLOCK Settings");
        NBLOCK_REMOVE = BUILDER.comment("Effects removed by the S9")
                .defineList("s9_remove", List.of("minecraft:blindness", "minecraft:darkness"), obj -> true);

        NBLOCK_ADD = BUILDER.comment("Effects added by the S9. Format: \"mod:effect, amplifier, duration_ticks\"")
                .defineList("s9_add", List.of("minecraft:weakness, 0, 800"), obj -> true);
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

    public static boolean hasIncompatibleEffect(LivingEntity entity, List<? extends String> configList) {
        for (String effectId : configList) {
            try {
                ResourceLocation loc = new ResourceLocation(effectId.trim());
                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(loc);

                if (effect != null && entity.hasEffect(effect)) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println("ColdHealing - Erro no formato incompativel: " + effectId);
            }
        }
        return false;
    }

    public static boolean hasRemovableEffect(LivingEntity entity, List<? extends String> configList) {
        for (String effectId : configList) {
            ResourceLocation loc = new ResourceLocation(effectId.trim());
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(loc);
            if (effect != null && entity.hasEffect(effect)) {
                return true;
            }
        }
        return false;
    }

}