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
import net.minecraft.network.chat.Component;

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

            if (!player.level().isClientSide) {
                long startTime = data.getLong("MelatoninStartTime");
                long currentTime = player.level().getDayTime();
                long ticksSlept = currentTime - startTime;

                if (ticksSlept > 100) {

                    int hours = (int) (ticksSlept / 1000);
                    int remainder = (int) (ticksSlept % 1000);
                    int minutes = (int) ((remainder / 1000.0) * 60);

                    player.sendSystemMessage(Component.literal("§uYou slept for " + hours + " hours and " + minutes + " minutes."));
                }
            }

            data.remove("MelatoninSleep");
            data.remove("MelatoninTargetTime");
            data.remove("MelatoninStartTime");
        }
    }
}