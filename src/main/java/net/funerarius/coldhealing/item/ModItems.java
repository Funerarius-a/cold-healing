package net.funerarius.coldhealing.item;

import net.funerarius.coldhealing.ColdHealing;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ColdHealing.MOD_ID);

    public static final RegistryObject<Item> AFAK = ITEMS.register("afak",
            () -> new AfakItem(new Item.Properties().defaultDurability(10)));

    public static final RegistryObject<Item> IFAK = ITEMS.register("ifak",
            () -> new IfakItem(new Item.Properties().defaultDurability(7)));

    public static final RegistryObject<Item> IBUPROFEN = ITEMS.register("ibuprofen",
            () -> new IbuprofenItem(new Item.Properties().defaultDurability(5)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}