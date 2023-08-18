package ichttt.mods.eternalwinter;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class BiomesModifier implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.MODIFY) {
            ResourceLocation registryName = biome.unwrapKey().get().location();
            if (EWConfig.GENERAL.listMode.get() == ListMode.BLACKLIST) {
                if (EWConfig.GENERAL.biomeList.get().stream().anyMatch(s -> s.equals(registryName.getPath()) || s.equals(registryName.toString())))
                    return;
            } else if (EWConfig.GENERAL.listMode.get() == ListMode.WHITELIST) {
                if (EWConfig.GENERAL.biomeList.get().stream().noneMatch(s -> s.equals(registryName.getPath()) || s.equals(registryName.toString())))
                    return;
            } else {
                throw new RuntimeException("Wrong list mode! " + EWConfig.GENERAL.biomeList.get());
            }

            float downfall = EWConfig.GENERAL.downfall.get().floatValue();
            if (downfall != -1 && downfall < 0) {
                EternalWinter.LOGGER.error("Invalid downfall " + downfall + ", defaulting to -1!");
                downfall = -1F;
            }
            if (downfall != -1F)
                builder.getClimateSettings().setDownfall(downfall);
            builder.getClimateSettings().setTemperature(0F);
            builder.getClimateSettings().setTemperatureModifier(Biome.TemperatureModifier.NONE);
            builder.getClimateSettings().setHasPrecipitation(true);
            EternalWinter.LOGGER.debug("Modified Biome {} successful!", registryName);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return EternalWinter.BIOME_MODIFIER_CODEC.get();
    }
}
