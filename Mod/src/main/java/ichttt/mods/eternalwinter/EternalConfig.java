package ichttt.mods.eternalwinter;

import net.minecraftforge.common.config.Config;

@Config(modid = EternalWinter.MOD_ID, name = "Eternal Winter")
public class EternalConfig {

    @Config.Comment("Set this to true if the ocean should freeze")
    @Config.RequiresMcRestart
    public static boolean freezeOcean = false;

    @Config.Comment("Set this to true if the rivers should freeze")
    @Config.RequiresMcRestart
    public static boolean freezeRivers = false;

    @Config.Comment("Set this to true if polar bears and stray should spawn in all biomes")
    @Config.RequiresMcRestart
    public static boolean copySnowEntities = true;
}
