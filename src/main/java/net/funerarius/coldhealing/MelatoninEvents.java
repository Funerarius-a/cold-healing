package net.funerarius.coldhealing;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ColdHealing.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MelatoninEvents {

    @SubscribeEvent
    public static void onSleepLocationCheck(SleepingLocationCheckEvent event) {
        if (event.getEntity() instanceof Player player && player.getPersistentData().getBoolean("MelatoninSleep")) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onSleepTimeCheck(SleepingTimeCheckEvent event) {
        Player player = event.getEntity();
        if (player.getPersistentData().getBoolean("MelatoninSleep")) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        Level level = (Level) event.getLevel();
        long currentWorldTime = level.getDayTime();
        long targetTime = -1;

        for (Player player : level.players()) {
            if (player.isSleeping() && player.getPersistentData().getBoolean("MelatoninSleep")) {
                long playerTarget = player.getPersistentData().getLong("MelatoninTargetTime");
                // Se houver múltiplos jogadores, pegamos o que quer acordar mais cedo
                if (targetTime == -1 || playerTarget < targetTime) {
                    targetTime = playerTarget;
                }
            }
        }

        if (targetTime != -1) {
            long timeJump = targetTime - currentWorldTime;

            if (timeJump > 0) {
                event.setTimeAddition(currentWorldTime + timeJump);
            }
        }
    }

    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent event) {
        Player player = event.getEntity();
        CompoundTag data = player.getPersistentData();
        if (data.getBoolean("MelatoninSleep")) {
            data.remove("MelatoninSleep");
            data.remove("MelatoninTargetTime");
        }
    }
}