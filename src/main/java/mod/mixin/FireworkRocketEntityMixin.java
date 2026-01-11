package mod.mixin;

import mod.items.ModItems;
import mod.particle.ModParticles; // Import twoich cząsteczek
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {

    @Shadow
    public abstract ItemStack getStack();

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        FireworkRocketEntity self = (FireworkRocketEntity) (Object) this;
        ItemStack stack = this.getStack();
        World world = self.getWorld();

        if (!stack.isEmpty() && stack.isOf(ModItems.UPGRADED_FIREWORKS)) {
            Vec3d velocity = self.getVelocity();

            // 1. Logika Prędkości (ta co wcześniej)
            if (velocity.lengthSquared() < 5.0) {
                self.setVelocity(velocity.multiply(1.1));
            }

            // 2. Logika Cząsteczek (NOWA)
            // Wykonujemy tylko po stronie klienta (wizualne efekty)
            if (world.isClient) {
                // Generujemy cząsteczkę w aktualnej pozycji rakiety
                // Dodajemy trochę losowości (-0.1 do 0.1), żeby dym był "szeroki"
                double randomX = (world.random.nextDouble() - 0.5) * 0.2;
                double randomY = (world.random.nextDouble() - 0.5) * 0.2;
                double randomZ = (world.random.nextDouble() - 0.5) * 0.2;

                // velocity.x * -0.5 oznacza, że cząsteczki będą lecieć lekko "do tyłu" za rakietą
                world.addParticle(ModParticles.ECHO_FIREWORK,
                        self.getX(), self.getY(), self.getZ(),
                        velocity.x * -0.5 + randomX,
                        velocity.y * -0.5 + randomY,
                        velocity.z * -0.5 + randomZ
                );
            }
        }
    }
}