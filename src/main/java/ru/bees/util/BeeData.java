package ru.bees.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;
import ru.bees.util.Enums.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeeData {

    public static final String BEE_KEY = "BB_Bee";
    public static final String GENES_KEY = "Genes";
    public static final String TYPE_KEY = "BeeType";
    public static final String RARITY_KEY = "Rarity";
    private static final int GENE_TRANSFER_CHANCE = 75;
    private static final int COMMON_TRANSFORM_CHANCE = 10;
    private static final int RARE_TRANSFORM_CHANCE = 5;
    private static final int LEGENDARY_TRANSFORM_CHANCE = 1;
    public static final int MAX_RARITY = 10;
    public static final String GENERATION_KEY = "Generation";
    public static NbtCompound getAllBeeData(INbtSaver bee){
        return bee.getNbtData();
    }
    private static void setGeneration(INbtSaver parent1,INbtSaver parent2, INbtSaver child){
        int generation1 = parent1.getNbtData().getInt(GENERATION_KEY);
        int generation2 = parent2.getNbtData().getInt(GENERATION_KEY);
        int childGeneration = generation1 >= generation2 ? generation1 + 1 : generation2 + 1;
        child.getNbtData().putInt(GENERATION_KEY, childGeneration);
    }
    public static int getGeneration(INbtSaver bee){
        return bee.getNbtData().getInt(GENERATION_KEY);
    }

    private static void setBeeType(INbtSaver bee, BeeTypes type){
        bee.getNbtData().putString(TYPE_KEY, type.name());
    }
    public static @Nullable BeeTypes getBeeType(INbtSaver bee){
        return bee.getNbtData().contains(TYPE_KEY) ? BeeTypes.valueOf(bee.getNbtData().getString(TYPE_KEY)) : null;
    }
    private static void setBeeRarity(INbtSaver bee, int rarity){
        bee.getNbtData().putInt(RARITY_KEY, Math.min(rarity, MAX_RARITY));
    }

    public static int getBeeRarity(INbtSaver bee){
        int rarity = bee.getNbtData().getInt(RARITY_KEY);
        return rarity == 0 ? 1 : rarity;
    }

    private static void setGenes(INbtSaver bee, NbtList genes){
        bee.getNbtData().put(GENES_KEY, genes);
    }
    public static @Nullable NbtList getGenes(INbtSaver bee){
        return bee.getNbtData().contains(GENES_KEY) ? (NbtList) bee.getNbtData().get(GENES_KEY) : null;
    }
    public static boolean containGene(INbtSaver bee, Genes gen){
        NbtList genes = getGenes(bee);
        if(genes == null) return false;
        for (NbtElement currentGen:genes) {
            if(currentGen.asString().equals(gen.name())){
                return true;
            }
        }
        return false;
    }

    public static void generateChildBeeNbt(Entity parentEntity1, Entity parentEntity2, Entity childEntity, ServerWorld serverWorld){
        INbtSaver parent1 = ((INbtSaver) parentEntity1);
        INbtSaver parent2 = ((INbtSaver) parentEntity2);
        INbtSaver child = ((INbtSaver) childEntity);
        NbtList genes1 = getGenes(parent1);
        NbtList genes2 = getGenes(parent2);
        List<Genes> genes = new ArrayList<>();
        //set a base genes
        if(serverWorld.isRaining()){
            genes.add(Genes.Rain);
        }
        if(serverWorld.isThundering()){
            genes.add(Genes.Thunder);
        }
        if(childEntity.getEntityWorld().getRegistryKey().getValue().equals(DimensionTypes.THE_END.getValue())){
            genes.add(Genes.End);
        }
        if(childEntity.getEntityWorld().getRegistryKey().getValue().equals(DimensionTypes.THE_NETHER.getValue())){
            genes.add(Genes.Nether);
        }

        //Breeding basic
        if(genes1 != null && genes2 != null){
            //mix genes
            for (NbtElement parent1Gene:genes1) {
                if(serverWorld.random.nextInt(100) <= GENE_TRANSFER_CHANCE) genes.add(Genes.valueOf(parent1Gene.asString()));
            }
            for (NbtElement parent2Gene:genes2) {
                if(serverWorld.random.nextInt(100) <= GENE_TRANSFER_CHANCE) genes.add(Genes.valueOf(parent2Gene.asString()));
            }

            //overworld
            if(genes.contains(Genes.End) && genes.contains(Genes.Nether)){
                genes.clear();
                genes.add(Genes.Overworld);
            }
            //overworldBasic
            if(Collections.frequency(genes, Genes.Overworld) > 1){
                genes.clear();
                genes.add(Genes.OverworldBasic);
            }
            //endBasic
            if(genes.contains(Genes.End) && genes.contains(Genes.OverworldBasic)){
                genes.clear();
                genes.add(Genes.EndBasic);
            }
            //netherBasic
            if(genes.contains(Genes.Nether) && genes.contains(Genes.OverworldBasic)){
                genes.clear();
                genes.add(Genes.NetherBasic);
            }
        }

        //set rarity and parent type
        BeeTypes parent1Type = getBeeType(parent1);
        BeeTypes parent2Type = getBeeType(parent2);
        if(parent1Type != null && parent2Type != null){
            if(parent1Type == parent2Type){
                int parent1Rarity = getBeeRarity(parent1);
                int parent2Rarity = getBeeRarity(parent2);
                setBeeRarity(child, parent1Rarity == parent2Rarity ? parent1Rarity + 1 : (parent1Rarity + parent2Rarity) / 2 + 1);
                setBeeType(child, parent1Type);
            }else {
                setBeeType(child, serverWorld.random.nextBoolean() ? parent1Type : parent2Type);
            }

        }

        //mutate to type
        //obsidian
        trySetNewType(Genes.EndBasic, BeeTypes.Obsidian, Blocks.OBSIDIAN, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //chorus
        trySetNewType(Genes.EndBasic, BeeTypes.Chorus, Blocks.END_STONE, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //shulker
        trySetNewType(Genes.EndBasic, BeeTypes.Shulker, Blocks.SHULKER_BOX, RARE_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //quartz
        trySetNewType(Genes.NetherBasic, BeeTypes.Quartz, Blocks.QUARTZ_BLOCK, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //gold
        trySetNewType(Genes.NetherBasic, BeeTypes.Gold, Blocks.GOLD_BLOCK, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //glowstone
        trySetNewType(Genes.NetherBasic, BeeTypes.Glowstone, Blocks.GLOWSTONE, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //netherite
        trySetNewType(Genes.NetherBasic, BeeTypes.Netherite, Blocks.NETHERITE_BLOCK, LEGENDARY_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //soulsand
        trySetNewType(Genes.NetherBasic, BeeTypes.SoulSand, Blocks.SOUL_SAND, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //magma
        trySetNewType(Genes.NetherBasic, BeeTypes.Magma, Blocks.SHROOMLIGHT, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //dirt
        trySetNewType(Genes.OverworldBasic, BeeTypes.Dirt, Blocks.DIRT, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //cobblestone
        trySetNewType(Genes.OverworldBasic, BeeTypes.Cobblestone, Blocks.COBBLESTONE, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //wood
        trySetNewType(Genes.OverworldBasic, BeeTypes.Wood, Blocks.OAK_WOOD, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //stone
        trySetNewType(Genes.OverworldBasic, BeeTypes.Stone, Blocks.STONE, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);

        //mutate type
        //coal
        tryMutateType(BeeTypes.Stone, BeeTypes.Coal, Blocks.COAL_BLOCK, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //iron
        tryMutateType(BeeTypes.Stone, BeeTypes.Iron, Blocks.IRON_BLOCK, COMMON_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //diamond
        tryMutateType(BeeTypes.Stone, BeeTypes.Diamond, Blocks.DIAMOND_BLOCK, RARE_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);
        //emerald
        tryMutateType(BeeTypes.Stone, BeeTypes.Emerald, Blocks.EMERALD_BLOCK, RARE_TRANSFORM_CHANCE, genes, serverWorld, parentEntity1, parentEntity2, child);


        //distinct genes
        genes = genes.stream().distinct().toList();

        //set generation
        setGeneration(parent1, parent2, child);

        //write genes in child
        if(!genes.isEmpty()){
            NbtList nbtGenes = new NbtList();
            for (Genes gen:genes) {
                nbtGenes.add(NbtString.of(gen.name()));
            }
            setGenes(child, nbtGenes);
        }
    }

    private static void tryMutateType(BeeTypes checkedType, BeeTypes newType, Block checkedBlock, int chance, List<Genes> genes,ServerWorld serverWorld, Entity parentEntity1, Entity parentEntity2, INbtSaver child) {
        INbtSaver parent1NBT = (INbtSaver) parentEntity1;
        INbtSaver parent2NBT = (INbtSaver) parentEntity2;
        if(getBeeType(parent1NBT) == checkedType
                && getBeeType(parent2NBT) == checkedType
                && serverWorld.getBlockState(parentEntity1.getBlockPos().down()).isOf(checkedBlock)
                && serverWorld.getBlockState(parentEntity2.getBlockPos().down()).isOf(checkedBlock)
                && serverWorld.random.nextInt(100) <= chance){
            genes.clear();
            setBeeType(child, newType);
        }
    }

    private static void trySetNewType(Genes checkedGene, BeeTypes newType, Block checkedBlock, int chance, List<Genes> genes, ServerWorld serverWorld, Entity parentEntity1, Entity parentEntity2, INbtSaver child){
        INbtSaver parent1NBT = (INbtSaver) parentEntity1;
        INbtSaver parent2NBT = (INbtSaver) parentEntity2;
        if(containGene(parent1NBT, checkedGene)
                && containGene(parent2NBT, checkedGene)
                && serverWorld.getBlockState(parentEntity1.getBlockPos().down()).isOf(checkedBlock)
                && serverWorld.getBlockState(parentEntity2.getBlockPos().down()).isOf(checkedBlock)
                && serverWorld.random.nextInt(100) <= chance){
            genes.clear();
            setBeeType(child, newType);
        }
    }

}
