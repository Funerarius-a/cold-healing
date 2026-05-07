package net.funerarius.coldhealing.client;

import net.funerarius.coldhealing.ColdHealing;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ColdHealing.MOD_ID);

    public static final RegistryObject<SoundEvent> OPEN_GENERIC = registerSoundEvent("item.generic_open");
    public static final RegistryObject<SoundEvent> BANDAGE_USE = registerSoundEvent("item.bandage_use");
    public static final RegistryObject<SoundEvent> BANDAGE_FINISH = registerSoundEvent("item.bandage_finish");
    public static final RegistryObject<SoundEvent> INJECTOR_KIT_USE = registerSoundEvent("item.injector_kit_use");
    public static final RegistryObject<SoundEvent> INJECTOR_PUTAWAY = registerSoundEvent("item.injector_putaway");
    public static final RegistryObject<SoundEvent> SPLINT_START = registerSoundEvent("item.splint_start");
    public static final RegistryObject<SoundEvent> SPLINT_USE = registerSoundEvent("item.splint_use");
    public static final RegistryObject<SoundEvent> SPLINT_END = registerSoundEvent("item.splint_end");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ColdHealing.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}