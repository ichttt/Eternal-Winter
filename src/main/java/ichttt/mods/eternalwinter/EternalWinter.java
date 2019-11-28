package ichttt.mods.eternalwinter;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mod("eternalwinter")
public class EternalWinter {
    private static final Logger LOGGER = LogManager.getLogger("EternalWinter");

    public EternalWinter() {
        LOGGER.info("Starting up eternal winter mod");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EWConfig.generalSpec);
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) { //All biomes should be known by now
        LOGGER.debug("Modifying Biome stats...");


        DeferredWorkQueue.runLater(() -> {
            int i = 0;
            for (Biome b : ForgeRegistries.BIOMES) {
                LOGGER.debug("Modifying Biome {}", b);
                if (EWConfig.GENERAL.listMode.get() == ListMode.BLACKLIST) {
                    ResourceLocation registryName = Objects.requireNonNull(b.getRegistryName());
                    if (EWConfig.GENERAL.biomeList.get().stream().anyMatch(s -> s.equals(registryName.getPath()) || s.equals(registryName.toString())))
                        continue;
                } else if (EWConfig.GENERAL.listMode.get() == ListMode.WHITELIST) {
                    ResourceLocation registryName = Objects.requireNonNull(b.getRegistryName());
                    if (EWConfig.GENERAL.biomeList.get().stream().noneMatch(s -> s.equals(registryName.getPath()) || s.equals(registryName.toString())))
                        continue;
                } else {
                    throw new RuntimeException("Wrong list mode! " + EWConfig.GENERAL.biomeList.get());
                }
                List<Biome.SpawnListEntry> spawnListEntries = b.spawns.computeIfAbsent(EntityClassification.CREATURE, k -> Lists.newArrayList());
                boolean hasPolarBear = spawnListEntries.stream().map(spawnListEntry -> spawnListEntry.entityType).anyMatch(entityType -> entityType.equals(EntityType.POLAR_BEAR));
                if (!hasPolarBear) spawnListEntries.add(new Biome.SpawnListEntry(EntityType.POLAR_BEAR, 1, 1, 2));
                b.precipitation = Biome.RainType.SNOW;
                b.temperature = 0F;
                i++;
            }
            LOGGER.info("Modified " + i + " Biomes modified successful!");
        });
    }
}
