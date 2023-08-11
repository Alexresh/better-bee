package ru.bees.util;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import ru.bees.mixin.nbt.BeeEntityMixin;

import java.util.ArrayList;

public class BeeData {

    public static final String beeKey  = "BB_Bee";
    public static final String genesKey  = "Genes";
    public static final String generationKey = "Generation";

    public enum Genes{
        Rain,
        Thunder,
        End,
        Nether,
        Owerworld

    }
    public static NbtCompound getAllBeeData(INbtSaver bee){
        return bee.getNbtData();
    }
    private static void setGeneration(INbtSaver parent1,INbtSaver parent2, INbtSaver child){
        int generation1 = parent1.getNbtData().getInt(generationKey);
        int generation2 = parent2.getNbtData().getInt(generationKey);
        int childGeneration = generation1 >= generation2 ? generation1 + 1 : generation2 + 1;
        child.getNbtData().putInt(generationKey, childGeneration);
    }

    private static void setGenes(INbtSaver bee, NbtList genes){
        bee.getNbtData().put(genesKey, genes);
    }
    public static NbtList getGenes(INbtSaver bee){
        if(!bee.getNbtData().contains(genesKey)) return null;
        return (NbtList) bee.getNbtData().get(genesKey);
    }
    public static boolean containGene(INbtSaver bee, Genes gen){
        if(getGenes(bee) != null){
            return getGenes(bee).toString().contains(gen.name());
        }
        return false;
    }

    public static void generateChildBeeNbt(INbtSaver parent1, INbtSaver parent2, INbtSaver child, ServerWorld serverWorld){
        NbtList genes1 = getGenes(parent1);
        NbtList genes2 = getGenes(parent2);
        ArrayList<Genes> genes = new ArrayList<>();
        if(genes1 != null && genes2 != null){
            //overworld bee
            if((genes1.toString().contains(Genes.End.name()) && genes2.toString().contains(BeeData.Genes.Nether.name()))
                    || (genes1.toString().contains(Genes.Nether.name()) && genes2.toString().contains(Genes.End.name()))){
                genes.add(Genes.Owerworld);
            }
        }
        setGeneration(parent1, parent2, child);

        if(serverWorld.isRaining()){
            genes.add(Genes.Rain);
        }

        if(serverWorld.isThundering()){
            genes.add(Genes.Thunder);
        }

        if(((BeeEntity)child).getEntityWorld().getRegistryKey().getValue().equals(DimensionTypes.THE_END.getValue())){
            genes.add(Genes.End);
        }
        if(((BeeEntity) child).getEntityWorld().getRegistryKey().getValue().equals(DimensionTypes.THE_NETHER.getValue())){
            genes.add(Genes.Nether);
        }
        NbtList nbtGenes = new NbtList();
        for (Genes gen:genes) {
            nbtGenes.add(NbtString.of(gen.name()));
        }
        setGenes(child, nbtGenes);
    }

}
