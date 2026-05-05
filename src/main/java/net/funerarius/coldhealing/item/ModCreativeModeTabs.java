package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.ColdHealing;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ColdHealing.MOD_ID);

    public static final RegistryObject<CreativeModeTab> COLDHEALING_TAB = CREATIVE_MODE_TABS.register("coldhealing_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.AFAK.get()))
                    .title(Component.translatable("creativetab.coldhealing_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.AFAK.get());
                        output.accept(ModItems.IFAK.get());
                        output.accept(ModItems.IBUPROFEN.get());
                        output.accept(ModItems.SPLINT.get());
                        output.accept(ModItems.CMS.get());
                        output.accept(ModItems.HEALTHSTIM.get());
                        output.accept(ModItems.ADRENALINESTIM.get());
                        output.accept(ModItems.STIM_POUCH.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
