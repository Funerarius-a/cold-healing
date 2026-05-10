package net.funerarius.coldhealing.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;

public class LsoCompat {

    public static final boolean IS_LSO_LOADED = ModList.get().isLoaded("legendarysurvivaloverhaul");

    public static boolean tryRestoreBrokenLimb(Player player) {
        if (!IS_LSO_LOADED) return false;
        return LsoInternal.doSurgery(player);
    }

    public static boolean healMostDamagedLimb(Player player, float healPercentage, int healingTime) {
        if (!IS_LSO_LOADED) return false;
        return LsoInternal.healDamage(player, healPercentage, healingTime);
    }

    public static boolean healAllLimbs(Player player, float healPercentage, int healingTime) {
        if (!IS_LSO_LOADED) return false;
        return LsoInternal.healAll(player, healPercentage, healingTime);
    }

    public static boolean healSpecificLimbs(Player player, float healPercentage, int healingTime, String... targetParts) {
        if (!IS_LSO_LOADED) return false;
        return LsoInternal.healSpecific(player, healPercentage, healingTime, targetParts);
    }

    public static boolean tryRestoreSpecificBrokenLimbs(Player player, String... targetParts) {
        if (!IS_LSO_LOADED) return false;
        return LsoInternal.restoreSpecificBroken(player, targetParts);
    }

    public static void openLsoHealingScreen(Player player, ItemStack stack) {
        if (!IS_LSO_LOADED) return;
        LsoInternal.openScreen(player, stack);
    }

    private static class LsoInternal {

        static boolean doSurgery(Player player) {
            for (BodyPartEnum part : BodyPartEnum.values()) {
                float maxHealth = BodyDamageUtil.getMaxHealth(player, part);
                float ratio = BodyDamageUtil.getHealthRatio(player, part);

                if ((maxHealth * ratio) <= 0.01F) {
                    BodyDamageUtil.healBodyPart(player, part, 1.0F);
                    return true;
                }
            }
            return false;
        }

        static boolean healDamage(Player player, float healPercentage, int healingTime) {
            BodyPartEnum mostDamagedPart = null;
            float lowestRatio = 1.0F;

            for (BodyPartEnum part : BodyPartEnum.values()) {
                float ratio = BodyDamageUtil.getHealthRatio(player, part);
                if (ratio > 0.01F && ratio < 1.0F && ratio < lowestRatio) {
                    lowestRatio = ratio;
                    mostDamagedPart = part;
                }
            }

            if (mostDamagedPart != null) {
                float maxHealth = BodyDamageUtil.getMaxHealth(player, mostDamagedPart);
                float healAmount = maxHealth * healPercentage;

                // A MÁGICA ESCOLHE AQUI:
                if (healingTime > 0) {
                    BodyDamageUtil.applyHealingTimeBodyPart(player, mostDamagedPart, healAmount, healingTime);
                } else {
                    BodyDamageUtil.healBodyPart(player, mostDamagedPart, healAmount);
                }
                return true;
            }
            return false;
        }

        static boolean healAll(Player player, float healPercentage, int healingTime) {
            boolean healedAtLeastOne = false;

            for (BodyPartEnum part : BodyPartEnum.values()) {
                float ratio = BodyDamageUtil.getHealthRatio(player, part);

                if (ratio > 0.01F && ratio < 1.0F) {
                    float maxHealth = BodyDamageUtil.getMaxHealth(player, part);
                    float healAmount = maxHealth * healPercentage;

                    if (healingTime > 0) {
                        BodyDamageUtil.applyHealingTimeBodyPart(player, part, healAmount, healingTime);
                    } else {
                        BodyDamageUtil.healBodyPart(player, part, healAmount);
                    }
                    healedAtLeastOne = true;
                }
            }
            return healedAtLeastOne;
        }

        static boolean healSpecific(Player player, float healPercentage, int healingTime, String... targetParts) {
            boolean healedAtLeastOne = false;

            for (String targetName : targetParts) {
                try {
                    BodyPartEnum part = BodyPartEnum.get(targetName);
                    float ratio = BodyDamageUtil.getHealthRatio(player, part);

                    if (ratio > 0.01F && ratio < 1.0F) {
                        float maxHealth = BodyDamageUtil.getMaxHealth(player, part);
                        float healAmount = maxHealth * healPercentage;

                        if (healingTime > 0) {
                            BodyDamageUtil.applyHealingTimeBodyPart(player, part, healAmount, healingTime);
                        } else {
                            BodyDamageUtil.healBodyPart(player, part, healAmount);
                        }
                        healedAtLeastOne = true;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("ColdHealing LSO Compat - Invalid body part: " + targetName);
                }
            }
            return healedAtLeastOne;
        }

        static boolean restoreSpecificBroken(Player player, String... targetParts) {
            boolean restoredAtLeastOne = false;
            for (String targetName : targetParts) {
                try {
                    BodyPartEnum part = BodyPartEnum.get(targetName);
                    float maxHealth = BodyDamageUtil.getMaxHealth(player, part);
                    float ratio = BodyDamageUtil.getHealthRatio(player, part);

                    if ((maxHealth * ratio) <= 0.01F) {
                        BodyDamageUtil.healBodyPart(player, part, 1.0F);
                        restoredAtLeastOne = true;
                        return true;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("ColdHealing LSO Compat - Invalid body part: " + targetName);
                }
            }
            return restoredAtLeastOne;
        }

        static void openScreen(Player player, ItemStack stack) {
            BodyDamageUtil.applyConsumableHealing(player, stack, true);
        }
    }
}