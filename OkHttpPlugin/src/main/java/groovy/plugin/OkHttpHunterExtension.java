package groovy.plugin;


import groovy.hunter.RunVariant;

public class OkHttpHunterExtension {

    public RunVariant runVariant = RunVariant.ALWAYS;
    public boolean weaveEventListener = true;
    public boolean duplcatedClassSafeMode = false;

    @Override
    public String toString() {
        return "OkHttpHunterExtension{" +
                "runVariant=" + runVariant +
                ", weaveEventListener=" + weaveEventListener +
                ", duplcatedClassSafeMode=" + duplcatedClassSafeMode +
                '}';
    }

}
