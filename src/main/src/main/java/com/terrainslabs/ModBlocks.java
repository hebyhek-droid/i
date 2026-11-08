package com.terrainslabs;

import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
	public static final Block DIRT_SLAB = register(
			"dirt_slab", SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT));
	public static final Block GRASS_SLAB = register(
			"grass_slab", SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK));

	private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory,
			BlockBehaviour.Properties properties) {
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK,
				Identifier.fromNamespaceAndPath(TerrainSlabs.MOD_ID, name));
		Block block = blockFactory.apply(properties.setId(blockKey));

		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
				Identifier.fromNamespaceAndPath(TerrainSlabs.MOD_ID, name));
		BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
		Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
	}

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(tab -> {
			tab.accept(DIRT_SLAB.asItem());
			tab.accept(GRASS_SLAB.asItem());
		});
	}
}
