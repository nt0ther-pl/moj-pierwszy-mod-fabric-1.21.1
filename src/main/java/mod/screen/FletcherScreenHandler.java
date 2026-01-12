package mod.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class FletcherScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    // Konstruktor dla klienta (pusty, synchronizowany)
    public FletcherScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(195));
    }

    // Konstruktor główny (serwer + logika slotów)
    public FletcherScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.FLETCHING_SCREEN_HANDLER, syncId);
        checkSize(inventory, 19);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        // --- UKŁAD SLOTÓW STOŁU ---

        // 1. INPUT (Surowiec) - Index 0
        this.addSlot(new Slot(inventory, 0, 48, 35));

        // 2. CRAFTING (7 pionowe) - Index 1, 2, 3
        this.addSlot(new Slot(inventory, 1, 76, 17)); // Grot
        this.addSlot(new Slot(inventory, 2, 76, 35)); // Trzon
        this.addSlot(new Slot(inventory, 3, 76, 53)); // Lotka
        this.addSlot(new Slot(inventory, 3, 76, 53)); // Dół łuku
        this.addSlot(new Slot(inventory, 3, 76, 53)); // Środek łuku
        this.addSlot(new Slot(inventory, 3, 76, 53)); // Góra łuku
        this.addSlot(new Slot(inventory, 3, 76, 53)); // Struna

        // 3. OUTPUT (Wynik) - Index 4
        this.addSlot(new Slot(inventory, 4, 116, 35) {
            @Override
            public boolean canInsert(ItemStack stack) { return false; } // Blokada wkładania
        });

        // 4. MAGAZYN (2x5 po lewej) - Index 5-14
        for (int row = 0; row < 5; ++row) {
            this.addSlot(new Slot(inventory, 5 + row, 8, 17 + row * 18));  // Kolumna 1
            this.addSlot(new Slot(inventory, 10 + row, 26, 17 + row * 18)); // Kolumna 2
        }

        // --- EKWIPUNEK GRACZA ---
        // Dostosowałem Y (120), bo Twoje gui jest wysokie przez magazyn 5-rzędowy
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (invSlot < this.inventory.size()) { // Ze stołu do gracza
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) { // Z gracza do stołu
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                // Y = 120 (podwyższone, żeby zmieścić magazyn)
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 120 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            // Y = 178 (Hotbar pod ekwipunkiem)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 178));
        }
    }
}