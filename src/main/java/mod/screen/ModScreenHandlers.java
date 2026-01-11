package mod.screen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import mod.Mod;

public class ModScreenHandlers {
    public static final ScreenHandlerType<JadeCoatedAnvilScreenHandler> JADE_COATED_ANVIL_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Mod.MOD_ID, "jade_coated_anvil"),
                    new ScreenHandlerType<>(JadeCoatedAnvilScreenHandler::new, null));

    public static void registerScreenHandlers() {
        Mod.LOGGER.info("Registering Screen Handlers for " + Mod.MOD_ID);
    }
} 