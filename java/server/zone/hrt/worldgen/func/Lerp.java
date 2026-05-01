// Copyright 2026 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package zone.hrt.worldgen.func;

import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Lerp(DensityFunction delta, DensityFunction start, DensityFunction end)
    implements DensityFunction.SimpleFunction {
  public static final KeyDispatchDataCodec<Lerp> CODEC_HOLDER = KeyDispatchDataCodec
      .of(RecordCodecBuilder.mapCodec(instance -> instance.group(
          DensityFunction.HOLDER_HELPER_CODEC.fieldOf("delta").forGetter(Lerp::delta),
          DensityFunction.HOLDER_HELPER_CODEC.fieldOf("start").forGetter(Lerp::start),
          DensityFunction.HOLDER_HELPER_CODEC.fieldOf("end").forGetter(Lerp::end))
          .apply(instance, Lerp::new)));

  @Override
  public double compute(DensityFunction.FunctionContext pos) {
    double deltaClamped = Math.clamp(this.delta.compute(pos), 0, 1);
    return Mth.lerp(deltaClamped, this.start.compute(pos), this.end.compute(pos));
  }

  @Override
  public void fillArray(double[] doubles, ContextProvider ctx) {
    ctx.fillAllDirectly(doubles, this);
  }

  @Override
  public DensityFunction mapAll(Visitor visitor) {
    return visitor.apply(new Lerp(delta.mapAll(visitor), start.mapAll(visitor), end.mapAll(visitor)));
  }

  @Override
  public double minValue() {
    return Math.min(start.minValue(), end.minValue());
  }

  @Override
  public double maxValue() {
    return Math.max(start.maxValue(), end.maxValue());
  }

  @Override
  public KeyDispatchDataCodec<? extends DensityFunction> codec() {
    return CODEC_HOLDER;
  }
}