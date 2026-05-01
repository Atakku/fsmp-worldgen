// Copyright 2026 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package zone.hrt.worldgen.func;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import com.mojang.serialization.MapCodec;

public class XAddZ implements DensityFunction.SimpleFunction {
  public static final KeyDispatchDataCodec<XAddZ> CODEC_HOLDER = KeyDispatchDataCodec.of(MapCodec.unit(XAddZ::new));

  @Override
  public double compute(DensityFunction.FunctionContext pos) {
    return pos.blockX() + pos.blockZ();
  }

  @Override
  public double minValue() {
    return -Double.MAX_VALUE;
  }

  @Override
  public double maxValue() {
    return Double.MAX_VALUE;
  }

  @Override
  public KeyDispatchDataCodec<? extends DensityFunction> codec() {
    return CODEC_HOLDER;
  }
}