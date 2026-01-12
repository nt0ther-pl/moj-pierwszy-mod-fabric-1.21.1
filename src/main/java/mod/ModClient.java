package mod;

import mod.particle.ModParticles;
import mod.screen.FletcherScreen;
import mod.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.particle.EndRodParticle; // <--- ZMIANA IMPORTU

public class ModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Zmieniamy FireworksSparkParticle na EndRodParticle
        ParticleFactoryRegistry.getInstance().register(ModParticles.ECHO_FIREWORK, EndRodParticle.Factory::new);
        HandledScreens.register(ModScreenHandlers.FLETCHING_SCREEN_HANDLER, FletcherScreen::new);
    }
}