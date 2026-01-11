package mod.screen;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.enchantment.EnchantmentHelper;
import mod.items.ModItems;

public class JadeCoatedAnvilScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final Property levelCost = Property.create();

    // Konstruktor dla CLIENT
    public JadeCoatedAnvilScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(5));
    }

    // Główny konstruktor
    public JadeCoatedAnvilScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.JADE_COATED_ANVIL_SCREEN_HANDLER, syncId);
        checkSize(inventory, 5);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        // Dodaj property dla kosztów XP
        this.addProperty(this.levelCost);

// Slot 0: Lewy dolny input - z listenerem
        this.addSlot(new Slot(inventory, 0, 26, 51) {
            @Override
            public void markDirty() {
                super.markDirty();
                System.out.println("SLOT 0 ZMIENIONY!");
                JadeCoatedAnvilScreenHandler.this.updateResult();
            }
        });

// Slot 1: Środkowy dolny input - z listenerem
        this.addSlot(new Slot(inventory, 1, 75, 51) {
            @Override
            public void markDirty() {
                super.markDirty();
                System.out.println("SLOT 1 ZMIENIONY!");
                JadeCoatedAnvilScreenHandler.this.updateResult();
            }
        });

        // Slot 2: Środkowy górny input (ZABLOKOWANY dla repair/enchant)
        this.addSlot(new Slot(inventory, 2, 75, 20));

        // Slot 3: Prawy górny input (ZABLOKOWANY dla repair/enchant)
        this.addSlot(new Slot(inventory, 3, 104, 20));

// Slot 4: Output
        this.addSlot(new Slot(inventory, 4, 133, 51) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity player) {
                int cost = JadeCoatedAnvilScreenHandler.this.levelCost.get();
                return cost > 0 && this.hasStack() &&
                        (player.getAbilities().creativeMode || player.experienceLevel >= cost);
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                if (!player.getAbilities().creativeMode) {
                    player.addExperienceLevels(-JadeCoatedAnvilScreenHandler.this.levelCost.get());
                }

                // Sprawdź czy to receptura z fragmentami (4 sloty)
                ItemStack leftBottom = JadeCoatedAnvilScreenHandler.this.getSlot(0).getStack();
                ItemStack middleBottom = JadeCoatedAnvilScreenHandler.this.getSlot(1).getStack();
                ItemStack middleTop = JadeCoatedAnvilScreenHandler.this.getSlot(2).getStack();
                ItemStack rightTop = JadeCoatedAnvilScreenHandler.this.getSlot(3).getStack();

                // Echo Powder receptura (zużywa 3 itemy + uszkadza hammer)
                boolean isEchoPowderRecipe =
                        leftBottom.isOf(ModItems.JADE_IRON_HAMMER) &&
                                middleBottom.isOf(Items.ECHO_SHARD) &&
                                middleTop.isOf(ModItems.STRING_NET);

                if (isEchoPowderRecipe) {
                    // Uszkodź hammer zamiast go zużyć całkowicie
                    ItemStack hammer = JadeCoatedAnvilScreenHandler.this.getSlot(0).getStack();
                    hammer.damage(1, player, net.minecraft.entity.EquipmentSlot.MAINHAND);

                    // Zużyj echo shard i string net
                    JadeCoatedAnvilScreenHandler.this.getSlot(1).getStack().decrement(1);
                    JadeCoatedAnvilScreenHandler.this.getSlot(2).getStack().decrement(1);

                    JadeCoatedAnvilScreenHandler.this.updateResult();
                    return;
                }


                boolean isFragmentRecipe =
                        leftBottom.isOf(ModItems.JADE_NETHERITE_UPGRADE_SMITHING_TEMPLATE_FRAGMENT) &&
                                middleBottom.isOf(ModItems.JADE_NETHERITE_UPGRADE_SMITHING_TEMPLATE_FRAGMENT) &&
                                middleTop.isOf(Items.NETHERITE_INGOT) &&
                                rightTop.isOf(Items.ECHO_SHARD);

                if (isFragmentRecipe) {
                    // Zużyj wszystkie 4 itemy
                    JadeCoatedAnvilScreenHandler.this.getSlot(0).getStack().decrement(1);
                    JadeCoatedAnvilScreenHandler.this.getSlot(1).getStack().decrement(1);
                    JadeCoatedAnvilScreenHandler.this.getSlot(2).getStack().decrement(1);
                    JadeCoatedAnvilScreenHandler.this.getSlot(3).getStack().decrement(1);
                } else {
                    // Normalna receptura - zużyj tylko dolne 2 sloty
                    JadeCoatedAnvilScreenHandler.this.getSlot(0).getStack().decrement(1);
                    JadeCoatedAnvilScreenHandler.this.getSlot(1).getStack().decrement(1);
                }

                JadeCoatedAnvilScreenHandler.this.updateResult();
            }
        });

        // Player Inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        8 + col * 18,
                        84 + row * 18));
            }
        }

        // Hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i,
                    8 + i * 18,
                    142));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (inventory == this.inventory) {
            System.out.println("onContentChanged wywołane!"); // DEBUG
            this.updateResult();
        }
    }

    public void updateResult() {
        ItemStack leftBottom = this.getSlot(0).getStack();   // Slot 0
        ItemStack middleBottom = this.getSlot(1).getStack(); // Slot 1
        ItemStack middleTop = this.getSlot(2).getStack();    // Slot 2 (ignorowany dla repair/enchant)
        ItemStack rightTop = this.getSlot(3).getStack();     // Slot 3 (ignorowany dla repair/enchant)

        // Reset
        this.getSlot(4).setStack(ItemStack.EMPTY);
        this.levelCost.set(0);

        // ========================================
        // RECEPTURA: ECHO POWDER (Jade Hammer + Echo Shard + String Net)
        // ========================================
        if (leftBottom.isOf(ModItems.JADE_IRON_HAMMER) &&
                middleBottom.isOf(Items.ECHO_SHARD) &&
                middleTop.isOf(ModItems.STRING_NET) &&
                rightTop.isEmpty()) {

            ItemStack result = new ItemStack(ModItems.ECHO_POWDER, 4); // 4 echo powder

            this.getSlot(4).setStack(result);
            this.levelCost.set(5); // 5 poziomów XP
            return;
        }

        // ========================================
        // CUSTOM RECEPTURA: 2x Fragment + Netherite Ingot + Echo Shard
        // ========================================
        if (leftBottom.isOf(ModItems.JADE_NETHERITE_UPGRADE_SMITHING_TEMPLATE_FRAGMENT) &&
                middleBottom.isOf(ModItems.JADE_NETHERITE_UPGRADE_SMITHING_TEMPLATE_FRAGMENT) &&
                middleTop.isOf(Items.NETHERITE_INGOT) &&
                rightTop.isOf(Items.ECHO_SHARD)) {

            // Utwórz pełny template
            ItemStack result = new ItemStack(ModItems.JADE_NETHERITE_UPGRADE_SMITHING_TEMPLATE);

            this.getSlot(4).setStack(result);
            this.levelCost.set(1); // 1 poziomów XP
            return;
        }

        // PRIORYTET 1: NAPRAWA JADE SWORD
        if (leftBottom.isOf(ModItems.JADE_SWORD) && middleBottom.isOf(ModItems.POLISHED_JADE)) {
            if (leftBottom.isDamaged()) {
                ItemStack result = leftBottom.copy();

                // Napraw 25% durability
                int repairAmount = result.getMaxDamage() / 4;
                int newDamage = Math.max(0, result.getDamage() - repairAmount);
                result.setDamage(newDamage);

                this.getSlot(4).setStack(result);
                this.levelCost.set(2); // 2 poziomy XP
                return;
            }
        }

        // PRIORYTET 2: NAPRAWA JADE-NETHERITE SWORD
        if (leftBottom.isOf(ModItems.JADE_SWORD_NETHERITE_UPGRADE) && middleBottom.isOf(ModItems.POLISHED_JADE)) {
            if (leftBottom.isDamaged()) {
                ItemStack result = leftBottom.copy();

                int repairAmount = result.getMaxDamage() / 4;
                int newDamage = Math.max(0, result.getDamage() - repairAmount);
                result.setDamage(newDamage);

                this.getSlot(4).setStack(result);
                this.levelCost.set(3); // 3 poziomy XP
                return;
            }
        }

        // PRIORYTET 3: ENCHANTOWANIE KSIĄŻKĄ
        if (!leftBottom.isEmpty() && middleBottom.isOf(Items.ENCHANTED_BOOK)) {
            ItemStack result = leftBottom.copy();

            // Pobierz enchantmenty z książki
            var bookEnchantments = EnchantmentHelper.getEnchantments(middleBottom);

            if (!bookEnchantments.isEmpty()) {
                // Dodaj enchantmenty do przedmiotu
                EnchantmentHelper.set(result, bookEnchantments);

                this.getSlot(4).setStack(result);
                this.levelCost.set(5); // 5 poziomów XP
                return;
            }
        }

        // ========================================
        // RECEPTURA: NAPRAWA PRZEDMIOTU SUROWCEM
        // ========================================
        if (!leftBottom.isEmpty() && !middleBottom.isEmpty() && middleTop.isEmpty() && rightTop.isEmpty()) {

            // Sprawdź czy przedmiot można naprawić tym materiałem
            if (leftBottom.isDamaged() && leftBottom.getItem().canRepair(leftBottom, middleBottom)) {
                ItemStack result = leftBottom.copy();

                // Napraw 25% durability za każdy surowiec
                int repairAmount = result.getMaxDamage() / 4;
                int newDamage = Math.max(0, result.getDamage() - repairAmount);
                result.setDamage(newDamage);

                // Dodaj work penalty
                int repairCost = result.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
                result.set(DataComponentTypes.REPAIR_COST, repairCost + 1);

                this.getSlot(4).setStack(result);
                this.levelCost.set(Math.max(1, repairCost)); // Koszt rośnie z użyciem
                return;
            }
        }

        // ========================================
        // RECEPTURA: ŁĄCZENIE 2 TAKICH SAMYCH PRZEDMIOTÓW
        // ========================================
        if (!leftBottom.isEmpty() && !middleBottom.isEmpty() && middleTop.isEmpty() && rightTop.isEmpty()) {

            // Sprawdź czy to ten sam przedmiot
            if (leftBottom.getItem() == middleBottom.getItem() && leftBottom.getMaxDamage() > 0) {
                ItemStack result = leftBottom.copy();

                // Połącz durability z obu przedmiotów + 12% bonus
                int leftDurability = leftBottom.getMaxDamage() - leftBottom.getDamage();
                int rightDurability = middleBottom.getMaxDamage() - middleBottom.getDamage();
                int bonus = result.getMaxDamage() * 12 / 100; // 12% bonus

                int totalDurability = leftDurability + rightDurability + bonus;
                int newDamage = Math.max(0, result.getMaxDamage() - totalDurability);
                result.setDamage(newDamage);

                // Work penalty - weź wyższy + 1
                int leftCost = leftBottom.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
                int rightCost = middleBottom.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
                int repairCost = Math.max(leftCost, rightCost) + 1;

                result.set(DataComponentTypes.REPAIR_COST, repairCost);

                this.getSlot(4).setStack(result);
                this.levelCost.set(Math.max(1, repairCost));
                return;
            }
        }

        // GÓRNE SLOTY BLOKUJĄ NORMALNE FUNKCJE
        if (!middleTop.isEmpty() || !rightTop.isEmpty()) {
            return;
        }
    }

    public int getLevelCost() {
        return this.levelCost.get();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (index == 4) {
                if (!this.insertItem(slotStack, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(slotStack, itemStack);
            } else if (index >= 5) {
                if (!this.insertItem(slotStack, 0, 4, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(slotStack, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, slotStack);
        }

        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onSlotClick(int slotIndex, int button, net.minecraft.screen.slot.SlotActionType actionType, PlayerEntity player) {
        super.onSlotClick(slotIndex, button, actionType, player);

        System.out.println("========== SLOT CLICKED ==========");
        System.out.println("Slot index: " + slotIndex);
        System.out.println("Action type: " + actionType);

        // Wywołaj updateResult() po każdej zmianie w slotach
        this.updateResult();
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        System.out.println("GUI ZAMKNIETE - zwracam itemy");

        // Zwróć itemy do gracza (tylko sloty input 0-3)
        if (!player.getWorld().isClient) {
            for (int i = 0; i < 4; i++) {
                ItemStack stack = this.inventory.getStack(i);
                if (!stack.isEmpty()) {
                    player.giveItemStack(stack);
                    this.inventory.setStack(i, ItemStack.EMPTY);
                }
            }
        }

        this.inventory.onClose(player);
    }
}