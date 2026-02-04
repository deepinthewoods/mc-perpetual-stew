package ninja.trek.perpetualstew.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import ninja.trek.perpetualstew.block.entity.StewCauldronBlockEntity;
import ninja.trek.perpetualstew.component.StewData;
import ninja.trek.perpetualstew.registry.ModDataComponents;
import ninja.trek.perpetualstew.registry.ModItems;
import org.jetbrains.annotations.Nullable;

public class StewCauldronBlock extends BaseEntityBlock {
    public static final MapCodec<StewCauldronBlock> CODEC = simpleCodec(StewCauldronBlock::new);
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 3);

    public StewCauldronBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 3));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StewCauldronBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof StewCauldronBlockEntity stewCauldron)) {
            return InteractionResult.PASS;
        }

        // Adding food ingredient
        if (stack.getItem().components().has(net.minecraft.core.component.DataComponents.FOOD)) {
            if (!level.isClientSide()) {
                stewCauldron.addIngredient(stack.getItem(), 5);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        // Extracting stew with bowl
        if (stack.is(Items.BOWL)) {
            if (!level.isClientSide()) {
                int ingredientCount = stewCauldron.extractStew();
                if (ingredientCount > 0) {
                    ItemStack stewStack = new ItemStack(ModItems.PERPETUAL_STEW);
                    stewStack.set(ModDataComponents.STEW_DATA, new StewData(ingredientCount));

                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    if (!player.getInventory().add(stewStack)) {
                        player.drop(stewStack, false);
                    }

                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);

                    // Check if cauldron should revert to empty
                    if (stewCauldron.isEmpty()) {
                        level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return InteractionResult.PASS;
    }
}
