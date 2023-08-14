package ru.bees.util;

import net.minecraft.nbt.NbtCompound;
public class BeehiveData {
    public static final String HONEY_KEY = "BB_Beehive";

    public static NbtCompound getCompound(INbtSaver hive){
        return hive.getNbtData();
    }
    private static int getMaxHoney(Enums.Honeys honey){
        return switch (honey){
            case RainHoney, NetherHoney, EndHoney, OverworldHoney -> 64;
            case ThunderHoney -> 5;
        };
    }

    public static void addHoney(INbtSaver hive, Enums.Honeys honey, int amount){
        int currentHoney = getHoney(hive, honey);
        int maxHoney = getMaxHoney(honey);
        hive.getNbtData().putInt(honey.name(), currentHoney < maxHoney ? currentHoney + amount : maxHoney);
    }

    public static boolean tryRemoveHoney(INbtSaver hive, Enums.Honeys honey, int amount){
        int currentHoney = getHoney(hive, honey);
        if(currentHoney >= amount){
            hive.getNbtData().putInt(honey.name(), currentHoney - amount);
            return true;
        }
        return false;
    }

    private static int getHoney(INbtSaver hive, Enums.Honeys honey){return hive.getNbtData().getInt(honey.name());}

}
