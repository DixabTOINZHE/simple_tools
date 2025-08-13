package com.simple_tools.tools;

import com.simple_tools.Run;
import com.simple_tools.SimpleTools;
import com.simple_tools.tools.items.stTools;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class stItemGroup implements Run {
    public static void run(){
        SimpleTools.LOGGER.info("com.simple_tools_tools_SimpleToolsGroup are loaded successfully!");
    }
    public static final ItemGroup ST_GROUP = Registry.register(Registries.ITEM_GROUP,Identifier.of(SimpleTools.MOD_ID,"simple_tools_group"),
        ItemGroup.create(null,-1).displayName(Text.translatable("simple_tools_group.simple_tools.name"))
        .icon(() -> new ItemStack(stTools.BLOCK_SEARCHER))
        .entries((displayContext,entries) -> {
            entries.add(stTools.BLOCK_SEARCHER);
            entries.add(stTools.NETHER_PORTAL_FRAME_GENERATOR);
            entries.add(stTools.BLOCK_PLACER);
            entries.add(stTools.SLIME_BLOCK_PLACER);
            entries.add(stTools.PLAYER_STEALER);
        }).build());
}
