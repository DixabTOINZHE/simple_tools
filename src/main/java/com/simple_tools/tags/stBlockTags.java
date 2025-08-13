package com.simple_tools.tags;

import com.simple_tools.Run;
import com.simple_tools.SimpleTools;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class stBlockTags implements Run {

    public static final TagKey<Block> PLACEABLE_BLOCKS = of("placeable_blocks");

    private static TagKey<Block> of(String id) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(SimpleTools.MOD_ID, id));
    }

    public static void run(){
        SimpleTools.LOGGER.info("com.simple_tools.tags.BlockTags are loaded successfully.");
    }
}
