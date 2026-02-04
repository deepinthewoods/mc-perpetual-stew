package ninja.trek.perpetualstew.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record StewData(int ingredientCount) {
    public static final Codec<StewData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("ingredient_count").forGetter(StewData::ingredientCount)
            ).apply(instance, StewData::new)
    );

    public static final StewData DEFAULT = new StewData(1);
}
