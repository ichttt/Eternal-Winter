package ichttt.mods.eternalwinter;

import ichttt.mods.eternalwinter.core.ClassTransformer;
import ichttt.mods.eternalwinter.core.LoadingHook;
import ichttt.mods.eternalwinter.core.TransformerState;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraftforge.fml.common.CertificateHelper;

import java.util.List;

public class CoreModHandler {
    public static void preloadPacket() {
        if (ClassTransformer.STATE == TransformerState.IDLE) {
            EternalWinter.logger.debug("Preloading Packet");
            new SPacketChunkData();
            if (ClassTransformer.STATE == TransformerState.TRANSFORMED)
                EternalWinter.logger.debug("Preloading seems to be successful!");
            else
                EternalWinter.logger.error("Class Transformer is in state "+ ClassTransformer.STATE + ". Things may not work!");
        } else
            EternalWinter.logger.info("Skipping Packet preload as class seems to be loaded already");
    }

    public static void verifyFingerprint() {
        List<String> certs = CertificateHelper.getFingerprints(LoadingHook.class.getProtectionDomain().getCodeSource().getCertificates());
        if (!certs.contains("7904c4e13947c8a616c5f39b26bdeba796500722"))
            EternalWinter.logger.warn("Invalid fingerprint for coremod!");
    }
}
