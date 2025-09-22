// Copyright 2025 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package dev.atakku.fsmp.worldgen.func;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import com.mojang.serialization.MapCodec;

import dev.atakku.fsmp.worldgen.Worldgen;

public class ContinentalnessMap implements DensityFunction.SimpleFunction {
  public static final KeyDispatchDataCodec<ContinentalnessMap> CODEC_HOLDER = KeyDispatchDataCodec
      .of(MapCodec.unit(ContinentalnessMap::new));

  @Override
  public double compute(DensityFunction.FunctionContext pos) {
    int x = pos.blockX();
    int z = pos.blockZ();
    return (compute(x - 1, z - 1) +
        compute(x - 1, z) +
        compute(x - 1, z + 1) +
        compute(x, z - 1) +
        compute(x, z) +
        compute(x, z + 1) +
        compute(x + 1, z - 1) +
        compute(x + 1, z) +
        compute(x + 1, z + 1)) / 9.0;
  }

  public static final double compute(int bx, int bz) {
    int x = Math.max(Math.min(bx + 8192, 16383), 0) / 4;
    int z = Math.max(Math.min(bz + 8192, 16383), 0) / 4;
    double amplitude = (Worldgen.CONTINENTALNESS_MAP[x + z * 4096] & 0xFF) / 255.0d;
    return amplitude * 2.0d - 1.0d;
  }

  @Override
  public double minValue() {
    return -1.0;
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