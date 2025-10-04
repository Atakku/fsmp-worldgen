package dev.atakku.fsmp.worldgen.mixin;

import net.minecraft.SharedConstants;
import net.minecraft.world.level.ChunkPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.atakku.fsmp.worldgen.Worldgen;

@Mixin(SharedConstants.class)
public class MixinSharedConstants {
  @Inject(at = @At("HEAD"), cancellable = true, method = "Lnet/minecraft/SharedConstants;debugVoidTerrain(Lnet/minecraft/world/level/ChunkPos;)Z")
  private void debugVoidTerrain(ChunkPos p, CallbackInfoReturnable<Boolean> cir) {
    cir.setReturnValue(p.x >= Worldgen.CR || p.z >= Worldgen.CR || p.x < -Worldgen.CR || p.z < -Worldgen.CR);
  }
}
