package ichttt.mods.eternalwinter;

import net.minecraftforge.common.config.Config;

@Config(modid = EternalWinter.MOD_ID, name = "Eternal Winter")
public class EternalConfig {

    @Config.Comment("True: Set is Blacklist, False: Set is Whitelist")
    @Config.RequiresMcRestart
    public static boolean blackListMode = true;

    @Config.Comment("A set of biomes that should be treated different then the rest." +
            "\nIf \"blackListMode\" is true, this is a blacklist, otherwise it's a whitelist")
    public static String[] biomeList = new String[] {"river", "ocean", "deep_ocean"};

    @Config.Comment("Set this to true if polar bears and stray should spawn in all biomes")
    @Config.RequiresMcRestart
    public static boolean copySnowEntities = true;
}
