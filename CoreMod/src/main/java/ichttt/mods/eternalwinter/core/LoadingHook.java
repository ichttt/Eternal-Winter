package ichttt.mods.eternalwinter.core;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created by Tobias on 10.06.2017.
 */
@IFMLLoadingPlugin.SortingIndex(1015)
@IFMLLoadingPlugin.TransformerExclusions({"ichttt.mods"})
@IFMLLoadingPlugin.Name("Eternal Winter Core")
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
public class LoadingHook implements IFMLLoadingPlugin {
    public static final String COREMOD_VERSION = "1.0.0";

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"ichttt.mods.eternalwinter.core.ClassTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null; //Gets found via @Mod annotation
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return SetupHook.class.getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
