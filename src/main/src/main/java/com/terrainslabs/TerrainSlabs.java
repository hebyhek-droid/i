package com.terrainslabs;

import net.fabricmc.api.ModInitializer;

public class TerrainSlabs implements ModInitializer {
	public static final String MOD_ID = "terrain_slabs";

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModWorldgen.initialize();
	}
}
