package ru.bees.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bees.util.BeeData;

@Mixin(BeeEntity.class)
public abstract class BeeBreedsMixin extends Entity {
    public BeeBreedsMixin(EntityType<? extends BeeEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createChild", at = @At("RETURN"))
    private void breedBeeMixin(ServerWorld serverWorld, PassiveEntity passiveEntity, CallbackInfoReturnable ci){
        if(ci.getReturnValue() instanceof BeeEntity smallBee){
            BeeData.generateChildBeeNbt(this, passiveEntity, smallBee, serverWorld);
        }
    }


}
