package mod.screen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import mod.Mod;

public class ModScreenHandlers {
    // Twoje istniejące kowadło
    public static final ScreenHandlerType<JadeCoatedAnvilScreenHandler> JADE_COATED_ANVIL_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Mod.MOD_ID, "jade_coated_anvil"),
                    new ScreenHandlerType<>(JadeCoatedAnvilScreenHandler::new, FeatureSet.empty()));

    // --- DODAJ TO: Rejestracja Fletching Table ---
    public static final ScreenHandlerType<FletchingScreenHandler> FLETCHING_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Mod.MOD_ID, "fletching_table"),
                    new ScreenHandlerType<>(FletchingScreenHandler::new, FeatureSet.empty()));

    public static void registerScreenHandlers() {
        Mod.LOGGER.info("Registering Screen Handlers for " + Mod.MOD_ID);
    }
}