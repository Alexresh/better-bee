package ru.bees.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.bees.data.BeeDrop;
import ru.bees.data.BeeDrops;
import ru.bees.util.BeeData;
import ru.bees.util.BeehiveData;
import ru.bees.util.Enums.*;
import ru.bees.util.INbtSaver;

@Mixin(BeehiveBlockEntity.class)
public abstract class BeehiveEntityMixin extends BlockEntity {

    public BeehiveEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "tryEnterHive(Lnet/minecraft/entity/Entity;ZI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;removeAllPassengers()V", shift = At.Shift.AFTER))
    private void addBee(Entity entity, boolean hasNectar, int ticksInHive, CallbackInfo ci){
        if(entity instanceof BeeEntity bee && hasNectar){

            INbtSaver BBbee = (INbtSaver) bee;
            INbtSaver BBBeehive = (INbtSaver) this;

            //drop items and add honey
            BeeTypes type = BeeData.getBeeType(BBbee);
            int beeRarity = BeeData.getBeeRarity(BBbee);
            if(type == null) return;
            switch (type){
                case Rain -> BeehiveData.addHoney(BBBeehive, Honeys.RainHoney, 1);
                case Water -> BeehiveData.addHoney(BBBeehive, Honeys.WaterHoney, world.random.nextInt(6));
                case Thunder -> BeehiveData.addHoney(BBBeehive, Honeys.ThunderHoney, 1);
                case End -> BeehiveData.addHoney(BBBeehive, Honeys.EndHoney, world.random.nextInt(6));
                case Nether -> BeehiveData.addHoney(BBBeehive, Honeys.NetherHoney, world.random.nextInt(6));
                case Overworld -> BeehiveData.addHoney(BBBeehive, Honeys.OverworldHoney, world.random.nextInt(6));
                default -> {}
            }
            BeeDrop drop = BeeDrops.getBeeDrop(type);
            if(drop != null){
                dropItem(drop, beeRarity);
            }
        }
    }

    private void dropItem(BeeDrop drop, float beeRarity){
        if(world.random.nextInt(1000) <= drop.chance() * beeRarity){
            ItemEntity itemEntity = new ItemEntity(this.world, this.getPos().getX() + 0.5, this.getPos().getY(), this.getPos().getZ() + 0.5, new ItemStack(drop.droppedItem()));
            itemEntity.getStack().setCount((int) Math.floor(beeRarity * drop.multiplier()) + 1);
            itemEntity.setPickupDelay(0);
            this.getWorld().spawnEntity(itemEntity);
        }
    }
}