package zone.hrt.worldgen.mixin;

import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import zone.hrt.worldgen.Worldgen;

@Mixin(NoiseBasedChunkGenerator.class)
public class MixinNoiseBasedChunkGenerator {
  @Inject(at = @At("HEAD"), cancellable = true, method = "spawnOriginalMobs(Lnet/minecraft/server/level/WorldGenRegion;)V")
  private void spawnOriginalMobs(WorldGenRegion r, CallbackInfo ci) {
    if (Worldgen.isOutside(r.getCenter(), Worldgen.BORDER)) {
      ci.cancel();
    }
  }
}
