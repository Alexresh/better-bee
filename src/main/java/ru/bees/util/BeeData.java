package ru.bees.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;
import ru.bees.data.BreedVariant;
import ru.bees.data.BreedVariants;
import ru.bees.util.Enums.*;

import java.util.ArrayList;

public class BeeData {

    public static final String BEE_KEY = "BB_Bee";
    public static final String GENES_KEY = "Genes";
    public static final String TYPE_KEY = "BeeType";
    public static final String RARITY_KEY = "Rarity";
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
        try{
            return bee.getNbtData().contains(TYPE_KEY) ? BeeTypes.valueOf(bee.getNbtData().getString(TYPE_KEY)) : null;
        } catch (IllegalArgumentException e){
            return null;
        }
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


        //set rarity and one of parent's type
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
        }else {
            //set basic types
            //rain
            if(serverWorld.isRaining()){
                setBeeType(child, BeeTypes.Rain);
            }
            //thunder
            if(serverWorld.isThundering()){
                setBeeType(child, BeeTypes.Thunder);
            }
            //end
            if(childEntity.getEntityWorld().getRegistryKey().getValue().equals(DimensionTypes.THE_END.getValue())){
                setBeeType(child, BeeTypes.End);
            }
            //nether
            if(childEntity.getEntityWorld().getRegistryKey().getValue().equals(DimensionTypes.THE_NETHER.getValue())){
                setBeeType(child, BeeTypes.Nether);
            }
        }

        //breed types
        ArrayList<BreedVariant> breedVariants = BreedVariants.getAllBreedVariants();
        for (BreedVariant variant: breedVariants) {
            //pizdes если типы равны и у родителей тоже одинаковые типы или если типы не равны и у родителей не равны
            if((variant.parent1Type() == variant.parent2Type() && parent1Type == parent2Type && parent1Type == variant.parent1Type())
                    || (parent1Type != parent2Type
                    && (parent1Type == variant.parent1Type() || parent1Type == variant.parent2Type())
                    && (parent2Type == variant.parent1Type() || parent2Type == variant.parent2Type()))){
                if(serverWorld.random.nextInt(100) <= variant.chance()){
                    //pizdes x2 если нужно чекнуть блок и если у хотя бы одно из родителей нет нужного блока под собой, то выйти
                    if(variant.checkedBlock() != null
                            && (!serverWorld.getBlockState(parentEntity1.getBlockPos().down()).isOf(variant.checkedBlock())
                            || !serverWorld.getBlockState(parentEntity2.getBlockPos().down()).isOf(variant.checkedBlock()))){
                        continue;
                    }
                    serverWorld.spawnParticles(ParticleTypes.FIREWORK,parentEntity1.getX(),parentEntity1.getY(),parentEntity1.getZ(),100, 0.1, 0.1,0.1,0.1);
                    setBeeType(child, variant.resultType());
                }
            }
        }
        //set generation
        setGeneration(parent1, parent2, child);
    }
}
