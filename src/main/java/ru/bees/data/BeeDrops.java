package ru.bees.data;

import net.minecraft.item.Items;
import ru.bees.util.Enums.*;

public class BeeDrops {
    private static final int COMMON_CHANCE = 100;
    private static final int RARE_CHANCE = 10;
    private static final float LEGENDARY_MULTIPLIER = 0.99f;
    private static final float RARE_MULTIPLIER = 0.49f;
    private static final float COMMON_MULTIPLIER = 0.19f;
    private static final float ZERO_MULTIPLIER = 0;

    public static BeeDrop getBeeDrop(BeeTypes type){
        return switch (type){
            case Chorus -> new BeeDrop(Items.CHORUS_FRUIT, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Obsidian -> new BeeDrop(Items.OBSIDIAN, COMMON_CHANCE, RARE_MULTIPLIER);
            case Shulker -> new BeeDrop(Items.SHULKER_SHELL, COMMON_CHANCE, COMMON_MULTIPLIER);
            case Quartz -> new BeeDrop(Items.QUARTZ, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Gold -> new BeeDrop(Items.GOLD_INGOT, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Glowstone -> new BeeDrop(Items.GLOWSTONE, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Netherite -> new BeeDrop(Items.NETHERITE_SCRAP, RARE_CHANCE, COMMON_MULTIPLIER);
            case SoulSand -> new BeeDrop(Items.SOUL_SAND, COMMON_CHANCE, RARE_MULTIPLIER);
            case Magma -> new BeeDrop(Items.MAGMA_CREAM, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Dirt -> new BeeDrop(Items.DIRT, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Cobblestone -> new BeeDrop(Items.COBBLESTONE, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Wood -> new BeeDrop(Items.OAK_WOOD, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Stone -> new BeeDrop(Items.STONE, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Coal -> new BeeDrop(Items.COAL, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Iron -> new BeeDrop(Items.RAW_IRON, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Diamond -> new BeeDrop(Items.DIAMOND, COMMON_CHANCE, ZERO_MULTIPLIER);
            case Emerald -> new BeeDrop(Items.EMERALD, COMMON_CHANCE, RARE_MULTIPLIER);
            case Clay -> new BeeDrop(Items.CLAY_BALL, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Sand -> new BeeDrop(Items.GRAVEL, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Glass -> new BeeDrop(Items.GLASS, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Lapis -> new BeeDrop(Items.CLAY_BALL, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Water -> new BeeDrop(Items.HEART_OF_THE_SEA, RARE_CHANCE, ZERO_MULTIPLIER);
            case Copper -> new BeeDrop(Items.RAW_COPPER, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Gravel -> new BeeDrop(Items.GRAVEL, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            case Redstone -> new BeeDrop(Items.REDSTONE, COMMON_CHANCE, LEGENDARY_MULTIPLIER);
            default -> null;
        };
    }

}
