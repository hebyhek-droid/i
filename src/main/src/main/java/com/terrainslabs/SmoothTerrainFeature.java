package com.terrainslabs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Сглаживает пологие места: если низкая площадка граничит со ступенькой высотой
 * ровно в 1 блок, а вокруг нет перепадов больше 1, сверху ставится нижний слэб.
 * Крутые склоны и горы не трогаем.
 */
public class SmoothTerrainFeature extends Feature<NoneFeatureConfiguration> {
	public SmoothTerrainFeature() {
		super(NoneFeatureConfiguration.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		int x0 = context.origin().getX();
		int z0 = context.origin().getZ();

		// Высоты поверхности для чанка и одного блока вокруг него (18x18).
		int[][] h = new int[18][18];
		for (int dx = -1; dx <= 16; dx++) {
			for (int dz = -1; dz <= 16; dz++) {
				h[dx + 1][dz + 1] = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x0 + dx, z0 + dz) - 1;
			}
		}

		boolean placedAny = false;
		for (int cx = 0; cx < 16; cx++) {
			for (int cz = 0; cz < 16; cz++) {
				int i = cx + 1;
				int j = cz + 1;
				int y = h[i][j];

				if (!isGentleStep(h, i, j, y)) {
					continue;
				}

				BlockPos top = new BlockPos(x0 + cx, y, z0 + cz);
				BlockState topState = level.getBlockState(top);

				BlockState slab;
				BlockState newTop = null;
				if (topState.is(Blocks.GRASS_BLOCK)) {
					slab = ModBlocks.GRASS_SLAB.defaultBlockState();
					newTop = Blocks.DIRT.defaultBlockState(); // под травяным слэбом - земля
				} else if (topState.is(Blocks.DIRT) || topState.is(Blocks.COARSE_DIRT)) {
					slab = ModBlocks.DIRT_SLAB.defaultBlockState();
				} else if (topState.is(Blocks.STONE)) {
					slab = Blocks.STONE_SLAB.defaultBlockState();
				} else {
					continue;
				}

				BlockPos above = top.above();
				BlockState aboveState = level.getBlockState(above);
				if (!(aboveState.isAir() || aboveState.is(Blocks.SHORT_GRASS) || aboveState.is(Blocks.FERN))) {
					continue;
				}

				if (newTop != null) {
					level.setBlock(top, newTop, Block.UPDATE_CLIENTS);
				}
				level.setBlock(above, slab, Block.UPDATE_CLIENTS);
				placedAny = true;
			}
		}
		return placedAny;
	}

	/** Все 8 соседей на высоте y или y+1, и хотя бы один из 4 боковых ровно на y+1. */
	private static boolean isGentleStep(int[][] h, int i, int j, int y) {
		for (int di = -1; di <= 1; di++) {
			for (int dj = -1; dj <= 1; dj++) {
				if (di == 0 && dj == 0) {
					continue;
				}
				int n = h[i + di][j + dj];
				if (n != y && n != y + 1) {
					return false;
				}
			}
		}
		return h[i + 1][j] == y + 1 || h[i - 1][j] == y + 1
				|| h[i][j + 1] == y + 1 || h[i][j - 1] == y + 1;
	}
}
