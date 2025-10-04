package dev.atakku.fsmp.worldgen.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.blending.Blender;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.atakku.fsmp.worldgen.Worldgen;

@Mixin(NoiseBasedChunkGenerator.class)
public class MixinNoiseBasedChunkGenerator {
  @Inject(at = @At("HEAD"), cancellable = true, method = "doCreateBiomes(Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;)V")
  private void doCreateBiomes(Blender b, RandomState nc, StructureManager sa, ChunkAccess c,
      CallbackInfo ci) {
    if (Worldgen.isOutside(c.getPos())) {
      ci.cancel();
    }
  }

  @Inject(at = @At("HEAD"), cancellable = true, method = "applyCarvers(Lnet/minecraft/server/level/WorldGenRegion;JLnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/biome/BiomeManager;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/GenerationStep$Carving;)V")
  public void applyCarvers(WorldGenRegion cr, long s, RandomState nc, BiomeManager ba, StructureManager sa,
      ChunkAccess c, GenerationStep.Carving cs, CallbackInfo ci) {
    if (Worldgen.isOutside(c.getPos())) {
      ci.cancel();
    }
  }

  @Inject(at = @At("HEAD"), cancellable = true, method = "buildSurface(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/WorldGenerationContext;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/biome/BiomeManager;Lnet/minecraft/core/Registry;Lnet/minecraft/world/level/levelgen/blending/Blender;)V")
  public void buildSurface(ChunkAccess c, WorldGenerationContext hc, RandomState nc, StructureManager sa,
      BiomeManager ba, Registry<Biome> br, Blender b, CallbackInfo ci) {
    if (Worldgen.isOutside(c.getPos())) {
      ci.cancel();
    }
  }

  @Inject(at = @At("HEAD"), cancellable = true, method = "spawnOriginalMobs(Lnet/minecraft/server/level/WorldGenRegion;)V")
  private void spawnOriginalMobs(WorldGenRegion r, CallbackInfo ci) {
    if (Worldgen.isOutside(r.getCenter())) {
      ci.cancel();
    }
  }

  @Inject(at = @At("HEAD"), cancellable = true, method = "doFill(Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;II)Lnet/minecraft/world/level/chunk/ChunkAccess;")
  private void doFill(Blender b, StructureManager sa, RandomState nc, ChunkAccess c, int mY, int cY,
      CallbackInfoReturnable<ChunkAccess> cir) {
    ChunkPos p = c.getPos();
    if (Worldgen.isOutside(p)) {
      int x = p.x < 0 ? p.x * 16 + 15 : p.x * 16;
      int z = p.z < 0 ? p.z * 16 + 15 : p.z * 16;
      ResourceLocation dim = ((NoiseBasedChunkGenerator) (Object) this).generatorSettings().getKey().location();

      BlockState bs = getState(dim);
      int bY = c.getMinBuildHeight();
      int tY = dim == NoiseGeneratorSettings.NETHER.location() ? c.getMaxBuildHeight() : 62;
      if ((p.x == Worldgen.CR || p.x == -Worldgen.CR - 1) && (p.z == Worldgen.CR || p.z == -Worldgen.CR - 1)) {
        for (int y = bY; y <= tY; y++) {
          if (dim == NoiseGeneratorSettings.END.location() && y % 3 == 0)
            continue;
          c.setBlockState(new BlockPos(x, y, z), bs, false);
        }
      } else if ((p.x == Worldgen.CR || p.x == -Worldgen.CR - 1) && (p.z < Worldgen.CR && p.z >= -Worldgen.CR)) {
        for (int o = bY; o < 16; o++) {
          for (int y = bY; y <= tY; y++) {
            if (dim == NoiseGeneratorSettings.END.location() && y % 3 == 0)
              continue;
            c.setBlockState(new BlockPos(x, y, z + p.z < 0 ? o : -o), bs, false);
          }
        }
      } else if ((p.z == Worldgen.CR || p.z == -Worldgen.CR - 1) && (p.x < Worldgen.CR && p.x >= -Worldgen.CR)) {
        for (int ox = bY; ox < 16; ox++) {
          for (int y = bY; y <= tY; y++) {
            if (dim == NoiseGeneratorSettings.END.location() && y % 4 == 0)
              continue;
            c.setBlockState(new BlockPos(x + p.x < 0 ? ox : -ox, y, z), bs, false);
          }
        }
      }

      cir.setReturnValue(c);
    }
  }

  private static final BlockState OVERWORLD_WALL = Blocks.BARRIER.defaultBlockState();
  private static final BlockState NETHER_WALL = Blocks.BEDROCK.defaultBlockState();
  private static final BlockState END_WALL = Blocks.GLOWSTONE.defaultBlockState();

  private static BlockState getState(ResourceLocation dim) {
    if (dim == NoiseGeneratorSettings.OVERWORLD.location()) {
      return OVERWORLD_WALL;
    } else if (dim == NoiseGeneratorSettings.NETHER.location()) {
      return NETHER_WALL;
    }
    return END_WALL;
  }
}
