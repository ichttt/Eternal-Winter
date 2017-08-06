package ichttt.mods.eternalwinter;

import net.minecraftforge.common.config.Config;

@Config(modid = EternalWinter.MOD_ID, name = "Eternal Winter")
public class EternalConfig {

    @Config.Comment("Soll der Ozean einfrieren?")
    @Config.RequiresMcRestart
    public static boolean freezeOcean = false;

    @Config.Comment("Sollen Flüsse einfrieren?")
    @Config.RequiresMcRestart
    public static boolean freezeRivers = false;

    @Config.Comment("Sollen Schnee Entities spawnen?")
    @Config.RequiresMcRestart
    public static boolean copySnowEntities = true;
}
