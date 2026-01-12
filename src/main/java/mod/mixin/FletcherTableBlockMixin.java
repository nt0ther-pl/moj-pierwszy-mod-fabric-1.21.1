package mod.mixin;

import mod.blocks.entity.FletcherTableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FletchingTableBlock.class)
public abstract class FletcherTableBlockMixin extends Block implements BlockEntityProvider {

    public FletcherTableBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FletcherTableBlockEntity(pos, state);
    }

    // Nadpisujemy onUse, aby ręcznie otworzyć GUI
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            // DEBUG: Sprawdzamy, czy w ogóle wchodzimy w interakcję
            System.out.println("DEBUG MIXIN: Kliknięto Fletching Table na serwerze!");

            // ZAMIAST: state.createScreenHandlerFactory(...)
            // ROBIMY: Pobieramy BlockEntity bezpośrednio z mapy
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof FletcherTableBlockEntity) {
                System.out.println("DEBUG MIXIN: Znaleziono FletcherTableBlockEntity! Otwieram GUI...");
                player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
            } else {
                System.out.println("DEBUG MIXIN: BlockEntity to NULL lub zły typ!");
            }
        }
        return ActionResult.SUCCESS;
    }
}