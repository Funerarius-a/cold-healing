package net.funerarius.coldhealing;

import net.funerarius.coldhealing.client.ClientStimPouchTooltip;
import net.funerarius.coldhealing.item.StimPouchTooltip;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.funerarius.coldhealing.client.HealingProgressOverlay;

@Mod.EventBusSubscriber(modid = ColdHealing.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void registerTooltips(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(StimPouchTooltip.class, ClientStimPouchTooltip::new);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("healing_progress", HealingProgressOverlay.HUD);
    }
}