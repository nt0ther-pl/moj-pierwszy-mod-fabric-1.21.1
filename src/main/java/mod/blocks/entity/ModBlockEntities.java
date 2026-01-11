package mod.blocks.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static BlockEntityType<FletchingTableBlockEntity> FLETCHING_TABLE_BE;

    public static void registerBlockEntities() {
        FLETCHING_TABLE_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of("mod", "fletching_table_be"),
                FabricBlockEntityTypeBuilder.create(FletchingTableBlockEntity::new, Blocks.FLETCHING_TABLE).build()
        );
    }
}