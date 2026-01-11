package mod.items;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class UpgradedFireworkRocketItem extends FireworkRocketItem {

    public UpgradedFireworkRocketItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        // Ustaw zwiększony flight duration
        if (!itemStack.contains(DataComponentTypes.FIREWORKS)) {
            // 4 poziomy lotu (powyżej vanilla max!)
            FireworksComponent fireworks = new FireworksComponent(
                    5, // Flight duration
                    java.util.List.of()
            );
            itemStack.set(DataComponentTypes.FIREWORKS, fireworks);
        }

        return super.use(world, user, hand);
    }
}