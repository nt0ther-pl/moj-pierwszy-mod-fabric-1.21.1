package mod;

import mod.blocks.entity.ModBlockEntities;
import mod.particle.ModParticles;
import net.fabricmc.api.ModInitializer;
import mod.blocks.ModBlocks;
import mod.items.ModItems;
import mod.screen.ModScreenHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mod implements ModInitializer {
    public static final String MOD_ID = "mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        ModItems.registeredModItems();
        ModBlocks.registerModBlocks();
        ModScreenHandlers.registerScreenHandlers();
        ModParticles.registerParticles();
        ModBlockEntities.registerBlockEntities();
	}
}