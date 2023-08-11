package ru.bees.mixin.nbt;

import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.bees.util.INbtSaver;

import static ru.bees.util.BeehiveData.honeyKey;

@Mixin(BeehiveBlockEntity.class)
public abstract class BeehiveBlockEntityMixin implements INbtSaver {
    private NbtCompound persistentData;

    @Override
    public NbtCompound getNbtData() {
        if(this.persistentData == null){
            this.persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void readNbtMixin(NbtCompound nbt, CallbackInfo ci){
        if(nbt.contains(honeyKey)){
            persistentData = nbt.getCompound(honeyKey);
        }
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci){
        if(persistentData != null){
            nbt.put(honeyKey, persistentData);
        }
    }
}
