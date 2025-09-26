// Copyright 2025 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package dev.atakku.fsmp.worldgen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import javax.imageio.ImageIO;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.atakku.fsmp.worldgen.func.*;

@Mod(Worldgen.MOD_ID)
public class Worldgen {
  public static final String MOD_ID = "fsmp-worldgen";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  //public static byte[] BLEND_MAP = new byte[4096*4096]; // 0 - regular - 255 custom
  //public static byte[] TEMPERATURE_MAP = new byte[4096*4096]; // 0 - cold - 255 hot
  //public static byte[] VEGETATION_MAP = new byte[4096*4096]; // 0 - no trees - 255 many trees
  public static byte[] CONTINENTALNESS_MAP = new byte[4096*4096]; // 0 - ocean - 255 inland

  public Worldgen(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
  }
  
  private void commonSetup(FMLCommonSetupEvent event) {
    LOGGER.info("Initializing FSMP Worldgen");

    try {
      BufferedImage img = ImageIO.read(new File("map.png"));
      for (int i = 0; i < 4096 * 4096; i++) {
        int pixel = img.getRGB(i%4096, i/4096);
        //Worldgen.BLEND_MAP[i] = (byte) ((pixel & 0xff000000) >> 24);
        //Worldgen.TEMPERATURE_MAP[i] = (byte) ((pixel & 0x00ff0000) >> 16);
        //Worldgen.VEGETATION_MAP[i] = (byte) ((pixel & 0x0000ff00) >> 8);
        Worldgen.CONTINENTALNESS_MAP[i] = (byte) (pixel & 0x000000ff);
      }
      LOGGER.info("Loaded sample map");
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    reg("continentalness_map", ContinentalnessMap.CODEC_HOLDER);
    reg("x_add_z", XAddZ.CODEC_HOLDER);
    reg("x_sub_z", XSubZ.CODEC_HOLDER);

    reg("edge_ratio_neg", EdgeRatioNeg.CODEC_HOLDER);
    reg("edge_ratio_pos", EdgeRatioPos.CODEC_HOLDER);
  }

  private void reg(String name, KeyDispatchDataCodec<? extends DensityFunction> c) {
    Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, name), c.codec());
  }
}
