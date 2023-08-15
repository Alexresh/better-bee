package ru.bees.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends Entity {
    public BeeEntityMixin(EntityType<? extends BeeEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = "mobTick", at = @At("HEAD"))
    private void tick(CallbackInfo ci){
        BeeEntity th = ((BeeEntity)(Object) this);
        if(th.hasAngerTime()){
            th.onHoneyDelivered();
        }
    }
}
