package ru.bees;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bees.util.BeeData;
import ru.bees.util.BeehiveData;
import ru.bees.util.INbtSaver;

import static net.minecraft.block.BeehiveBlock.HONEY_LEVEL;

public class ServerMain implements ModInitializer {
	public static final String MOD_ID = "better-bees";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) ->{
			if(player.getStackInHand(hand).getItem() == Items.TORCH && entity instanceof BeeEntity bee){
				if(!world.isClient) {
					bee.growUp(1500);
				}
				return ActionResult.SUCCESS;
			}
			if(player.getStackInHand(hand).getItem() == Items.WRITABLE_BOOK && entity instanceof BeeEntity bee){
				if(!world.isClient){
					ItemStack writableBook = player.getStackInHand(hand);
					NbtCompound compound = new NbtCompound();
					NbtList pages = new NbtList();
					NbtCompound beeData = BeeData.getAllBeeData(((INbtSaver) bee));
					String page = beeData.toString()
							.replace(',', '\n')
							.replace("{","")
							.replace("}","")
							.replace("[", "[\n")
							.replace("]","\n]");
					pages.add(NbtString.of(page));
					compound.put("pages", pages);
					writableBook.setNbt(compound);
				}
				return ActionResult.SUCCESS;
			}

			return ActionResult.PASS;
		});
		//get beehive block with data
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if(!world.isClient){
				if(world.getBlockEntity(hitResult.getBlockPos()) instanceof BeehiveBlockEntity beehiveBlockEntity && player.getStackInHand(hand).getEnchantments().toString().contains("silk_touch")){
					ItemStack itemStack = new ItemStack(Items.BEEHIVE);
					NbtCompound compound = new NbtCompound();
					//insert a BB data in compound
					compound.put(BeehiveData.honeyKey, BeehiveData.getCompound(((INbtSaver) beehiveBlockEntity)));
					boolean hasBees = !beehiveBlockEntity.hasNoBees();
					//insert bees
					if (hasBees) {
						compound.put("Bees", beehiveBlockEntity.getBees());
					}
					//insert honeyLevel
					int honeyLevel = world.getBlockState(hitResult.getBlockPos()).get(HONEY_LEVEL);
					compound.putInt("honey_level", honeyLevel);
					//insert blockStateTag
					itemStack.setSubNbt("BlockStateTag", compound);
					//insert BlockEntityTag
					BlockItem.setBlockEntityNbt(itemStack, BlockEntityType.BEEHIVE, compound);
					//spawn item in world
					ItemEntity itemEntity = new ItemEntity(world, hitResult.getBlockPos().getX(), hitResult.getBlockPos().getY() + 1, hitResult.getBlockPos().getZ(), itemStack);
					itemEntity.setToDefaultPickupDelay();
					world.spawnEntity(itemEntity);
					//remove block from world
					world.removeBlock(hitResult.getBlockPos(),false);
					return ActionResult.SUCCESS;
				}
			}
			return ActionResult.PASS;
		});
	}
}