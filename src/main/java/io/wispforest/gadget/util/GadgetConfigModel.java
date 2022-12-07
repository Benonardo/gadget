package io.wispforest.gadget.util;

import io.wispforest.gadget.decompile.QuiltflowerVersions;
import io.wispforest.gadget.mappings.*;
import io.wispforest.owo.config.annotation.*;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Config(name = "gadget", wrapperName = "GadgetConfig")
public class GadgetConfigModel {
    public boolean menuButtonEnabled = true;
    public boolean rightClickDump = true;
    public boolean dropChunkData = false;
    public boolean debugKeysInScreens = true;
    public boolean matrixStackDebugging = true;
    public boolean uiInspector = true;
    @RestartRequired public boolean silenceStartupErrors = true;
    @PredicateConstraint("isQuiltflowerVersionValid") public String quiltflowerVersion = "LATEST";
    @Hook public MappingsType mappings = FabricLoader.getInstance().isDevelopmentEnvironment() ? MappingsType.LOCAL : MappingsType.YARN;
    public UICounterMode uiCounterMode = UICounterMode.LOG_ON_LONG_UPDATE;
    @Hook public List<String> hiddenFields = new ArrayList<>(List.of(
        "java.lang.Enum#name",

        "java.util.ArrayList#elementData",
        "java.util.ArrayList#size",
        "java.util.AbstractList#modCount",

        "java.util.AbstractMap#keySet",
        "java.util.AbstractMap#values",
        "java.util.EnumMap#entrySet",
        "java.util.EnumMap#size",
        "java.util.EnumMap#vals",
        "java.util.HashMap#table",
        "java.util.HashMap#size",
        "java.util.HashMap#modCount",
        "java.util.HashMap#loadFactor",
        "java.util.HashMap#threshold",
        "java.util.HashMap#entrySet",
        "java.util.HashMap$Node#hash",
        "java.util.HashMap$Node#next",
        "java.util.LinkedHashMap#accessOrder",
        "java.util.LinkedHashMap#head",
        "java.util.LinkedHashMap#tail",
        "java.util.LinkedHashMap$Entry#before",
        "java.util.LinkedHashMap$Entry#after",
        "java.util.IdentityHashMap#table",
        "java.util.IdentityHashMap#size",
        "java.util.IdentityHashMap#modCount",
        "java.util.IdentityHashMap#entrySet",
        "java.util.HashSet#map",
        "java.util.RegularEnumSet#elements",
        "java.util.EnumSet#elementType",
        "java.util.EnumSet#universe",

        "net.minecraft.class_2586#field_11866",
        "net.minecraft.class_2586#field_11865",
        "net.minecraft.class_2586#field_11864",

        "net.minecraft.class_1297#field_5961"
    ));
    @Nest public InternalSettings internalSettings = new InternalSettings();

    public static boolean isQuiltflowerVersionValid(String version) {
        return version.equals("LATEST") || QuiltflowerVersions.versions().contains(version);
    }

    public static class InternalSettings {
        public boolean debugMatrixStackDebugging = false;
        public boolean injectMatrixStackErrors = false;
        public boolean inspectClasses = false;
    }

    public enum MappingsType {
        // TODO: finish everything™
        LOCAL(() -> LocalMappings.INSTANCE),
        YARN(YarnMappings::new),
        MOJANG(MojangMappings::new),
        QUILT(QuiltMappings::new);

        private final Supplier<Mappings> factory;

        MappingsType(Supplier<Mappings> factory) {
            this.factory = factory;
        }

        public Supplier<Mappings> factory() {
            return factory;
        }
    }

    public enum UICounterMode {
        OFF,
        LOG_ON_LONG_UPDATE,
        LOG_ALWAYS
    }
}
