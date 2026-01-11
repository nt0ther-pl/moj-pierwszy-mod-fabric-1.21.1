package mod.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class FletchingScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    // Konstruktor dla klienta (pusty, synchronizowany)
    public FletchingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(15));
    }

    // Konstruktor główny (serwer + logika slotów)
    public FletchingScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.FLETCHING_SCREEN_HANDLER, syncId);
        checkSize(inventory, 15);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        // --- UKŁAD SLOTÓW STOŁU ---

        // 1. INPUT (Surowiec) - Index 0
        this.addSlot(new Slot(inventory, 0, 48, 35));

        // 2. CRAFTING (3 pionowe) - Index 1, 2, 3
        this.addSlot(new Slot(inventory, 1, 76, 17)); // Góra
        this.addSlot(new Slot(inventory, 2, 76, 35)); // Środek
        this.addSlot(new Slot(inventory, 3, 76, 53)); // Dół

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