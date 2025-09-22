// Copyright 2025 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package dev.atakku.fsmp.worldgen.func;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import com.mojang.serialization.MapCodec;

public class EdgeRatioNeg extends EdgeRatioAbstract {
  public static final KeyDispatchDataCodec<EdgeRatioNeg> CODEC_HOLDER = KeyDispatchDataCodec.of(MapCodec.unit(EdgeRatioNeg::new));

  @Override
  public double compute(DensityFunction.FunctionContext pos) {
    return compute(pos, true);
  }

  @Override
  public KeyDispatchDataCodec<? extends DensityFunction> codec() {
    return CODEC_HOLDER;
  }
}