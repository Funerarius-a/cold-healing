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

    public static final RegistryObject<Item> SPLINT = ITEMS.register("splint",
            () -> new SplintItem(new Item.Properties().defaultDurability(1)));

    public static final RegistryObject<Item> CMS = ITEMS.register("cms",
            () -> new CmsItem(new Item.Properties().defaultDurability(3)));

    public static final RegistryObject<Item> P_04_STABILIZER = ITEMS.register("p_04_stabilizer",
            () -> new StabilizerStimItem(new Item.Properties().defaultDurability(1), 6.0F));

    public static final RegistryObject<Item> ADRENO_NEURAL_S3 = ITEMS.register("adreno_neural_s3",
            () -> new AdrenoStimItem(new Item.Properties().defaultDurability(1), 0.0F));

    public static final RegistryObject<Item> HEMATOX_G = ITEMS.register("hematox_g",
            () -> new HematoxStimItem(new Item.Properties().defaultDurability(1), 10.0F));

    public static final RegistryObject<Item> S9_N_BLOCK = ITEMS.register("s9_n_block",
            () -> new NblockStimItem(new Item.Properties().defaultDurability(1), 0.0F));

    public static final RegistryObject<Item> STIM_POUCH = ITEMS.register("stim_pouch",
            () -> new StimPouchItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CAR = ITEMS.register("car",
            () -> new CarItem(new Item.Properties().defaultDurability(12)));

    public static final RegistryObject<Item> MELATONIN = ITEMS.register("melatonin",
            () -> new MelatoninItem(new Item.Properties().defaultDurability(5)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}