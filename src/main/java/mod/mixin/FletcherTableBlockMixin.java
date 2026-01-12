package mod.mixin;

import mod.blocks.entity.FletcherTableBlockEntity; // Upewnij się, że to pasuje do Twojego pliku!
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

// Mixujemy oryginalny blok Minecrafta (FletchingTableBlock), ale dodajemy mu duszę (Provider)
@Mixin(FletchingTableBlock.class)
public abstract class FletcherTableBlockMixin extends Block implements BlockEntityProvider {

    public FletcherTableBlockMixin(Settings settings) {
        super(settings);
    }

    // 1. Renderowanie (Bez tego blok będzie niewidzialny!)
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    // 2. Tworzenie BlockEntity (Twojego 'Mózgu' stołu)
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        // Tu używamy Twojej nazwy: Fletcher
        return new FletcherTableBlockEntity(pos, state);
    }

    // 3. Otwieranie GUI Prawym Przyciskiem
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }
}