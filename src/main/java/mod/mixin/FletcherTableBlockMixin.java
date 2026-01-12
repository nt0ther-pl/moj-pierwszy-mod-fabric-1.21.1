package mod.mixin;

import mod.blocks.entity.FletcherTableBlockEntity; // Upewnij się, że pakiet jest dobry (blocks vs block)
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
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

// Podmieniamy FletchingTableBlock, żeby dziedziczył po BlockWithEntity (dzięki temu ma BlockEntity)
@Mixin(FletchingTableBlock.class)
public abstract class FletcherTableBlockMixin extends BlockWithEntity {

    protected FletcherTableBlockMixin(Settings settings) {
        super(settings);
    }

    // 1. Metoda tworząca BlockEntity w świecie
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FletcherTableBlockEntity(pos, state);
    }

    // 2. Metoda Renderowania (WAŻNE: bez tego blok będzie niewidzialny lub zbugowany przy BlockWithEntity)
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    // 3. Obsługa Prawego Przycisku Myszy (Otwieranie GUI)
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            // Pobieramy BlockEntity z pozycji
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                // Otwieramy GUI
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }
}