package mod.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import mod.Mod;

public class ModParticles {

    // Rejestrujemy prosty typ cząsteczki
    public static final SimpleParticleType ECHO_FIREWORK = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(Mod.MOD_ID, "particle_echo_firework"), ECHO_FIREWORK);
    }
}