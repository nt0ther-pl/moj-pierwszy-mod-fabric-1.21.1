package mod.items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Formatting;
import net.minecraft.text.Text;
import java.util.List;
import net.minecraft.item.SmithingTemplateItem;
import mod.Mod;
import mod.items.UpgradedFireworkRocketItem;

public class ModItems {

    public static final Item POLISHED_JADE = registerItem("polished_jade", new Item(new Item.Settings()));
    public static final Item JADE = registerItem("jade", new Item(new Item.Settings()));
    public static final Item POLISHING_PAPER = registerItem("polishing_paper", new Item(new Item.Settings()));
    public static final Item JADE_UPGRADE_SMITHING_TEMPLATE = registerItem("jade_upgrade_smithing_template",
            createJadeUpgradeTemplate());
    public static final Item JADE_SWORD = registerItem("jade_sword",
            new SwordItem(JadeToolMaterial.INSTANCE,
                    new Item.Settings()
                            .attributeModifiers(SwordItem.createAttributeModifiers(
                                    JadeToolMaterial.INSTANCE, 3, -1.8f))));
    public static final Item JADE_SWORD_NETHERITE_UPGRADE = registerItem("jade_sword_netherite_upgrade",
            new SwordItem(JadeToolNetheriteUpgradeMaterial.INSTANCE,
                    new Item.Settings()
                            .attributeModifiers(SwordItem.createAttributeModifiers(
                                    JadeToolNetheriteUpgradeMaterial.INSTANCE, 3, -1.8f))));
    public static final Item JADE_NETHERITE_UPGRADE_SMITHING_TEMPLATE_FRAGMENT =
            registerItem("jade_netherite_upgrade_smithing_template_fragment",
                    new Item(new Item.Settings()));
    public static final Item JADE_NETHERITE_UPGRADE_SMITHING_TEMPLATE =
            registerItem("jade_netherite_upgrade_smithing_template",
                    new Item(new Item.Settings()));

    public static final Item JADE_IRON_HAMMER = registerItem("jade_iron_hammer",
            new Item(new Item.Settings().maxCount(1).maxDamage(64)));

    public static final Item ECHO_POWDER = registerItem("echo_powder",
            new Item(new Item.Settings()));

    public static final Item ECHO_GUNPOWDER = registerItem("echo_gunpowder",
            new Item(new Item.Settings()));

    public static final Item STRING_NET = registerItem("string_net",
            new Item(new Item.Settings()));

    public static final Item UPGRADED_FIREWORKS = registerItem("upgraded_fireworks",
            new UpgradedFireworkRocketItem(new Item.Settings()
                    .component(DataComponentTypes.FIREWORKS, new FireworksComponent(5, List.of()))
            ));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Mod.MOD_ID, name), item);
    }

    public static void registeredModItems() {
        Mod.LOGGER.info("Registering Mod Items for: " + Mod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(POLISHED_JADE);
            entries.add(JADE);
            entries.add(POLISHING_PAPER);
            entries.add(JADE_UPGRADE_SMITHING_TEMPLATE);
            entries.add(JADE_NETHERITE_UPGRADE_SMITHING_TEMPLATE_FRAGMENT);
            entries.add(JADE_NETHERITE_UPGRADE_SMITHING_TEMPLATE);
            entries.add(ECHO_POWDER);
            entries.add(ECHO_GUNPOWDER);
            entries.add(STRING_NET);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(JADE_SWORD);
            entries.add(JADE_SWORD_NETHERITE_UPGRADE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(UPGRADED_FIREWORKS);
            entries.add(JADE_IRON_HAMMER);
        });

    }
    private static Item createJadeUpgradeTemplate() {
        return new SmithingTemplateItem(
                Text.translatable("upgrade.mod.jade_upgrade.applies_to").formatted(Formatting.BLUE),
                Text.translatable("upgrade.mod.jade_upgrade.ingredients").formatted(Formatting.BLUE),
                Text.translatable("upgrade.mod.jade_upgrade").formatted(Formatting.GRAY),
                Text.translatable("upgrade.mod.jade_upgrade.base_slot_description"),
                Text.translatable("upgrade.mod.jade_upgrade.additions_slot_description"),
                createJadeUpgradeIconList(),
                createJadeUpgradeBaseSlotIconList()
        );
    }

    private static List<Identifier> createJadeUpgradeBaseSlotIconList() {
        return List.of(
                Identifier.of(Mod.MOD_ID, "item/empty_slot_jade")
        );
    }

    private static List<Identifier> createJadeUpgradeIconList() {
        return List.of(
                Identifier.ofVanilla("item/empty_slot_sword")
        );
    }
}
