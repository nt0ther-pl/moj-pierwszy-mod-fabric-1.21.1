package mod.items;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

public class JadeToolMaterial implements ToolMaterial {
    public static final JadeToolMaterial INSTANCE = new JadeToolMaterial();

    @Override
    public int getDurability() {
        return 2581;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 9.0f; // Szybsze niż diament (8.0)
    }

    @Override
    public float getAttackDamage() {
        return 3.0f; // Jak diament
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
    }

    @Override
    public int getEnchantability() {
        return 12; // Lepsze niż diament (10)
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(ModItems.POLISHED_JADE);
    }
}