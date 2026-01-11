package mod.blocks;

import mod.screen.JadeCoatedAnvilScreenHandler;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class JadeCoatedAnvilBlock extends AnvilBlock {
    private static final Text TITLE = Text.translatable("container.mod.jade_coated_anvil");
    // Definicja kształtów - dzięki temu kowadło nie jest pełnym blokiem (pustym pudełkiem)
    private static final VoxelShape SHAPE_X = Block.createCuboidShape(0.0, 0.0, 2.0, 16.0, 16.0, 14.0);
    private static final VoxelShape SHAPE_Z = Block.createCuboidShape(2.0, 0.0, 0.0, 14.0, 16.0, 16.0);

    public JadeCoatedAnvilBlock(Settings settings) {
        super(settings);
    }

    // Pozwala przechodzić "obok" kowadła (nie jest pełnym sześcianem)
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return direction.getAxis() == Direction.Axis.X ? SHAPE_X : SHAPE_Z;
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new NamedScreenHandlerFactory() {
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new JadeCoatedAnvilScreenHandler(syncId, playerInventory);
            }

            @Override
            public Text getDisplayName() {
                return TITLE;
            }
        };
    }

    // Ustawia orientację kowadła zależnie od tego, jak stoi gracz
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().rotateYClockwise());
    }

    // NAPRAWIONA METODA onUse (to ona generowała błąd na linii 74 przez parametr Hand)
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
            if (factory != null) {
                player.openHandledScreen(factory);
            }
            return ActionResult.CONSUME;
        }
    }

    // Twoja logika 5x większej wytrzymałości - rzadsze przechodzenie w stany uszkodzone
    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, net.minecraft.entity.Entity entity, float fallDistance) {
        // Najpierw obsłuż obrażenia od upadku
        entity.handleFallDamage(fallDistance, 1.0F, world.getDamageSources().fall());

        // Potem sprawdź czy kowadło się uszkodzi
        if (!world.isClient && fallDistance > 0.5F) {
            // 5x wytrzymalsze: 12% / 5 = 2.4%
            if (world.random.nextFloat() < 0.024F) {
                BlockState newState = null;

                if (state.isOf(ModBlocks.JADE_COATED_ANVIL)) {
                    newState = ModBlocks.CHIPPED_JADE_COATED_ANVIL.getDefaultState();
                } else if (state.isOf(ModBlocks.CHIPPED_JADE_COATED_ANVIL)) {
                    newState = ModBlocks.DAMAGED_JADE_COATED_ANVIL.getDefaultState();
                }

                if (newState != null) {
                    world.setBlockState(pos, newState.with(FACING, state.get(FACING)), Block.NOTIFY_ALL);
                    world.syncWorldEvent(1031, pos, 0); // Dźwięk uszkodzenia
                }
            }
        }
    }
}