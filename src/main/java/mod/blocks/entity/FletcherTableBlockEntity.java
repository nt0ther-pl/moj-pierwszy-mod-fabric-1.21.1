package mod.blocks.entity;

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

// Rozszerzamy BlockEntity i implementujemy nasz Inventory oraz obsługę Menu
public class FletcherTableBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {

    // Lista przechowująca itemy. 19 slotów to suma twoich wymagań.
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(19, ItemStack.EMPTY);

    public FletcherTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLETCHING_TABLE_BE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    // Tytuł okna
    @Override
    public Text getDisplayName() {
        return Text.translatable("container.fletching_table");
    }

    // Zapisywanie danych (żeby itemy nie znikały po wyjściu z gry)
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    // Odczytywanie danych
    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    // To stworzymy w następnym kroku (Logika Menu)
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        // Zwracamy instancję naszego Handlera (logiki menu)
        // Upewnij się, że nazwa klasy to FletcherScreenHandler (lub FletchingScreenHandler - zależy jak nazwałeś plik)
        return new mod.screen.FletcherScreenHandler(syncId, playerInventory, this);
    }
}