package ichttt.mods.eternalwinter;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("eternalwinter")
public class EternalWinter {
    public static final Logger LOGGER = LogManager.getLogger("EternalWinter");

    private static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "eternalwinter");
    public static final RegistryObject<Codec<BiomesModifier>> BIOME_MODIFIER_CODEC = BIOME_MODIFIER_SERIALIZERS.register("change_to_snow", () -> Codec.unit(BiomesModifier::new));

    public EternalWinter() {
        LOGGER.info("Starting up eternal winter mod");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EWConfig.generalSpec);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BIOME_MODIFIER_SERIALIZERS.register(modEventBus);

        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (a, b) -> true));
    }
}
