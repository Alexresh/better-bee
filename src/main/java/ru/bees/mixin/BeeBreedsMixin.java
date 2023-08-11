package ru.bees.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bees.ServerMain;
import ru.bees.util.BeeData;
import ru.bees.util.INbtSaver;

import java.util.ArrayList;

@Mixin(BeeEntity.class)
public abstract class BeeBreedsMixin extends Entity {
    public BeeBreedsMixin(EntityType<? extends BeeEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createChild", at = @At("RETURN"))
    private void breedBeeMixin(ServerWorld serverWorld, PassiveEntity passiveEntity, CallbackInfoReturnable ci){
        if(ci.getReturnValue() instanceof BeeEntity smallBee){
            INbtSaver parent1 = (INbtSaver)this;
            INbtSaver parent2 = (INbtSaver)passiveEntity;
            INbtSaver child = (INbtSaver)smallBee;

            //generate child
            BeeData.generateChildBeeNbt(parent1, parent2, child, serverWorld);
        }
    }


}
