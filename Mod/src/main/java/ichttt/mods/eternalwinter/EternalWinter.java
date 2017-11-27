package ichttt.mods.eternalwinter;

import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.biome.BiomeRiver;
import net.minecraft.world.biome.BiomeSnow;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

@Mod(modid = EternalWinter.MOD_ID,
        name = "Eternal Winter",
        version = "1.0.2",
        certificateFingerprint = "7904c4e13947c8a616c5f39b26bdeba796500722",
        acceptableRemoteVersions = "*",
        acceptedMinecraftVersions = "[1.10, 1.13)")
public class EternalWinter {
    public static final String MOD_ID = "eternalwinter";

    public final static Logger logger = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (Launch.blackboard.containsKey("EternalWinterCoreVersion")) {
            CoreModHandler.verifyFingerprint();
        }
        logger.info("Starting up eternal winter mod");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.debug("Modifying Biome stats...");
        int i = 0;
        boolean hasStray;

        try {
            Class.forName("net.minecraft.entity.monster.EntityStray"); // Are we in a version with strays?
            hasStray = true; // Yes!
        } catch (ClassNotFoundException e) {
            logger.info("Could not find EntityStray - not enabling strays"); // Nope!
            hasStray = false;
        }

        for (Biome b : Biome.REGISTRY) {
            logger.debug("Modifying Biome " + b);
            if (b instanceof BiomeRiver && !EternalConfig.freezeRivers)
                continue;
            if (b instanceof BiomeOcean && !EternalConfig.freezeOcean)
                continue;
            if (EternalConfig.copySnowEntities && !(b instanceof BiomeSnow)) {
                b.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPolarBear.class, 1, 1, 2));
                if (hasStray)
                    b.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityStray.class, 40, 2, 4));
            }
            b.enableSnow = true;
            b.enableRain = false;
            b.temperature = 0F;
            i++;
        }
        logger.info("Modified " + i + " Biomes modified successful!");
        if (Launch.blackboard.containsKey("EternalWinterCoreVersion")) {
            String coreModVersion = (String) Launch.blackboard.get("EternalWinterCoreVersion");
            if (coreModVersion.equals("1.0.0"))
                logger.warn("Outdated coremod version found! CoreMod version is " + coreModVersion + " while this mod already knows that version 1.0.1 exists!");
            else if (!coreModVersion.equals("1.0.2"))
                logger.warn("Unknown coremod version " + coreModVersion + ", please update this mod!");
            CoreModHandler.preloadPacket();
        } else
            logger.info("Missing core mod, client will have rain if the mod is missing!");
    }

    @Mod.EventHandler
    public void wrongFingerprint(FMLFingerprintViolationEvent event) {
        logger.error("Got invalid fingerprint - THIS IS NOT A SIGNED COPY OF THIS MOD!!");
        logger.error("Expected " + event.getExpectedFingerprint());
        logger.error("Found fingerprints:");
        Collection<String> fingerprints = event.getFingerprints();
        if (fingerprints.isEmpty()) {
            logger.error("NO FINGERPRINT PRESENT ");
        } else {
            for (String s : fingerprints)
                logger.error(s);
        }
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent event) {
        if (event.getModID().equals(MOD_ID))
            ConfigManager.load(MOD_ID, Config.Type.INSTANCE);
    }
}
