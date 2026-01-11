package mod.blocks;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import mod.Mod;

public class ModBlocks {

    public static final Block JADE_ORE = registerBlock("jade_ore",
            new Block(AbstractBlock.Settings.create()
                    .strength(10.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)));
    public static final Block POLISHED_JADE_BLOCK = registerBlock("polished_jade_block",
            new Block(AbstractBlock.Settings.create()
                    .strength(15.0f, 600.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.COPPER_BULB)));
    public static final Block JADE_BLOCK = registerBlock("jade_block",
            new JadeBlock(
                    AbstractBlock.Settings.create()
                            .strength(15.0f, 200.0f)
                            .requiresTool()
                            .sounds(BlockSoundGroup.AMETHYST_CLUSTER),
                    POLISHED_JADE_BLOCK));

    public static final Block JADE_COATED_ANVIL = registerBlock("jade_coated_anvil",
            new JadeCoatedAnvilBlock(AbstractBlock.Settings.copy(Blocks.ANVIL)));

    public static final Block CHIPPED_JADE_COATED_ANVIL = registerBlock("jade_coated_anvil_chipped",
            new JadeCoatedAnvilBlock(AbstractBlock.Settings.copy(Blocks.CHIPPED_ANVIL)));

    public static final Block DAMAGED_JADE_COATED_ANVIL = registerBlock("jade_coated_anvil_damaged",
            new JadeCoatedAnvilBlock(AbstractBlock.Settings.copy(Blocks.DAMAGED_ANVIL)));

    public static final Block CUT_POLISHED_JADE_BLOCK = registerBlock("cut_polished_jade_block",
            new Block(AbstractBlock.Settings.create()
                    .strength(15.0f, 600.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.COPPER_BULB)));

    public static final Block CUT_POLISHED_JADE_SLAB = registerBlock("cut_polished_jade_slab",
            new SlabBlock(AbstractBlock.Settings.create()
                    .strength(15.0f, 600.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.COPPER_BULB)));

    public static final Block CUT_POLISHED_JADE_STAIRS = registerBlock("cut_polished_jade_stairs",
            new StairsBlock(CUT_POLISHED_JADE_BLOCK.getDefaultState(),
                    AbstractBlock.Settings.create()
                            .strength(15.0f, 600.0f)
                            .requiresTool()
                            .sounds(BlockSoundGroup.COPPER_BULB)));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Mod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Mod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        Mod.LOGGER.info("Registering Mod Blocks for " + Mod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.JADE_ORE);
            entries.add(ModBlocks.JADE_BLOCK);
            entries.add(ModBlocks.POLISHED_JADE_BLOCK);
            entries.add(ModBlocks.CUT_POLISHED_JADE_BLOCK);
            entries.add(ModBlocks.CUT_POLISHED_JADE_SLAB);
            entries.add(ModBlocks.CUT_POLISHED_JADE_STAIRS);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries ->{
            entries.add(ModBlocks.JADE_COATED_ANVIL);
        });
    }
}