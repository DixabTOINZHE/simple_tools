package com.simple_tools.components;

import com.mojang.serialization.Codec;
import com.simple_tools.SimpleTools;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class stComponents {

    public static void run(){
        SimpleTools.LOGGER.info("com.simple_tools.components.Components are loaded successfully!");
    }

    //史莱姆放置机直径
    public static final ComponentType<Integer> SLIME_BLOCK_PLACER_PLACING_DIAMETER = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SimpleTools.MOD_ID, "slime_block_placer_placing_diameter"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    //置块器直径
    public static final ComponentType<Integer> BLOCK_PLACER_PLACING_DIAMETER = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SimpleTools.MOD_ID, "block_placer_placing_diameter"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    //置块器模式
    public static final ComponentType<Integer> BLOCK_PLACER_PLACING_MODE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SimpleTools.MOD_ID, "block_placer_placing_mode"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    //寻块仪直径
    public static final ComponentType<Integer> BLOCK_SEARCHER_SEARCHING_DIAMETER = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SimpleTools.MOD_ID, "block_searcher_seaching_diameter"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );
}
