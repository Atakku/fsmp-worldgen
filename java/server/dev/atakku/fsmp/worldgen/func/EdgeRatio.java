// Copyright 2025 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package dev.atakku.fsmp.worldgen.func;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import com.mojang.serialization.MapCodec;

public class EdgeRatio implements DensityFunction.SimpleFunction {
  public static final KeyDispatchDataCodec<EdgeRatio> CODEC_HOLDER = KeyDispatchDataCodec.of(MapCodec.unit(EdgeRatio::new));

  private static final int SIZE = 256;
  private static final int END = 8192;
  private static final int START = END - SIZE;

  public double compute(DensityFunction.FunctionContext pos) {
    int absX = Math.min(Math.abs(pos.blockX()), END);
    int absZ = Math.min(Math.abs(pos.blockZ()), END);
    if (absX < START && absZ < START) {
      return 0;
    }
    int point = Math.max(absX, absZ) - START;
    return ((double) point / (double) SIZE);
  }

  @Override
  public double minValue() {
    return 0;
  }

  @Override
  public double maxValue() {
    return 1;
  }

  @Override
  public KeyDispatchDataCodec<? extends DensityFunction> codec() {
    return CODEC_HOLDER;
  }
}