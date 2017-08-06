package ichttt.mods.eternalwinter.core;

import net.minecraftforge.fml.common.ICrashCallable;

/**
 * Created by Tobias on 22.06.2017.
 */
public class CrashReportEnhancer implements ICrashCallable {
    @Override
    public String getLabel() {
        return "Eternal Winter Core Status";
    }

    @Override
    public String call() throws Exception {
        return "The Transformer was in state " + ClassTransformer.STATE + "  Description: " + ClassTransformer.STATE.description;
    }
}
