// Copyright 2026 Atakku <https://atakku.dev>
//
// This project is dual licensed under MIT and Apache.

package zone.hrt.worldgen;

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

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zone.hrt.worldgen.func.*;

@Mod(Worldgen.MOD_ID)
public class Worldgen {
  public static final String MOD_ID = "hrt_worldgen";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  public static final int SIZE = 1536;
  public static final int END = 10240;
  public static final int START = END - SIZE;
  public static final int EDGE = START - SIZE;
  public static final int CR = END / 16;

  public Worldgen(IEventBus bus) {
    bus.addListener(this::registerDensityFunctionTypes);
    bus.addListener(this::registerEnabledPacks);
  }

  private void registerDensityFunctionTypes(final RegisterEvent event) {
    event.register(Registries.DENSITY_FUNCTION_TYPE, helper -> {
        helper.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "edge_ratio"), EdgeRatio.CODEC_HOLDER.codec());
        helper.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "land_ratio"), LandRatio.CODEC_HOLDER.codec());
        helper.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "lerp"), Lerp.CODEC_HOLDER.codec());
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
                Component.literal("_hrt-worldgen"),
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
