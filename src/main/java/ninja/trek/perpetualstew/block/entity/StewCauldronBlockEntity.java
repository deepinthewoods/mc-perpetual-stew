package ninja.trek.perpetualstew.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import ninja.trek.perpetualstew.registry.ModBlockEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StewCauldronBlockEntity extends BlockEntity {
    private final Map<Item, Integer> ingredients = new HashMap<>();

    // Codec for a single ingredient entry
    private static final Codec<IngredientEntry> INGREDIENT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("item").forGetter(e -> BuiltInRegistries.ITEM.getKey(e.item)),
                    Codec.INT.fieldOf("charges").forGetter(e -> e.charges)
            ).apply(instance, (id, charges) -> new IngredientEntry(BuiltInRegistries.ITEM.getValue(id), charges))
    );

    // Codec for the list of ingredients
    private static final Codec<List<IngredientEntry>> INGREDIENTS_CODEC = INGREDIENT_CODEC.listOf();

    private record IngredientEntry(Item item, int charges) {}

    public StewCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STEW_CAULDRON_BLOCK_ENTITY, pos, state);
    }

    public void addIngredient(Item item, int charges) {
        ingredients.merge(item, charges, Integer::sum);
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public int extractStew() {
        List<Item> itemsWithCharges = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : ingredients.entrySet()) {
            if (entry.getValue() > 0) {
                itemsWithCharges.add(entry.getKey());
                if (itemsWithCharges.size() >= 3) break;
            }
        }

        int count = 0;
        for (Item item : itemsWithCharges) {
            int current = ingredients.get(item);
            if (current > 1) {
                ingredients.put(item, current - 1);
            } else {
                ingredients.remove(item);
            }
            count++;
        }

        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }

        return count;
    }

    public int getTotalCharges() {
        return ingredients.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getUniqueIngredientCount() {
        return ingredients.size();
    }

    public boolean isEmpty() {
        return ingredients.isEmpty();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        List<IngredientEntry> entries = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : ingredients.entrySet()) {
            entries.add(new IngredientEntry(entry.getKey(), entry.getValue()));
        }
        output.store("ingredients", INGREDIENTS_CODEC, entries);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ingredients.clear();
        input.read("ingredients", INGREDIENTS_CODEC).ifPresent(entries -> {
            for (IngredientEntry entry : entries) {
                if (entry.item != null) {
                    ingredients.put(entry.item, entry.charges);
                }
            }
        });
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
