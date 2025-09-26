// Copyright 2025 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package dev.atakku.fsmp.worldgen.func;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

import com.mojang.serialization.MapCodec;

import dev.atakku.fsmp.worldgen.Worldgen;

public class ContinentalnessMap implements DensityFunction.SimpleFunction {
  public static final KeyDispatchDataCodec<ContinentalnessMap> CODEC_HOLDER = KeyDispatchDataCodec
      .of(MapCodec.unit(ContinentalnessMap::new));

  @Override
  public double compute(DensityFunction.FunctionContext pos) {
    int x = Math.max(Math.min(pos.blockX() + 8192, 16383), 0);
    int y = Math.max(Math.min(pos.blockZ() + 8192, 16383), 0);

    return Mth.lerp2(x%4/4.0, y%4/4.0, p(x, y), p(x+1, y), p(x, y+1), p(x, y+1));
  }

  public static final double p(int x, int z) {
    int bx = Math.max(0, Math.min(x, 4095));
    int bz = Math.max(0, Math.min(z, 4095));
    return (Worldgen.CONTINENTALNESS_MAP[bx + bz * 4096] & 0xFF) / 255.0d * 2.0d - 1.0d;
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