package com.terrainslabs.client;

import java.util.List;

import com.terrainslabs.ModBlocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class TerrainSlabsClient implements ClientModInitializer {
	// Фиксированный зелёный цвет травы. Потом заменим на цвет биома.
	private static final int GRASS_GREEN = 0x79C05A;

	@Override
	public void onInitializeClient() {
		BlockColorRegistry.register(List.of(new BlockTintSource() {
			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return ARGB.opaque(GRASS_GREEN);
			}

			@Override
			public int color(BlockState state) {
				return ARGB.opaque(GRASS_GREEN);
			}
		}), ModBlocks.GRASS_SLAB);
	}
}
