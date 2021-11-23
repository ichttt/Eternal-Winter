package ichttt.mods.eternalwinter;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("eternalwinter")
public class EternalWinter {
    private static final Logger LOGGER = LogManager.getLogger("EternalWinter");

    public EternalWinter() {
        LOGGER.info("Starting up eternal winter mod");
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, this::onLoadComplete);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EWConfig.generalSpec);
    }

    public void onLoadComplete(BiomeLoadingEvent event) { //All biomes should be known by now
        if (EWConfig.GENERAL.listMode.get() == ListMode.BLACKLIST) {
            ResourceLocation registryName = event.getName();
            if (EWConfig.GENERAL.biomeList.get().stream().anyMatch(s -> s.equals(registryName.getPath()) || s.equals(registryName.toString())))
                return;
        } else if (EWConfig.GENERAL.listMode.get() == ListMode.WHITELIST) {
            ResourceLocation registryName = event.getName();
            if (EWConfig.GENERAL.biomeList.get().stream().noneMatch(s -> s.equals(registryName.getPath()) || s.equals(registryName.toString())))
                return;
        } else {
            throw new RuntimeException("Wrong list mode! " + EWConfig.GENERAL.biomeList.get());
        }

        float downfall = EWConfig.GENERAL.downfall.get().floatValue();
        if (downfall != -1 && downfall < 0) {
            LOGGER.error("Invalid downfall " + downfall + ", defaulting to -1!");
            downfall = -1F;
        }
        if (downfall == -1F)
            downfall = event.getClimate().downfall;
        event.setClimate(new Biome.ClimateSettings(Biome.Precipitation.SNOW, 0F, Biome.TemperatureModifier.NONE, downfall));
        LOGGER.debug("Modified Biome {} successful!", event.getName());
    }
}
