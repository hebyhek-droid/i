package com.terrainslabs.client;

import java.util.List;

import com.terrainslabs.ModBlocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;

public class TerrainSlabsClient implements ClientModInitializer {
	// Цвет травы вне мира (например, в инвентаре) - как у ванильного блока травы.
	private static final int DEFAULT_GRASS = 0x7CBD6B;

	@Override
	public void onInitializeClient() {
		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return ARGB.opaque(BiomeColors.getAverageGrassColor(level, pos));
			}

			@Override
			public int color(BlockState state) {
				return ARGB.opaque(DEFAULT_GRASS);
			}
		}), ModBlocks.GRASS_SLAB);
	}
}
