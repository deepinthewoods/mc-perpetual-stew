package ninja.trek.perpetualstew.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import ninja.trek.perpetualstew.component.StewData;
import ninja.trek.perpetualstew.registry.ModDataComponents;

public class PerpetualStewItem extends Item {
    public static final FoodProperties STEW_FOOD = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.6f) // Results in 7.2 saturation
            .build();

    public PerpetualStewItem(Properties properties) {
        super(properties.food(STEW_FOOD));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide() && entity instanceof Player player) {
            StewData data = stack.get(ModDataComponents.STEW_DATA);
            int ingredientCount = data != null ? data.ingredientCount() : 1;

            applyEffects(player, ingredientCount);
        }

        ItemStack result = super.finishUsingItem(stack, level, entity);

        if (entity instanceof Player player) {
            if (player.getAbilities().instabuild) {
                return result;
            }
            ItemStack bowl = new ItemStack(Items.BOWL);
            if (!player.getInventory().add(bowl)) {
                player.drop(bowl, false);
            }
        }

        return result;
    }

    private void applyEffects(Player player, int ingredientCount) {
        if (ingredientCount >= 3) {
            // Tier 3: Regen II (30 sec), Absorption II (2 min), Resistance I (30 sec)
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 30 * 20, 1)); // 30 seconds, level 2
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2 * 60 * 20, 1)); // 2 minutes, level 2
            player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 30 * 20, 0)); // 30 seconds, level 1
        } else if (ingredientCount == 2) {
            // Tier 2: Regen I (10 sec)
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 10 * 20, 0)); // 10 seconds, level 1
        }
        // Tier 1: No special effects, just food restoration
    }
}
