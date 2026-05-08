package net.funerarius.coldhealing;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ColdHealing.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MelatoninEvents {

    @SubscribeEvent
    public static void onSleepLocationCheck(SleepingLocationCheckEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getPersistentData().getBoolean("MelatoninSleep")) {
                event.setResult(Event.Result.ALLOW);
            }
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
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            Player player = event.player;
            CompoundTag data = player.getPersistentData();

            if (player.isSleeping() && data.getBoolean("MelatoninSleep")) {

                int sleepTimer = data.getInt("MelatoninTimer");
                sleepTimer++;

                if (sleepTimer >= 100) {
                    ServerLevel level = (ServerLevel) player.level();

                    level.setDayTime(level.getDayTime() + 7000);

                    if (level.isRaining() || level.isThundering()) {
                        level.setWeatherParameters(6000, 0, false, false);
                    }

                    player.stopSleeping();

                    data.remove("MelatoninSleep");
                    data.remove("MelatoninTimer");
                } else {
                    data.putInt("MelatoninTimer", sleepTimer);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent event) {
        Player player = event.getEntity();
        CompoundTag data = player.getPersistentData();

        if (data.getBoolean("MelatoninSleep")) {
            data.remove("MelatoninSleep");
            data.remove("MelatoninTimer");
        }
    }
}