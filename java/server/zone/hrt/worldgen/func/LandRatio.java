// Copyright 2026 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package zone.hrt.worldgen.func;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import com.mojang.serialization.MapCodec;

import zone.hrt.worldgen.Worldgen;

public class LandRatio implements DensityFunction.SimpleFunction {
  public static final KeyDispatchDataCodec<LandRatio> CODEC_HOLDER = KeyDispatchDataCodec.of(MapCodec.unit(LandRatio::new));

  @Override
  public double compute(DensityFunction.FunctionContext pos) {
    int distX = Math.min(Math.abs(pos.blockX()), Worldgen.START);
    int distZ = Math.min(Math.abs(pos.blockZ()), Worldgen.START);
    return ((double) Math.max(distX, distZ)) / (double) Worldgen.START;
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