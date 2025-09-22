// Copyright 2025 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package dev.atakku.fsmp.worldgen.func;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import com.mojang.serialization.MapCodec;

public class EdgeRatioPos extends EdgeRatioAbstract {
  public static final KeyDispatchDataCodec<EdgeRatioPos> CODEC_HOLDER = KeyDispatchDataCodec.of(MapCodec.unit(EdgeRatioPos::new));

  @Override
  public double compute(DensityFunction.FunctionContext pos) {
    return compute(pos, false);
  }

  @Override
  public KeyDispatchDataCodec<? extends DensityFunction> codec() {
    return CODEC_HOLDER;
  }
}