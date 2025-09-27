// Copyright 2025 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package dev.atakku.fsmp.worldgen.func;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import com.mojang.serialization.MapCodec;

import dev.atakku.fsmp.worldgen.Worldgen;

public class RasterMap implements DensityFunction.SimpleFunction {
  public static final KeyDispatchDataCodec<RasterMap> CODEC_HOLDER = KeyDispatchDataCodec
      .of(MapCodec.unit(RasterMap::new));

  @Override
  public double compute(DensityFunction.FunctionContext pos) {
    int x = pos.blockX();
    int z = pos.blockZ();
    return (row(x - 1, z) + row(x, z) + row(x + 1, z)) / 3.0;
  }

  public static final double row(int x, int z) {
    return (smpl(x, z - 1) + smpl(x, z) + smpl(x, z + 1)) / 3.0;
  }

  public static final double smpl(int bx, int bz) {
    int x = Math.max(Math.min(bx + 8192, 16383), 0) / 4;
    int z = Math.max(Math.min(bz + 8192, 16383), 0) / 4;
    return (Worldgen.CONTINENTALNESS_MAP[x + z * 4096] & 0xFF) / 255.0d;
  }

  @Override
  public double minValue() {
    return 0.0;
  }

  @Override
  public double maxValue() {
    return 1.0;
  }

  @Override
  public KeyDispatchDataCodec<? extends DensityFunction> codec() {
    return CODEC_HOLDER;
  }
}