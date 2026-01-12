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

    // Konstruktor Klienta
    public FletcherScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(19));
        System.out.println("DEBUG: Otwieranie GUI po stronie KLIENTA - OK");
    }

    // Konstruktor Serwera
    public FletcherScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.FLETCHING_SCREEN_HANDLER, syncId);
        checkSize(inventory, 19);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        System.out.println("DEBUG: Otwieranie GUI po stronie SERWERA - OK");

        // --- UKŁAD SLOTÓW STOŁU ---

        // 1. INPUT (Surowiec) - Index 0
        this.addSlot(new Slot(inventory, 0, 128, 59));

        // 2. CRAFTING (7 pionowe) - Index 1, 2, 3
        this.addSlot(new Slot(inventory, 1, 183, 13)); // Grot
        this.addSlot(new Slot(inventory, 2, 183, 33)); // Trzon
        this.addSlot(new Slot(inventory, 3, 183, 53)); // Lotka
        this.addSlot(new Slot(inventory, 3, 77, 53)); // Dół łuku
        this.addSlot(new Slot(inventory, 3, 57, 33)); // Środek łuku
        this.addSlot(new Slot(inventory, 3, 75, 13)); // Góra łuku
        this.addSlot(new Slot(inventory, 3, 93, 33)); // Struna

        // 3. OUTPUT (Wynik) - Index 4
        this.addSlot(new Slot(inventory, 4, 128, 32) {
            @Override
            public boolean canInsert(ItemStack stack) { return false; } // Blokada wkładania
        });

        // 4. MAGAZYN (2x5 po lewej) - Index 5-14
        for (int row = 0; row < 5; ++row) {
            this.addSlot(new Slot(inventory, 5 + row, 7, 47 + row * 18));  // Kolumna 1
            this.addSlot(new Slot(inventory, 10 + row, 25, 47 + row * 18)); // Kolumna 2
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
        //return this.inventory.canPlayerUse(player);
        System.out.println("DEBUG: Sprawdzanie canUse... ZWRACAM TRUE");
        return true;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                // Y = 120 (podwyższone, żeby zmieścić magazyn)
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 56 + l * 18, 83 + i * 18));
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