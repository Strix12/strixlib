package com.strix12.strixlib;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrixLib implements ModInitializer {
	public static final String MODID = "strixlib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		LOGGER.info("StrixLib initialized.");
	}
}
