package ru.bees.data;

import java.util.ArrayList;

import net.minecraft.block.Blocks;
import ru.bees.util.Enums.*;

public class BreedVariants {
    private static final int VERY_COMMON_TRANSFER_CHANCE = 50;
    private static final int COMMON_TRANSFORM_CHANCE = 10;
    private static final int RARE_TRANSFORM_CHANCE = 5;
    private static final int LEGENDARY_TRANSFORM_CHANCE = 1;
    private static ArrayList<BreedVariant> variants = new ArrayList<>(){
        {
            add(new BreedVariant(BeeTypes.End, BeeTypes.Nether, BeeTypes.Overworld, null, VERY_COMMON_TRANSFER_CHANCE));
            add(new BreedVariant(BeeTypes.Overworld, BeeTypes.Overworld, BeeTypes.OverworldBasic, null, VERY_COMMON_TRANSFER_CHANCE));
            add(new BreedVariant(BeeTypes.OverworldBasic, BeeTypes.Nether, BeeTypes.NetherBasic, null, VERY_COMMON_TRANSFER_CHANCE));
            add(new BreedVariant(BeeTypes.OverworldBasic, BeeTypes.End, BeeTypes.EndBasic, null, VERY_COMMON_TRANSFER_CHANCE));
            add(new BreedVariant(BeeTypes.EndBasic, BeeTypes.EndBasic, BeeTypes.Obsidian, Blocks.OBSIDIAN, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.EndBasic, BeeTypes.EndBasic, BeeTypes.Chorus, Blocks.END_STONE, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.EndBasic, BeeTypes.EndBasic, BeeTypes.Shulker, Blocks.SHULKER_BOX, RARE_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.NetherBasic, BeeTypes.NetherBasic, BeeTypes.Quartz, Blocks.QUARTZ_BLOCK, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.NetherBasic, BeeTypes.NetherBasic, BeeTypes.Gold, Blocks.GOLD_BLOCK, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.NetherBasic, BeeTypes.NetherBasic, BeeTypes.Glowstone, Blocks.GLOWSTONE, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.NetherBasic, BeeTypes.NetherBasic, BeeTypes.SoulSand, Blocks.SOUL_SAND, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.NetherBasic, BeeTypes.NetherBasic, BeeTypes.Magma, Blocks.SHROOMLIGHT, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.NetherBasic, BeeTypes.NetherBasic, BeeTypes.Magma, Blocks.SHROOMLIGHT, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.OverworldBasic, BeeTypes.OverworldBasic, BeeTypes.Dirt, Blocks.DIRT, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.OverworldBasic, BeeTypes.OverworldBasic, BeeTypes.Cobblestone, Blocks.COBBLESTONE, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.OverworldBasic, BeeTypes.OverworldBasic, BeeTypes.Stone, Blocks.STONE, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.OverworldBasic, BeeTypes.OverworldBasic, BeeTypes.Wood, Blocks.OAK_LOG, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Rain, BeeTypes.Rain, BeeTypes.Water, null, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Magma, BeeTypes.Diamond, BeeTypes.Netherite, Blocks.ANCIENT_DEBRIS, LEGENDARY_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Stone, BeeTypes.Quartz, BeeTypes.Gravel, null, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Stone, BeeTypes.Stone, BeeTypes.Coal, Blocks.COAL_BLOCK, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Stone, BeeTypes.Stone, BeeTypes.Iron, Blocks.IRON_BLOCK, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Gold, BeeTypes.Iron, BeeTypes.Emerald, Blocks.EMERALD_BLOCK, RARE_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Emerald, BeeTypes.Glass, BeeTypes.Diamond, Blocks.DIAMOND_BLOCK, RARE_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Stone, BeeTypes.Magma, BeeTypes.Redstone, null, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Gravel, BeeTypes.Cobblestone, BeeTypes.Sand, null, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Sand, BeeTypes.Coal, BeeTypes.Glass, null, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Chorus, BeeTypes.Quartz, BeeTypes.Lapis, null, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Water, BeeTypes.Dirt, BeeTypes.Clay, null, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Stone, BeeTypes.Stone, BeeTypes.Copper, Blocks.COPPER_ORE, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Wood, BeeTypes.Wood, BeeTypes.BirchWood, Blocks.BIRCH_LOG, VERY_COMMON_TRANSFER_CHANCE));
            add(new BreedVariant(BeeTypes.Wood, BeeTypes.Wood, BeeTypes.AcaciaWood, Blocks.ACACIA_LOG, VERY_COMMON_TRANSFER_CHANCE));
            add(new BreedVariant(BeeTypes.Wood, BeeTypes.Wood, BeeTypes.DarkOakWood, Blocks.DARK_OAK_LOG, VERY_COMMON_TRANSFER_CHANCE));
            add(new BreedVariant(BeeTypes.Wood, BeeTypes.Wood, BeeTypes.SpruceWood, Blocks.SPRUCE_LOG, VERY_COMMON_TRANSFER_CHANCE));
            add(new BreedVariant(BeeTypes.Wood, BeeTypes.Wood, BeeTypes.CherryWood, Blocks.CHERRY_LOG, VERY_COMMON_TRANSFER_CHANCE));
            add(new BreedVariant(BeeTypes.Wood, BeeTypes.Wood, BeeTypes.Bamboo, Blocks.BAMBOO_BLOCK, VERY_COMMON_TRANSFER_CHANCE));
            add(new BreedVariant(BeeTypes.Wood, BeeTypes.Wood, BeeTypes.JungleWood, Blocks.JUNGLE_LOG, VERY_COMMON_TRANSFER_CHANCE));
            add(new BreedVariant(BeeTypes.Wood, BeeTypes.Wood, BeeTypes.MangroveWood, Blocks.MANGROVE_LOG, VERY_COMMON_TRANSFER_CHANCE));
            add(new BreedVariant(BeeTypes.Stone, BeeTypes.Stone, BeeTypes.Deepslate, Blocks.DEEPSLATE, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Deepslate, BeeTypes.Deepslate, BeeTypes.Calcite, Blocks.CALCITE, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Calcite, BeeTypes.Glass, BeeTypes.Amethyst, null, COMMON_TRANSFORM_CHANCE));
            add(new BreedVariant(BeeTypes.Amethyst, BeeTypes.Water, BeeTypes.Ice, null, COMMON_TRANSFORM_CHANCE));

        }
    };

    public static ArrayList<BreedVariant> getAllBreedVariants(){
        return variants;
    }
}
