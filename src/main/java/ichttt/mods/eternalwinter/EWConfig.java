package ichttt.mods.eternalwinter;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class EWConfig {
    static final ForgeConfigSpec generalSpec;
    public static final EWConfig.General GENERAL;


    static {
        final Pair<General, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(EWConfig.General::new);
        generalSpec = specPair.getRight();
        GENERAL = specPair.getLeft();
    }

    public static class General {

        General(ForgeConfigSpec.Builder builder) {
            listMode = builder.comment("If BLACKLIST, the list below will be treated as a blacklist what not to touch.",
                    "If WHITELIST, the will be treated as a whitelist what biomes should only be touched").defineEnum("listMode", ListMode.BLACKLIST);

            List<String> defaultVals = Arrays.asList("river", "ocean", "deep_ocean", "warm_ocean", "lukewarm_ocean", "cold_ocean", "deep_warm_ocean", "deep_lukewarm_ocean", "deep_cold_ocean", "deep_frozen_ocean");
            biomeList = builder.comment("The blacklist or whitelist, depending on listMode").defineList("biomeList", defaultVals, o -> true);

            downfall = builder.comment("The downfall value each biome should get. If -1, EternalWinter will keep the original downfall of the biome, otherwise it takes a value between 0 and 1").defineInRange("downfall", -1D, -1D, 1D);
        }

        public final ForgeConfigSpec.EnumValue<ListMode> listMode;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> biomeList;
        public final ForgeConfigSpec.DoubleValue downfall;
    }
}
