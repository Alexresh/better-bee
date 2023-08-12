package ru.bees.mixin.nbt;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bees.util.BeeData;
import ru.bees.util.INbtSaver;

@Mixin(Entity.class)
public abstract class BeeEntityMixin implements INbtSaver {
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
		if(nbt.contains(BeeData.BEE_KEY)){
			persistentData = nbt.getCompound(BeeData.BEE_KEY);
		}
	}

	@Inject(method = "writeNbt", at = @At("HEAD"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfoReturnable ci){
		if(persistentData != null){
			nbt.put(BeeData.BEE_KEY, persistentData);
		}
	}

}