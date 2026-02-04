package ninja.trek.perpetualstew;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ninja.trek.perpetualstew.block.entity.StewCauldronBlockEntity;
import ninja.trek.perpetualstew.registry.ModBlocks;

public class CauldronBehavior {
    public static void register() {
        UseBlockCallback.EVENT.register(CauldronBehavior::onUseBlock);
    }

    private static InteractionResult onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        BlockState state = level.getBlockState(hitResult.getBlockPos());
        ItemStack stack = player.getItemInHand(hand);

        // Check for full water cauldron
        if (state.is(Blocks.WATER_CAULDRON)) {
            int cauldronLevel = state.getValue(LayeredCauldronBlock.LEVEL);
            if (cauldronLevel == 3 && stack.getItem().components().has(net.minecraft.core.component.DataComponents.FOOD)) {
                if (!level.isClientSide()) {
                    // Replace with stew cauldron
                    level.setBlock(hitResult.getBlockPos(), ModBlocks.STEW_CAULDRON.defaultBlockState(), 3);

                    // Get the new block entity and add the ingredient
                    if (level.getBlockEntity(hitResult.getBlockPos()) instanceof StewCauldronBlockEntity stewCauldron) {
                        stewCauldron.addIngredient(stack.getItem(), 5);
                    }

                    // Consume the item
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    level.playSound(null, hitResult.getBlockPos(), SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
