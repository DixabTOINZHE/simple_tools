package com.simple_tools.tools.items;

import com.simple_tools.SimpleTools;
import com.simple_tools.components.stComponents;
import com.simple_tools.tools.logics.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class stTools {
    public static void run() {
        SimpleTools.LOGGER.info("com.simple_tolls.tools.items.Tools are loaded successfully!");
    }

    protected static Item item_register(String id, Item item) {
        return (Item) Registry.register(Registries.ITEM, Identifier.of(SimpleTools.MOD_ID, id), item);
    }

    public static final Item BLOCK_SEARCHER = item_register("block_searcher", new stBlockSearcher(new Item.Settings().component(stComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER, 3).maxDamage(256)));
    public static final Item NETHER_PORTAL_FRAME_GENERATOR = item_register("nether_portal_frame_generator", new stNetherPortalFrameGenerator((new Item.Settings().maxDamage(1024))));
    public static final Item BLOCK_PLACER = item_register("block_placer", new stBlockPlacer((new Item.Settings().component(stComponents.BLOCK_PLACER_PLACING_DIAMETER, 3).component(stComponents.BLOCK_PLACER_PLACING_MODE, 1).maxDamage(512))));
    public static final Item SLIME_BLOCK_PLACER = item_register("slime_block_placer", new stSlimeBlockPlacer((new Item.Settings()).component(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER, 1).maxDamage(128)));
    public static final Item PLAYER_STEALER = item_register("player_stealer", new stPlayerStealer((new Item.Settings()).maxDamage(256)));
}
