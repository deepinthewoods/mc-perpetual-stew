package ninja.trek.perpetualstew.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;
import ninja.trek.perpetualstew.PerpetualStew;
import ninja.trek.perpetualstew.block.entity.StewCauldronBlockEntity;

public class ModBlockEntities {
    public static final BlockEntityType<StewCauldronBlockEntity> STEW_CAULDRON_BLOCK_ENTITY =
            Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(PerpetualStew.MOD_ID, "stew_cauldron"),
                    FabricBlockEntityTypeBuilder.create(
                            StewCauldronBlockEntity::new,
                            ModBlocks.STEW_CAULDRON
                    ).build()
            );

    public static void register() {
        PerpetualStew.LOGGER.info("Registering block entities for " + PerpetualStew.MOD_ID);
    }
}
