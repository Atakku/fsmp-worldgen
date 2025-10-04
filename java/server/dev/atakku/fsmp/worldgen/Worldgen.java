// Copyright 2025 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package dev.atakku.fsmp.worldgen;

//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.ChunkPos;

//import javax.imageio.ImageIO;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.atakku.fsmp.worldgen.func.*;

@Mod(Worldgen.MOD_ID)
public class Worldgen {
  public static final String MOD_ID = "fsmp_worldgen";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
  
  public static final int SIZE = 1536;
  public static final int END = 10240;
  public static final int START = END - SIZE;
  public static final int EDGE = START - SIZE;
  public static final int CR = END / 16;

  //public static byte[] BLEND_MAP = new byte[4096*4096]; // 0 - regular - 255 custom
  //public static byte[] TEMPERATURE_MAP = new byte[4096*4096]; // 0 - cold - 255 hot
  //public static byte[] VEGETATION_MAP = new byte[4096*4096]; // 0 - no trees - 255 many trees
  public static byte[] CONTINENTALNESS_MAP = new byte[4096*4096]; // 0 - ocean - 255 inland

  public Worldgen(IEventBus bus) {
    //try {
    //  BufferedImage img = ImageIO.read(new File("map.png"));
    //  for (int i = 0; i < 4096 * 4096; i++) {
    //    int pixel = img.getRGB(i%4096, i/4096);
    //    //Worldgen.BLEND_MAP[i] = (byte) ((pixel & 0xff000000) >> 24);
    //    //Worldgen.TEMPERATURE_MAP[i] = (byte) ((pixel & 0x00ff0000) >> 16);
    //    //Worldgen.VEGETATION_MAP[i] = (byte) ((pixel & 0x0000ff00) >> 8);
    //    Worldgen.CONTINENTALNESS_MAP[i] = (byte) (pixel & 0x000000ff);
    //  }
    //  LOGGER.info("Loaded sample map");
    //} catch (IOException e) {
    //  e.printStackTrace();
    //}

    // Register the commonSetup method for modloading
    bus.addListener(this::registerDensityFunctionTypes);
    bus.addListener(this::registerEnabledPacks);
  }

  private void registerDensityFunctionTypes(final RegisterEvent event) {
    event.register(Registries.DENSITY_FUNCTION_TYPE, helper -> {
        helper.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "edge_ratio"), EdgeRatio.CODEC_HOLDER.codec());
        helper.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "land_ratio"), LandRatio.CODEC_HOLDER.codec());
        helper.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "lerp"), Lerp.CODEC_HOLDER.codec());
        //helper.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "raster_map"), RasterMap.CODEC_HOLDER.codec()); // Unused for now
        helper.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "x_add_z"), XAddZ.CODEC_HOLDER.codec());
        helper.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "x_sub_z"), XSubZ.CODEC_HOLDER.codec());
    });
  }

  // Taken from https://github.com/Apollounknowndev/tectonic/blob/v3/src/main/java/dev/worldgen/tectonic/loaders/neoforge/TectonicNeoforge.java
  private void registerEnabledPacks(final AddPackFindersEvent event) {
    if (event.getPackType() == PackType.SERVER_DATA) {
        Path resourcePath = ModList.get().getModFileById(Worldgen.MOD_ID).getFile().findResource("patch");

        Pack dataPack = Pack.readMetaAndCreate(
            new PackLocationInfo(
                resourcePath.getFileName().toString(),
                Component.literal("_fsmp-worldgen"),
                PackSource.BUILT_IN,
                Optional.empty()
            ),
            new PathPackResources.PathResourcesSupplier(resourcePath),
            PackType.SERVER_DATA,
            new PackSelectionConfig(
                true,
                Pack.Position.TOP,
                false
            )
        );
        event.addRepositorySource((packConsumer) -> packConsumer.accept(dataPack));
    }
  }

  public static boolean isOutside(ChunkPos p) {
    return p.x >= CR || p.z >= CR || p.x < -CR || p.z < -CR;
  }
}
