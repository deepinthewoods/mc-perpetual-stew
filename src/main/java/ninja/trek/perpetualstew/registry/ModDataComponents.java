package ninja.trek.perpetualstew.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import ninja.trek.perpetualstew.PerpetualStew;
import ninja.trek.perpetualstew.component.StewData;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DataComponentType<StewData> STEW_DATA = register("stew_data",
            builder -> builder.persistent(StewData.CODEC));

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                Identifier.fromNamespaceAndPath(PerpetualStew.MOD_ID, name),
                builder.apply(DataComponentType.builder()).build()
        );
    }

    public static void register() {
        PerpetualStew.LOGGER.info("Registering data components for " + PerpetualStew.MOD_ID);
    }
}
