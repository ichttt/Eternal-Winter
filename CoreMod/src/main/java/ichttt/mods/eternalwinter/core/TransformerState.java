package ichttt.mods.eternalwinter.core;

/**
 * Created by Tobias on 23.06.2017.
 */
public enum TransformerState {

    IDLE("did not do anything yet"),
    TRANSFORMING("is/was just running"),
    TRANSFORMED("ran successfully"),
    ERROR("crashed");

    public final String description;

    TransformerState(String description) {
        this.description = "The ClassTransformer " + description;
    }
}
