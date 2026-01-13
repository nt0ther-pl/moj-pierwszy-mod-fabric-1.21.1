package mod.blocks.entity;

import mod.Mod;
import mod.blocks.ModBlocks; // Import Twoich bloków
import net.minecraft.block.Blocks; // Import bloków Minecrafta (dla Fletchera)
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    // Zmienna 1: KOWADŁO (To pewnie zniknęło!)
    public static BlockEntityType<JadeCoatedAnvilBlockEntity> JADE_COATED_ANVIL_BE;

    // Zmienna 2: FLETCHER
    public static BlockEntityType<FletcherTableBlockEntity> FLETCHER_TABLE_BE;

    public static void registerBlockEntities() {
        // Rejestracja KOWADŁA
        JADE_COATED_ANVIL_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(Mod.MOD_ID, "jade_coated_anvil_be"),
                // Ważne: Tu musi być Twój blok kowadła (ModBlocks.JADE_COATED_ANVIL)
                BlockEntityType.Builder.create(JadeCoatedAnvilBlockEntity::new, ModBlocks.JADE_COATED_ANVIL).build(null)
        );

        // Rejestracja FLETCHERA
        FLETCHER_TABLE_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(Mod.MOD_ID, "fletching_table_be"),
                BlockEntityType.Builder.create(FletcherTableBlockEntity::new, Blocks.FLETCHING_TABLE).build(null)
        );
    }
}