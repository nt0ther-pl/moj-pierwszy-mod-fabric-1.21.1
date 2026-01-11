package mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import mod.items.ModItems;

public class JadeBlock extends Block {
    private final Block polishedVariant;

    public JadeBlock(Settings settings, Block polishedVariant) {
        super(settings);
        this.polishedVariant = polishedVariant;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos,
                                 PlayerEntity player, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(player.getActiveHand());

        // Sprawdź czy gracz trzyma papier ścierny
        if (itemStack.isOf(ModItems.POLISHING_PAPER)) {
            if (!world.isClient) {
                world.setBlockState(pos, polishedVariant.getDefaultState());

                // Odtwórz dźwięk
                world.playSound(null, pos, SoundEvents.BLOCK_GRINDSTONE_USE,
                        SoundCategory.BLOCKS, 1.0f, 1.0f);

                // Zużyj jeden papier ścierny (jeśli nie creative mode)
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
            }

            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }
}