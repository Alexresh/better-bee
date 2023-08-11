package ru.bees.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.bees.ServerMain;
import ru.bees.util.BeeData;
import ru.bees.util.BeehiveData;
import ru.bees.util.INbtSaver;

@Mixin(BeehiveBlockEntity.class)
public abstract class BeehiveEntityMixin extends BlockEntity {

    public BeehiveEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "tryEnterHive(Lnet/minecraft/entity/Entity;ZI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;removeAllPassengers()V", shift = At.Shift.AFTER))
    private void addBee(Entity entity, boolean hasNectar, int ticksInHive, CallbackInfo ci){
        if(entity instanceof BeeEntity bee /*&& hasNectar*/){
            INbtSaver BBbee = (INbtSaver) bee;
            INbtSaver BBBeehive = (INbtSaver) this;
            if(BeeData.containGene(BBbee, BeeData.Genes.Rain)){
                BeehiveData.addHoney(BBBeehive, BeehiveData.Honeys.RainHoney, 1);
            }
            if(BeeData.containGene(BBbee, BeeData.Genes.Thunder)){
                BeehiveData.addHoney(BBBeehive, BeehiveData.Honeys.ThunderHoney, 1);
            }
            if(BeeData.containGene(BBbee, BeeData.Genes.Nether)){
                BeehiveData.addHoney(BBBeehive, BeehiveData.Honeys.NetherHoney, 5);
            }
            if(BeeData.containGene(BBbee, BeeData.Genes.Owerworld)){
                BeehiveData.addHoney(BBBeehive, BeehiveData.Honeys.OverworldHoney, 5);
            }
            if(BeeData.containGene(BBbee, BeeData.Genes.End)){
                BeehiveData.addHoney(BBBeehive, BeehiveData.Honeys.EndHoney, 5);
            }

        }
    }
}