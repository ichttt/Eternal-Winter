package ichttt.mods.eternalwinter.core;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.IFMLCallHook;

import java.util.Arrays;
import java.util.Map;

public class SetupHook implements IFMLCallHook {
    public static final byte[] toSend = new byte[256];

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public Void call() throws Exception {
        ClassTransformer.logger.debug("Preparing coremod...");
        Arrays.fill(toSend, (byte) (12));
        FMLCommonHandler.instance().registerCrashCallable(new CrashReportEnhancer());
        Launch.blackboard.put("EternalWinterCoreVersion", LoadingHook.COREMOD_VERSION);

        //noinspection ConstantConditions
        if (!ForgeVersion.mcVersion.startsWith("1.12") && !ForgeVersion.mcVersion.startsWith("1.11") && !ForgeVersion.mcVersion.startsWith("1.10")) {
            ClassTransformer.logger.warn("----------------------------------------------------------------------");
            ClassTransformer.logger.warn("The coremod EternalWinterCore only supports MC version 1.10 - 1.12.2 while you are running " + ForgeVersion.mcVersion + ", thing may not work!");
            ClassTransformer.logger.warn("----------------------------------------------------------------------");
        }
        return null;
    }
}
