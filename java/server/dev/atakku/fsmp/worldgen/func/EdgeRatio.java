// Copyright 2025 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package dev.atakku.fsmp.worldgen.func;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import com.mojang.serialization.MapCodec;

import dev.atakku.fsmp.worldgen.Worldgen;

public class EdgeRatio implements DensityFunction.SimpleFunction {
  public static final KeyDispatchDataCodec<EdgeRatio> CODEC_HOLDER = KeyDispatchDataCodec
      .of(MapCodec.unit(EdgeRatio::new));

  public double compute(DensityFunction.FunctionContext pos) {
    int distX = Math.min(Math.abs(pos.blockX()), Worldgen.END);
    int distZ = Math.min(Math.abs(pos.blockZ()), Worldgen.END);

    int edgeX = distX - Worldgen.EDGE;
    int edgeZ = distZ - Worldgen.EDGE;
    if (edgeX > 0 && edgeZ > 0) {
      double dist = Math.sqrt(edgeX * edgeX + edgeZ * edgeZ) - Worldgen.SIZE;
      if (dist < 0)
        return 0;
      if (dist > Worldgen.SIZE)
        return 1;
      return (dist / (Worldgen.SIZE));
    }

    int point = Math.max(distX, distZ) - Worldgen.START;
    return ((double) Math.max(0, point) / (double) Worldgen.SIZE);
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