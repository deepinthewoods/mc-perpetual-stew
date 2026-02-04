package ninja.trek.perpetualstew.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import ninja.trek.perpetualstew.PerpetualStew;
import ninja.trek.perpetualstew.component.StewData;
import ninja.trek.perpetualstew.item.PerpetualStewItem;

public class ModItems {
    public static final Item PERPETUAL_STEW = register("perpetual_stew",
            new PerpetualStewItem(new Item.Properties()
                    .stacksTo(1)
                    .component(ModDataComponents.STEW_DATA, StewData.DEFAULT)));

    public static final Item STEW_CAULDRON = register("stew_cauldron",
            new BlockItem(ModBlocks.STEW_CAULDRON, new Item.Properties()));

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(PerpetualStew.MOD_ID, name),
                item
        );
    }

    public static void register() {
        PerpetualStew.LOGGER.info("Registering items for " + PerpetualStew.MOD_ID);
    }
}
