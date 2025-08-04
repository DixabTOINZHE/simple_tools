package com.simple_tools;

import com.simple_tools.components.stComponents;
import com.simple_tools.tags.stBlockTags;
import com.simple_tools.tools.stItemGroup;
import com.simple_tools.tools.items.stTools;
import com.simple_tools.tools.values.stValues;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleTools implements ModInitializer {
	public static final String MOD_ID = "simple_tools";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		stValues.run();
		stTools.run();
		stItemGroup.run();
		stBlockTags.run();
		stComponents.run();
		LOGGER.info("Hello Fabric world!");
	}
}