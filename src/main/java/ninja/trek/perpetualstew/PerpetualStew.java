package ninja.trek.perpetualstew;

import net.fabricmc.api.ModInitializer;
import ninja.trek.perpetualstew.registry.ModBlockEntities;
import ninja.trek.perpetualstew.registry.ModBlocks;
import ninja.trek.perpetualstew.registry.ModDataComponents;
import ninja.trek.perpetualstew.registry.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerpetualStew implements ModInitializer {
    public static final String MOD_ID = "perpetual-stew";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Perpetual Stew mod");

        // Register in order of dependencies
        ModDataComponents.register();
        LOGGER.info("Registered: {}", ModDataComponents.STEW_DATA);

        ModBlocks.register();
        LOGGER.info("Registered block: {}", ModBlocks.STEW_CAULDRON);

        ModBlockEntities.register();
        LOGGER.info("Registered block entity type: {}", ModBlockEntities.STEW_CAULDRON_BLOCK_ENTITY);

        ModItems.register();
        LOGGER.info("Registered items: {}, {}", ModItems.PERPETUAL_STEW, ModItems.STEW_CAULDRON);

        // Register cauldron interaction behavior
        CauldronBehavior.register();

        LOGGER.info("Perpetual Stew mod initialized!");
    }
}
