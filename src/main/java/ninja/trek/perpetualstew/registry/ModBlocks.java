package ninja.trek.perpetualstew.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import ninja.trek.perpetualstew.PerpetualStew;
import ninja.trek.perpetualstew.block.StewCauldronBlock;

public class ModBlocks {
    public static final StewCauldronBlock STEW_CAULDRON = register("stew_cauldron",
            new StewCauldronBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)));

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(
                BuiltInRegistries.BLOCK,
                Identifier.fromNamespaceAndPath(PerpetualStew.MOD_ID, name),
                block
        );
    }

    public static void register() {
        PerpetualStew.LOGGER.info("Registering blocks for " + PerpetualStew.MOD_ID);
    }
}
