package ru.bees.util;

import net.minecraft.nbt.NbtCompound;

public class BeehiveData {
    public static final String honeyKey  = "BB_Beehive";

    public enum Honeys{
        RainHoney,
        ThunderHoney,
        NetherHoney,
        EndHoney,
        OverworldHoney
    }

    public static NbtCompound getCompound(INbtSaver hive){
        return hive.getNbtData();
    }
    private static int getMaxHoney(Honeys honey){
        int maxHoney;
        switch (honey){
            case RainHoney, NetherHoney, OverworldHoney, EndHoney -> maxHoney = 64;
            case ThunderHoney -> maxHoney = 5;
            default -> maxHoney = 0;
        }
        return maxHoney;
    }

    public static void addHoney(INbtSaver hive, Honeys honey, int amount){
        int currentHoney = getHoney(hive, honey);
        int maxHoney = getMaxHoney(honey);
        hive.getNbtData().putInt(honey.name(), currentHoney < maxHoney ? currentHoney + amount : maxHoney);
    }

    public static boolean tryRemoveHoney(INbtSaver hive, Honeys honey, int amount){
        if(getHoney(hive, honey) >= amount){
            hive.getNbtData().putInt(honey.name(), getHoney(hive, honey) - amount);
            return true;
        }
        return false;
    }

    private static int getHoney(INbtSaver hive, Honeys honey){return hive.getNbtData().getInt(honey.name());}

}
