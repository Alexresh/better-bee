package ru.bees;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain implements ModInitializer {
	public static final String MOD_ID = "better-bees";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



	@Override
	public void onInitialize() {
		LOGGER.info("Beeeeee!");
	}
}