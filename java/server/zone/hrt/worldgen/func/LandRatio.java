// Copyright 2026 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package zone.hrt.worldgen.func;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import com.mojang.serialization.codecs.RecordCodecBuilder;

public record LandRatio(DensityFunction start) implements DensityFunction.SimpleFunction {
  public static final KeyDispatchDataCodec<LandRatio> CODEC_HOLDER = KeyDispatchDataCodec
      .of(RecordCodecBuilder.mapCodec(instance -> instance.group(
          DensityFunction.HOLDER_HELPER_CODEC.fieldOf("start").forGetter(LandRatio::start))
          .apply(instance, LandRatio::new)));

  @Override
  public double compute(DensityFunction.FunctionContext pos) {
    double start = this.start.compute(pos);

    double distX = Math.min(Math.abs(pos.blockX()), start);
    double distZ = Math.min(Math.abs(pos.blockZ()), start);
    return (Math.max(distX, distZ)) / start;
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