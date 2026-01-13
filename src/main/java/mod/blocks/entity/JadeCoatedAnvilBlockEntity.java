package mod.blocks.entity;

import mod.screen.JadeCoatedAnvilScreenHandler;
import mod.util.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class JadeCoatedAnvilBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {

    // Załóżmy, że kowadło ma np. 2 sloty (input + materiał) + 1 output?
    // Dostosuj liczbę '3' do swoich potrzeb!
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public JadeCoatedAnvilBlockEntity(BlockPos pos, BlockState state) {
        // Tutaj 'ModBlockEntities.JADE_COATED_ANVIL_BE' nie może być nullem (załatwiliśmy to w Kroku 1)
        super(ModBlockEntities.JADE_COATED_ANVIL_BE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.jade_coated_anvil");
    }

    // Zapisywanie stanu (żeby itemy nie znikały po restarcie)
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new JadeCoatedAnvilScreenHandler(syncId, playerInventory, this);
    }
}